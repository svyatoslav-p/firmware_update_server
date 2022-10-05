package com.uonmap.firmware

import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_MODE
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_STA_MAC
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_ESP32_VER
import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_MD5
import com.uonmap.firmware.config.RestHttpConsts.PARAM_ESP32_MODE_SKETCH
import com.uonmap.firmware.config.RestHttpConsts.PARAM_ESP32_MODE_SPIFFS
import com.uonmap.firmware.config.RestHttpConsts.URI_ESP_UPDATE_FULL
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FrwUpdateApplicationTests @Autowired constructor(
	val mockMvc: MockMvc
): ConfigurationTests(){

	@Test
	fun `Get file fs update for ESP client`() {
		val headers = HttpHeaders()
		headers.add(HEAD_X_ESP32_MODE , PARAM_ESP32_MODE_SPIFFS)
		headers.add(HEAD_X_ESP32_STA_MAC , "24:6F:28:DA:7E:B0")
		headers.add(HEAD_X_ESP32_VER , "0.0.25")
		mockMvc.perform(
			get(URI_ESP_UPDATE_FULL)
				.headers(headers))
			.andExpect(status().isOk)
			.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"spiffs_1_5_7.bin\""))
			.andExpect(header().string(HEAD_X_MD5, "72cd641de7f01fb47cad2462475a6c5a"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	fun `Get file frw update for ESP client`() {
		val headers = HttpHeaders()
		headers.add(HEAD_X_ESP32_MODE , PARAM_ESP32_MODE_SKETCH)
		headers.add(HEAD_X_ESP32_STA_MAC , "24:6F:28:DA:7E:B0")
		headers.add(HEAD_X_ESP32_VER , "0.0.25")
		mockMvc.perform(
			get(URI_ESP_UPDATE_FULL)
				.headers(headers))
			.andExpect(status().isOk)
			.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"firmware_3_0_28.bin\""))
			.andExpect(header().string(HEAD_X_MD5, "25105d25daccbd225e23b197e8960430"))
			.andDo(MockMvcResultHandlers.print());
	}
}
