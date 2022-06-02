package com.ftne.absys.bpmn.delegate

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class RestServiceInvoker: JavaDelegate {
    override fun execute(execution: DelegateExecution?) {
        println("process REST request")
    }

}