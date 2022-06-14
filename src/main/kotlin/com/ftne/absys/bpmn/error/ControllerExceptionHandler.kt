package com.ftne.absys.bpmn.error

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.Instant

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException)
            : ResponseEntity<ExceptionResponseModel> {
        logger.error { ex.message }
        return buildExceptionResponse(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleCommonException(ex: Exception): ResponseEntity<ExceptionResponseModel> =
        buildExceptionResponse(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
            .also { logger.error { ex.printStackTrace() } }

    private fun buildExceptionResponse(message: String?, status: HttpStatus) =
        ResponseEntity.status(status)
            .body(ExceptionResponseModel(message ?: "Unknown error", Instant.now()))

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}