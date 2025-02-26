package com.philblandford.currencystrength.common.audio

import com.philblandford.currencystrength.common.log.log
import currencystrengthcmm.composeapp.generated.resources.Res
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create


class AudioPlayerIos : AudioPlayer {
    private var player: AVAudioPlayer? = null

    @OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
    override suspend fun play(uri: String) {
        val bytes = Res.readBytes("$uri.mp3")
        val data = byteArrayToNSData(bytes)
        log("Playing audio $bytes $data")
        player = AVAudioPlayer(data = data, error = null)
        player?.play()
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun byteArrayToNSData(byteArray: ByteArray): NSData {
        return byteArray.usePinned {
            NSData.create(
                bytes = it.addressOf(0),
                length = byteArray.size.convert()
            )
        }
    }
}