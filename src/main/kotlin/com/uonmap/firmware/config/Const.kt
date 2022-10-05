package com.uonmap.firmware.config

import com.uonmap.firmware.http.ApiError
import com.uonmap.firmware.http.GenericResponse
import org.springframework.http.HttpStatus

object RestHttpConsts {
    /*HTTP пути*/
    private const val URI_API = "/api"
    private const val URI_V1 = "/v1"
    private const val URI_ESP = "/esp"
    const val URI_UPDATE = "/update"
    const val URI_FIRMWARE = "/firmware"
    const val URI_ESP_FULL = URI_API + URI_V1 + URI_ESP
    const val URI_ESP_UPDATE_FULL = URI_API + URI_V1 + URI_ESP + URI_UPDATE

    /*HTTP заголовки*/
    const val HEAD_X_MD5 = "x-MD5"
    const val HEAD_X_ESP32_MODE = "x-esp32-mode"
    const val HEAD_X_ESP32_STA_MAC = "x-esp32-sta-mac"
    const val HEAD_X_ESP32_VER = "x-esp32-version"

    /*HTTP параметры*/
    const val PARAM_ESP32_MODE_SPIFFS = "spiffs"
    const val PARAM_ESP32_MODE_SKETCH = "sketch"
}