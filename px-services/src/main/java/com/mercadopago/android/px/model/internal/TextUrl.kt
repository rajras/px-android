package com.mercadopago.android.px.model.internal

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.util.KParcelable

data class TextUrl(
        val container: Text,
        val url: String
): KParcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Text::class.java.classLoader)!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(container, flags)
        parcel.writeString(url)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TextUrl> {
        override fun createFromParcel(parcel: Parcel) = TextUrl(parcel)
        override fun newArray(size: Int) = arrayOfNulls<TextUrl?>(size)
    }
}
