package com.uonmap.firmware.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application.esp")
data class EspProp(
    var storagePathFw: String = "storage/esp/firmware",
    var storagePathFs: String = "storage/esp/file_system",
    var prefFw: String = "frw_",
    var prefFs: String = "fs_",
){}
