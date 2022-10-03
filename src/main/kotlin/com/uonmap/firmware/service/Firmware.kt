package com.uonmap.firmware.service

import com.uonmap.firmware.config.RestHttpConsts.HEAD_X_MD5
import com.uonmap.firmware.exeption.FileNotFoundExeption
import mu.KLogger
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.nio.file.Paths
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.DigestUtils
import java.io.FileInputStream
import java.nio.file.Files

abstract class Firmware(
    private val logger: KLogger = KotlinLogging.logger {}
) {
    abstract val storagePath: String
    abstract val regexFilter: Regex
    abstract val httpHeadersList: List<String>

    fun checkHeader(headersRequest: Map<String, String>): Boolean {
        httpHeadersList.forEach { if (!headersRequest.containsKey(it)) return false }
        return true
    }

    fun getListAllPath(): Array<String> {
        var listFwPath = emptyArray<String>()
        Files.walk(Paths.get(storagePath))
            .filter { Files.isRegularFile(it) }
            .filter { regexFilter.matches(it.toString()) }
            .forEach { listFwPath += it.toString() }
        return if (listFwPath.isNotEmpty()) listFwPath
            else throw FileNotFoundExeption("Files not found by path <$storagePath> matching the filter")
    }

    fun getLastFwPath(): String {
        val listFwPath = getListAllPath()
        var listFwVer = emptyArray<Int>()
        listFwPath.forEach {
            val match = regexFilter.find(it)
            listFwVer += match?.destructured?.toList()?.joinToString(separator = "")?.toInt() ?: 0
        }
        return listFwPath[listFwVer.indexOf(listFwVer.maxOrNull())]
    }

    fun getFwFile(
        filePath: String
    ): ResponseEntity<Resource> {
        val file: Resource = loadFile(filePath)
        val md5sum = DigestUtils.md5DigestAsHex(FileInputStream(filePath))
        logger.info { "Download file: $filePath MD5: $md5sum" }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"")
                .header(HEAD_X_MD5, md5sum )
                .body(file)}

    private fun loadFile(
        filePath: String
    ): Resource {
        val resource = UrlResource(Paths.get(filePath).toUri())
        if (resource.exists() || resource.isReadable) return resource
        else throw RuntimeException("Error file load: $filePath")
    }
}