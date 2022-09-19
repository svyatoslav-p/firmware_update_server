package com.uonmap.firmware.service

import com.uonmap.firmware.config.EspConfig
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Service
class EspFileSystem(
    val property: EspConfig,
    override val storagePath: String = property.storagePathFs,
    override val regexFilter: Regex = Regex(
        storagePath + File.separator +"${property.prefFs}(\\d+)_(\\d+)_(\\d+).bin"),
    override val httpHeadersList: List<String> = listOf(
        "x-esp32-sta-mac",
        "x-esp32-version",
        "x-esp32-mode"),
    ): Firmware() {
    init {
        Files.createDirectories(Paths.get(storagePath))
    }
}