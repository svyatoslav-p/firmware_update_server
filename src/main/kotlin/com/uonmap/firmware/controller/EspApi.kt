package com.uonmap.firmware.controller

import com.uonmap.firmware.exeption.*
import com.uonmap.firmware.service.EspFileSystem
import com.uonmap.firmware.service.EspFirmware
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
