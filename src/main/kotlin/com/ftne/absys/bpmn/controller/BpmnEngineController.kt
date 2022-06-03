package com.ftne.absys.bpmn.controller

import com.ftne.absys.bpmn.model.ProcessContext
import com.ftne.absys.bpmn.service.BpmnProcessService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class BpmnEngineController(
    private val processService: BpmnProcessService
) {

    @PostMapping("/process/start/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun startProcess(@PathVariable id: UUID, @RequestBody ctx: ProcessContext) {
        processService.startProcess(id)
    }

}