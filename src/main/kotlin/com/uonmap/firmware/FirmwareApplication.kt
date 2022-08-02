package com.uonmap.firmware

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.uonmap.firmware.config")
@OpenAPIDefinition(
	info = Info(
		title = "API Firmware Update Server",
		version = "0.1",
		contact = Contact(url = "https://smartsafeschool.com", name = "UonMap", email = "info@uonmap.com"))
)
class FrwUpdateApplication

fun main(args: Array<String>) {
	runApplication<FrwUpdateApplication>(*args)
}


