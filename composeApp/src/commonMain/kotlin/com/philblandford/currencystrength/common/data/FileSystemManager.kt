package com.philblandford.currencystrength.common.data


import currencystrengthcmm.composeapp.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use
import org.jetbrains.compose.resources.ExperimentalResourceApi

interface FileSystemConfig {
    val fileSystem: FileSystem
    val appDir: String
}

class FileSystemManager(private val fileSystemConfig: FileSystemConfig) {
    private val fileSystem = fileSystemConfig.fileSystem

    @OptIn(ExperimentalResourceApi::class)
    suspend fun getResourceFileBytes(pathString: String): Result<ByteArray> {
        return withContext(Dispatchers.IO) {
            runCatching {
                Res.readBytes(pathString)
            }
        }
    }

    suspend fun saveFileBytes(pathString: String, bytes: ByteArray): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val fullPath = fileSystemConfig.appDir + "/" + pathString
                fileSystem.sink(fullPath.toPath()).buffer().use { sink ->
                    sink.write(bytes)
                    sink.flush()
                }
                fullPath
            }
        }
    }

    suspend fun getResourceFileAsUrl(pathString: String): Result<String> {
        return withContext(Dispatchers.IO) {

            getResourceFileBytes(pathString).map { bytes ->
                val fullPath = fileSystemConfig.appDir + "/" + pathString
                fileSystem.createDirectories(fullPath.toPath().parent ?: "".toPath())
                fileSystem.sink(fullPath.toPath()).buffer().use { sink ->
                    sink.write(bytes)
                    sink.flush()
                    "file://$fullPath"
                }
            }
        }
    }

    suspend fun getFileAsUrl(pathString: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val fullPath = fileSystemConfig.appDir + "/" + pathString
                fileSystem.metadata(fullPath.toPath()) // Ensure the file exists
                "file://$fullPath"
            }
        }
    }

    suspend fun getFileBytes(pathString: String): Result<ByteArray> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val fullPath = fileSystemConfig.appDir + "/" + pathString
                fileSystem.source(fullPath.toPath()).use { source ->
                    source.buffer().readByteArray()
                }
            }
        }
    }
}