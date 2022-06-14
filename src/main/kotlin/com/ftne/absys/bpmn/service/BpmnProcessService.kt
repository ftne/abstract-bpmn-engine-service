package com.ftne.absys.bpmn.service

import com.ftne.absys.bpmn.model.ProcessContext
import kotlinx.serialization.ExperimentalSerializationApi
import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.stereotype.Service
import java.util.UUID

@ExperimentalSerializationApi
@Service
class BpmnProcessService(
    private val runtimeService: RuntimeService
) {

    fun startProcess(id: UUID, ctx: ProcessContext) {
        if (isProcessStarted(id)) throw IllegalStateException("Process $id already started")
        runtimeService.createProcessInstanceByKey(PROCESS_KEY)
            .businessKey(id.toString())
            .setVariables(ctx.asMap)
            .execute()
            .also { logger.debug { "Process $id started" } }
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
