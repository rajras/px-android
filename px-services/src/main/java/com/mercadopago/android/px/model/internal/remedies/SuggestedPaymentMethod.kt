package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable

data class SuggestedPaymentMethod(
        val alternativePaymentMethod: RemedyPaymentMethod,
        val message: String,
        val title: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(RemedyPaymentMethod::class.java.classLoader)!!,
            parcel.readString()!!,
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(alternativePaymentMethod, flags)
        parcel.writeString(message)
        parcel.writeString(title)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SuggestedPaymentMethod> {
        override fun createFromParcel(parcel: Parcel) = SuggestedPaymentMethod(parcel)
        override fun newArray(size: Int) = arrayOfNulls<SuggestedPaymentMethod?>(size)
    }
}