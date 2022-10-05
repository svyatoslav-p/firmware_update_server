package com.uonmap.firmware.http.exeption

import com.uonmap.firmware.http.GenericResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

abstract class BaseException(
    val httpStatus: HttpStatus = HttpStatus.OK,
    val genericResponse: GenericResponse,
): RuntimeException(genericResponse.error.description)

@ControllerAdvice
class CustomErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(ex: BaseException): ResponseEntity<GenericResponse> = ResponseEntity(ex.genericResponse, ex.httpStatus)
}
