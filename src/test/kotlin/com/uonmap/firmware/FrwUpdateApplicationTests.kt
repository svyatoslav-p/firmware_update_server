package com.uonmap.firmware

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
		headers.add("x-esp32-mode" , "spiffs")
		headers.add("x-esp32-sta-mac" , "24:6F:28:DA:7E:B0")
		headers.add("x-esp32-version" , "0.0.25")
		mockMvc.perform(
			get("/api/v1/esp/update")
				.headers(headers))
			.andExpect(status().isOk)
			.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"spiffs_1_5_7.bin\""))
			.andExpect(header().string("x-MD5", "72cd641de7f01fb47cad2462475a6c5a"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	fun `Get file frw update for ESP client`() {
		val headers = HttpHeaders()
		headers.add("x-esp32-mode" , "sketch")
		headers.add("x-esp32-sta-mac" , "24:6F:28:DA:7E:B0")
		headers.add("x-esp32-version" , "0.0.25")
		mockMvc.perform(
			get("/api/v1/esp/update")
				.headers(headers))
			.andExpect(status().isOk)
			.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"firmware_3_0_28.bin\""))
			.andExpect(header().string("x-MD5", "25105d25daccbd225e23b197e8960430"))
			.andDo(MockMvcResultHandlers.print());
	}
}
