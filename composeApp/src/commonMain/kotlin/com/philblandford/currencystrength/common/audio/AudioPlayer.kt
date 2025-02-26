package com.philblandford.currencystrength.common.audio

interface AudioPlayer {
    suspend fun play(uri:String)
}