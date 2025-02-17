package com.philblandford.currencystrength.common.data

import android.content.Context
import okio.FileSystem

class FileSystemConfigAndroid(private val context: Context) : FileSystemConfig {
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM
    override val appDir: String
        get() = context.filesDir.absolutePath
}