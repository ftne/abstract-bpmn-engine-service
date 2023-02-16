package com.ftne.absys.bpmn.service

import com.ftne.absys.bpmn.model.ProcessContext
import kotlinx.serialization.ExperimentalSerializationApi
import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.cloud.sleuth.Tracer
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.math.min

@ExperimentalSerializationApi
@Service
class BpmnProcessService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val tracer: Tracer
) {

    fun startProcess(id: UUID, ctx: ProcessContext) {
        if (isProcessStarted(id)) throw IllegalStateException("Process $id already started")
        runtimeService.createProcessInstanceByKey(PROCESS_KEY)
            .businessKey(id.toString())
            .setVariables(ctx.asMap)
            .execute()
            .also { logger.debug { "Process $id started" } }
    }

    fun completeHumanTask(id: UUID) {
        val task = taskService.createTaskQuery()
            .processInstanceBusinessKey(id.toString())
            .singleResult()
            ?: throw IllegalArgumentException("Unable to find active human task for $id.")
        taskService.complete(task.id)
    }

    private fun createIncident(businessKey: String, errorMessage: String?) {
        logger.error("Creating incident for $businessKey due to $errorMessage")
        try {
            runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)
                .list()
                .filterIsInstance<ExecutionEntity>()
                .firstOrNull { it.activityId != null } // assuming that current execution relates to some activity on bpmn diagram
                ?.let {
                    runtimeService.createIncident(
                        "Custom",
                        it.id,
                        "Incident data - " + it.processInstanceId,
                        errorMessage?.substring(0, min(4000, errorMessage.length))
                    )
                }
                ?: logger.error("No suitable executions found to create incident for $businessKey")
        } catch (e: Exception) {
            logger.error("Failed to create incident due to exception", e)
        }
    }

    fun correlateMessage(id: UUID, msgName: String) {
        logger.info { "correlateMessage: ${tracer.currentSpan()?.context()?.traceId()}" }
        runtimeService.createMessageCorrelation(msgName)
            .processInstanceBusinessKey(id.toString())
            .correlate()
    }

    private fun isProcessStarted(businessKey: UUID): Boolean = findProcessInstance(businessKey) != null

    private fun findProcessInstance(businessKey: UUID): ProcessInstance? = runtimeService.createProcessInstanceQuery()
        .processInstanceBusinessKey(businessKey.toString(), PROCESS_KEY)
        .singleResult()

    fun cancelProcess(id: UUID, deleteReason: String) =
        runtimeService.deleteProcessInstance(id.toString(), deleteReason)
            .also { logger.warn { "Process $id deleted for a reason: $deleteReason" } }

    companion object {
        val logger = KotlinLogging.logger {  }
        const val PROCESS_KEY = "abstract-system-root-process"
    }
}
