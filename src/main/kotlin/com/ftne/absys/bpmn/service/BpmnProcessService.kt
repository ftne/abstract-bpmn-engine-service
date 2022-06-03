package com.ftne.absys.bpmn.service

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BpmnProcessService(
    private val runtimeService: RuntimeService
) {

    fun startProcess(id: UUID) {
        if (isProcessStarted(id)) throw IllegalStateException("Process already started for id $id")
        runtimeService.createProcessInstanceByKey(PROCESS_KEY)
            .businessKey(id.toString())
//            .setVariables()
            .execute()
    }

    private fun isProcessStarted(businessKey: UUID): Boolean = findProcessInstance(businessKey) != null

    private fun findProcessInstance(businessKey: UUID): ProcessInstance? = runtimeService.createProcessInstanceQuery()
        .processInstanceBusinessKey(businessKey.toString(), PROCESS_KEY)
        .singleResult()

    companion object {
        const val PROCESS_KEY = "abstract-system-root-process"
    }
}