package com.ftne.absys.bpmn.controller

import BpmnProcessService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class BpmnEngineController(
    private val processService: BpmnProcessService
) {

    @GetMapping("/process/start/{id}")
    fun startProcess(@PathVariable id: UUID) {
        processService.startProcess(id)
    }

}