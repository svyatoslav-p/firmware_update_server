package com.uonmap.firmware.exeption

import com.fasterxml.jackson.databind.util.StdDateFormat
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.text.SimpleDateFormat
import java.util.*

val ISO8601_FORMAT = SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601)
private val log = KotlinLogging.logger {}

data class ApiError(
    val error: String,
    val description: String,
    var timestamp: String = ISO8601_FORMAT.format(Date())
)

abstract class BaseException(
    val httpStatus: HttpStatus,
    val apiError: ApiError,
): RuntimeException(apiError.description)

@ControllerAdvice
class CustomErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<ApiError> {
        log.error { ex.apiError.description }
        return ResponseEntity(ex.apiError, ex.httpStatus)
    }
}
