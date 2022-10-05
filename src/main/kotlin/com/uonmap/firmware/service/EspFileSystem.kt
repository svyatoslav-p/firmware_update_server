package com.uonmap.firmware.service

import com.uonmap.firmware.config.EspConfig
import com.uonmap.firmware.config.RestHttpConsts
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_MODE
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_STA_MAC
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_VER
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Service
class EspFileSystem(
    val property: EspConfig,
    override val storagePath: String = property.storagePathFs,
    override val regexFilter: Regex = Regex(
        storagePath + File.separator + "${property.prefFs}(\\d+)_(\\d+)_(\\d+).bin"),
    override val httpHeadersList: List<String> = listOf(
        HEAD_X_ESP32_STA_MAC,
        HEAD_X_ESP32_VER,
        HEAD_X_ESP32_MODE),
    ): Firmware() {
    init {
        Files.createDirectories(Paths.get(storagePath))
    }
}