package com.ftne.absys.bpmn.error

import java.time.Instant

data class ExceptionResponseModel(
    val message: String,
    val timestamp: Instant
)