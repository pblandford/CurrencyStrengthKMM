package com.philblandford.currencystrength.common.audio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import currencystrengthcmm.composeapp.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AudioPlayerAndroid(private val context:Context) : AudioPlayer {
    private val exoplayer = ExoPlayer.Builder(context).build()

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun play(name: String) {
        val uri = Res.getUri("$name.mp3")

        CoroutineScope(Dispatchers.Main).launch {
            exoplayer.setMediaItem(MediaItem.fromUri(uri))
            exoplayer.prepare()
            exoplayer.play()
        }
    }
}