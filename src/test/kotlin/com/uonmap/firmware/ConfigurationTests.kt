package com.uonmap.firmware

import com.uonmap.firmware.config.EspConfig
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.io.File
import java.nio.file.*

@SpringBootTest(classes = [FrwUpdateApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT)
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigurationTests (
    private val log: KLogger = KotlinLogging.logger {}
) {
    @Autowired
    lateinit var espConfig: EspConfig

    @BeforeAll
    fun setUp() {
        createTestFilesFs()
        createTestFilesFw()
    }
    @AfterAll
    fun tearDown() {
        val storageTest: String = espConfig.storagePathFs
            .subSequence(0, espConfig.storagePathFs.indexOf(File.separator))
            .toString()
        try {
            File(storageTest).deleteRecursively()
            log.info { "Successfully delete storage test: $storageTest" }
        } catch (ex:Exception) {
            log.error { "Fail delete storage test: $storageTest" }
            ex.stackTrace
        }
    }
    private fun createTestFilesFs() {
        val nameTemplate = espConfig.storagePathFs + File.separator + espConfig.prefFs
        try {
            Files.createDirectories(Paths.get(espConfig.storagePathFs))
            File(nameTemplate + "0_1_6.bin").writeBytes(byteArrayOf(0x1E, 0x48))
            File(nameTemplate + "0_2_6.bin").writeBytes(byteArrayOf(0x2E, 0x58))
            File(nameTemplate + "1_5_7.bin").writeBytes(byteArrayOf(0x3E, 0x68))
            File(espConfig.storagePathFs + File.separator + "other_1_5_7.bin")
                .writeBytes(byteArrayOf(0x7E, 0x48))
            log.info { "Successfully create test files for Fs ESP32" }
        } catch (ex:Exception) {
            log.error { "Fail create test files for Fs ESP32" }
            ex.stackTrace
        }

    }
    private fun createTestFilesFw() {
        Files.createDirectories(Paths.get(espConfig.storagePathFs))
        val nameTemplate = espConfig.storagePathFw + File.separator + espConfig.prefFw
        try {
            File(nameTemplate + "0_1_12.bin").writeBytes(byteArrayOf(0x1A, 0x4C))
            File(nameTemplate + "0_1_28.bin").writeBytes(byteArrayOf(0x2A, 0x5C))
            File(nameTemplate + "3_0_28.bin").writeBytes(byteArrayOf(0x3A, 0x6C))
            File(espConfig.storagePathFw + File.separator + "other_1_7_7.bin")
                .writeBytes(byteArrayOf(0x7A, 0x4C))
            log.info { "Successfully create test files for Fw ESP32" }
        } catch (ex:Exception) {
            log.error { "Fail create test files for Fw ESP32" }
            ex.stackTrace
        }
    }
}