package com.mercadopago.android.px.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class CvvInfo(val title: String, val message: String?, val imageUrl: String?): Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(imageUrl)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CvvInfo> {
        override fun createFromParcel(parcel: Parcel) = CvvInfo(parcel)
        override fun newArray(size: Int) = arrayOfNulls<CvvInfo?>(size)
    }
}