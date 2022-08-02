package com.uonmap.firmware.controller

import com.uonmap.firmware.exeption.*
import com.uonmap.firmware.service.EspFileSystem
import com.uonmap.firmware.service.EspFirmware
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/esp")
class EspApi(
    private val espFw: EspFirmware,
    private val espFs: EspFileSystem
) {
    @Operation(summary = "Get the update file for ESP32",
        description = "Returns the update file or an explanation why the file did not attach.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Firmware file",
                headers= [
                    (Header(name="x-MD5", description ="MD5 sum of found file"))],
                content = [
                    (Content(mediaType = "application/json", array = (
                            ArraySchema(schema = Schema(type = "object", format = "binary")))))]),
            ApiResponse(responseCode = "400", description = "Invalid request",
                content = [
                    (Content(mediaType = "application/json", array = (
                            ArraySchema(schema = Schema(implementation = ApiError::class)))))]),
            ApiResponse(responseCode = "404", description = "File Not Found",
                content = [
                    (Content(mediaType = "application/json", array = (
                            ArraySchema(schema = Schema(implementation = ApiError::class)))))])]
    )
    @Parameters(
        value = [
            Parameter(required = true, name = "x-ESP32-mode", example = "spiffs",
                description = "Type file response spiffs or sketch", `in` = ParameterIn.HEADER),
            Parameter(required = true, name = "x-ESP32-STA-MAC", example = "24:6F:28:DA:7E:B0",
                description = "MAC address device", `in` = ParameterIn.HEADER),
            Parameter(required = true, name = "x-ESP32-version", example = "0.0.25",
                description = "Current version firmware or file system ESP32", `in` = ParameterIn.HEADER)
        ]
    )
    @GetMapping("/update")
    fun update(
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Resource> =
        if (headers["x-esp32-mode"] == "spiffs" && espFs.checkHeader(headers)) {
            espFs.getFwFile(espFs.getLastFwPath())
        } else if (headers["x-esp32-mode"] == "sketch" && espFw.checkHeader(headers)) {
            espFw.getFwFile(espFw.getLastFwPath())
        } else throw HeaderNotCorrectExeption("x-ESP32-mode not correct or not enough required headers")
}
