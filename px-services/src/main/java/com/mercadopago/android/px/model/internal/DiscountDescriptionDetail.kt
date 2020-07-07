package com.mercadopago.android.px.model.internal

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.util.KParcelable

data class DiscountDescriptionDetail(
        val title: Text,
        val subtitle: Text?,
        val badge: TextUrl?,
        val summary: Text,
        val description: Text,
        val legalTerms: TextUrl
): KParcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Text::class.java.classLoader)!!,
            parcel.readParcelable(Text::class.java.classLoader),
            parcel.readParcelable(TextUrl::class.java.classLoader),
            parcel.readParcelable(Text::class.java.classLoader)!!,
            parcel.readParcelable(Text::class.java.classLoader)!!,
            parcel.readParcelable(TextUrl::class.java.classLoader)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(title, flags)
        parcel.writeParcelable(subtitle, flags)
        parcel.writeParcelable(badge, flags)
        parcel.writeParcelable(summary, flags)
        parcel.writeParcelable(description, flags)
        parcel.writeParcelable(legalTerms, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<DiscountDescriptionDetail> {
        override fun createFromParcel(parcel: Parcel) = DiscountDescriptionDetail(parcel)
        override fun newArray(size: Int) = arrayOfNulls<DiscountDescriptionDetail?>(size)
    }
}