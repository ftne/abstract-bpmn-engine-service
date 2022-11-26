package com.ftne.absys.bpmn.delegate

import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import kotlin.random.Random

@Service("numbersApiInvoker")
class NumbersApiInvoker(
    private val restTemplate: RestTemplate
): JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val number = Random.nextInt(301)
        val numberFact = restTemplate.getForObject(NUMBERS_API_URL + number, String::class.java)
        logger.info { "Fact for number: $numberFact" }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        const val NUMBERS_API_URL = "http://numbersapi.com/"
    }
}