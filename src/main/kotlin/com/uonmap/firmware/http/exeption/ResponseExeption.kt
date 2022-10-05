package com.uonmap.firmware.http.exeption

import com.uonmap.firmware.http.ApiError
import com.uonmap.firmware.http.ApiErrorCode.NOT_FOUND
import com.uonmap.firmware.http.ApiErrorCode.HTTP_HEAD_NOT_CORRECT
import com.uonmap.firmware.http.GenericResponse
import org.springframework.http.HttpStatus

class FileNotFoundExeption (description: String) : BaseException(
    genericResponse = GenericResponse.getError(
        ApiError(
            NOT_FOUND.code,
            description
        ),
        HttpStatus.NOT_FOUND.value()
    )
)

class HeaderNotCorrectExeption (description: String) : BaseException(
    genericResponse = GenericResponse.getError(
        ApiError(
            HTTP_HEAD_NOT_CORRECT.code,
            description),
        HttpStatus.BAD_REQUEST.value()
    )
)
