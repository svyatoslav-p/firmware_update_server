package com.uonmap.firmware

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.uonmap.firmware.config")
class FrwUpdateApplication

fun main(args: Array<String>) {
	runApplication<FrwUpdateApplication>(*args)
}
