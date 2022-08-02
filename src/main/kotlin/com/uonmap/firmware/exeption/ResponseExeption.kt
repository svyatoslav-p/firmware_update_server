package com.uonmap.firmware.exeption

import org.springframework.http.HttpStatus

class FileNotFoundExeption (description: String) : BaseException(
    HttpStatus.NOT_FOUND,
    ApiError(
        error = "file.not.found",
        description
    )
)

class HeaderNotCorrectExeption (description: String) : BaseException(
    HttpStatus.BAD_REQUEST,
    ApiError(
        error = "header.not.correct",
        description
    )
)
