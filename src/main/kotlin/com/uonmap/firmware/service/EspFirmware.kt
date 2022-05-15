package com.uonmap.firmware.service

import com.uonmap.firmware.config.EspProp
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class EspFirmware(
    val property: EspProp,
    override val storagePath: String = property.storagePathFw,
    override val regexFilter: Regex = "${storagePath}/${property.prefFw}(\\d+)_(\\d+)_(\\d+).bin"
        .toRegex(),
    override val httpHeadersList: List<String> = listOf(
        "x-esp32-sta-mac",
        "x-esp32-ap-mac",
        "x-esp32-free-space",
        "x-esp32-mode"),
    ): Firmware() {
    init {
        Files.createDirectories(Paths.get(storagePath))
    }
}