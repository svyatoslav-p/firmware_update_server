package com.uonmap.firmware.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.util.*

val ISO8601_FORMAT = SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601)
private val log = KotlinLogging.logger {}

data class ApiError(
    val code: String = "",
    val description: String = ""
)

data class GenericResponse(
    var status: Int = HttpStatus.OK.value(),
    var message: Any? = null,
    var error: ApiError = ApiError(),
    var timestamp: String = ISO8601_FORMAT.format(Date())
){
    companion object {
        fun getError(
            error: ApiError,
            httpStatus: Int,
        ): GenericResponse {
            log.error { "API Error => code: ${error.code} desc: ${error.description}" }
            return GenericResponse(httpStatus, null, error)
        }
    }

    constructor(data: Any?, objectMapper: ObjectMapper? = null) : this(
        status = HttpStatus.OK.value()
    ) {
        message = objectMapper?.valueToTree(data) ?: data
    }

    override fun toString(): String {
        return jacksonObjectMapper().writeValueAsString(this)
    }
}

enum class ApiErrorCode(val code: String) {
    UNKNOWN("10000"),
    NOT_FOUND("10001"),
    HTTP_HEAD_NOT_CORRECT("10002");
}
