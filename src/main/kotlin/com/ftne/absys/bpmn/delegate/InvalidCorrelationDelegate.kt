package com.ftne.absys.bpmn.delegate

import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

@Component
class InvalidCorrelationDelegate: JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        try {
            throw IllegalStateException("Invalid correlation key")
        } catch (ex: Exception) {
            logger.error("Unexpected error", ex)
            throw ex
        }
    }

    companion object {
        val logger = KotlinLogging.logger {  }
    }
}