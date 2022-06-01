package com.ftne.absys.bpmn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BpmnEngineApplication

fun main(args: Array<String>) {
    runApplication<BpmnEngineApplication>(*args)
}
