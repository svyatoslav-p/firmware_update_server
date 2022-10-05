package com.uonmap.firmware.http.controller

import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_MODE
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_STA_MAC
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_VER
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_MD5
import com.uonmap.firmware.config.RestHttpConsts.HTTP_200_STRING
import com.uonmap.firmware.config.RestHttpConsts.HTTP_400_STRING
import com.uonmap.firmware.config.RestHttpConsts.HTTP_404_STRING
import com.uonmap.firmware.config.RestHttpConsts.PARAM_ESP32_MODE_SKETCH
import com.uonmap.firmware.config.RestHttpConsts.PARAM_ESP32_MODE_SPIFFS
import com.uonmap.firmware.config.RestHttpConsts.URI_ESP_FULL
import com.uonmap.firmware.config.RestHttpConsts.URI_FIRMWARE
import com.uonmap.firmware.config.RestHttpConsts.URI_UPDATE
import com.uonmap.firmware.http.ApiError
import com.uonmap.firmware.http.GenericResponse
import com.uonmap.firmware.http.exeption.HeaderNotCorrectExeption
import com.uonmap.firmware.service.EspFileSystem
import com.uonmap.firmware.service.EspFirmware
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(URI_ESP_FULL)
class EspApi(
    private val espFw: EspFirmware,
    private val espFs: EspFileSystem
) {
    @Operation(summary = "Get the update file for ESP32",
        description = "Returns the update file or an explanation why the file did not attach.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = HTTP_200_STRING, description = "Firmware file or body with an error",
                headers= [
                    (Header(name=HEAD_X_MD5, description ="MD5 sum of found file"))],
                content = [
                    (Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = (
                            ArraySchema(schema = Schema(implementation = GenericResponse::class)))))])]
    )
    @Parameters(
        value = [
            Parameter(required = true, name = HEAD_X_ESP32_MODE, example = PARAM_ESP32_MODE_SPIFFS,
                description = "Type file response spiffs or sketch", `in` = ParameterIn.HEADER),
            Parameter(required = true, name = HEAD_X_ESP32_STA_MAC, example = "24:6F:28:DA:7E:B0",
                description = "MAC address device", `in` = ParameterIn.HEADER),
            Parameter(required = true, name = HEAD_X_ESP32_VER, example = "0.0.25",
                description = "Current version firmware or file system ESP32", `in` = ParameterIn.HEADER)
        ]
    )
    @GetMapping(URI_UPDATE)
    fun update(
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Resource> =
        if (headers[HEAD_X_ESP32_MODE] == PARAM_ESP32_MODE_SPIFFS && espFs.checkHeader(headers)) {
            espFs.getFwFile(espFs.getLastFwPath())
        } else if (headers[HEAD_X_ESP32_MODE] == PARAM_ESP32_MODE_SKETCH && espFw.checkHeader(headers)) {
            espFw.getFwFile(espFw.getLastFwPath())
        } else throw HeaderNotCorrectExeption("x-ESP32-mode not correct or not enough required headers")

    @GetMapping(URI_FIRMWARE)
    fun getFirmwareInfo(): GenericResponse {
        return GenericResponse(espFw.getListAllPath())
    }
}
