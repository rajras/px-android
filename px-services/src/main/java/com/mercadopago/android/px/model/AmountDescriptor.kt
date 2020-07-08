package com.mercadopago.android.px.model

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.model.internal.Text

data class AmountDescriptor(val descriptions: List<Text>,
                            val amount: Text,
                            val brief: List<Text>?,
                            val iconUrl: String?): KParcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(Text.CREATOR)!!,
            parcel.readParcelable(Text::class.java.classLoader)!!,
            parcel.createTypedArrayList(Text.CREATOR),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(descriptions)
        parcel.writeParcelable(amount, flags)
        parcel.writeList(brief)
        parcel.writeString(iconUrl)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<AmountDescriptor> {
        override fun createFromParcel(parcel: Parcel) = AmountDescriptor(parcel)
        override fun newArray(size: Int) = arrayOfNulls<AmountDescriptor?>(size)
    }
}