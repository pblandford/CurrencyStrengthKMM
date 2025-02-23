package com.philblandford.currencystrength.common.network

import com.philblandford.currencystrength.common.model.Currency
import com.philblandford.currencystrength.common.model.CurrencyPair
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object KMMDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.toString()
        encoder.encodeString(string) // Uses ISO-8601 format by default
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string) // Parses ISO-8601
    }
}


object CurrencyPairSerializer : KSerializer<CurrencyPair> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CurrencyPair", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CurrencyPair) {
        val string = "${value.base}/${value.counter}"
        encoder.encodeString(string) // Uses ISO-8601 format by default
    }

    override fun deserialize(decoder: Decoder): CurrencyPair {
        val string = decoder.decodeString()
        val fields = string.split("/")
        return CurrencyPair(  Currency.valueOf(fields[0]), Currency.valueOf(fields[1]))
    }
}