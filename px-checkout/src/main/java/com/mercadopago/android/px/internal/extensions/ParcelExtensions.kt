package com.mercadopago.android.px.internal.extensions

import android.os.Parcel
import java.math.BigDecimal
import java.util.*

internal fun Parcel.writeDate(date: Date?) = writeLong(date?.time ?: -1L)

internal fun Parcel.readDate(): Date? {
    val long = readLong()
    return if (long != -1L) Date(long) else null
}

internal fun Parcel.writeBigDecimal(value: BigDecimal?) = value?.let {
    writeBool(true)
    writeString(it.toString())
} ?: writeBool(false)

internal fun Parcel.readBigDecimal() = if (readBool()) BigDecimal(readString()) else null

internal fun Parcel.writeOptionalInt(value: Int?) = value?.let {
    writeBool(true)
    writeInt(it)
} ?: writeBool(false)

internal fun Parcel.readOptionalInt() = if (readBool()) readInt() else null

internal fun Parcel.readBool() = readByte() != 0.toByte()

internal fun Parcel.writeBool(value: Boolean) = writeByte(if (value) 1 else 0)