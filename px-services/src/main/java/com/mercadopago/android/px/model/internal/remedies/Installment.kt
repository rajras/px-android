package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

data class Installment(val installments: Int, val totalAmount: BigDecimal): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), BigDecimal(parcel.readString()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(installments)
        parcel.writeString(totalAmount.toString())
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Installment> {
        override fun createFromParcel(parcel: Parcel) = Installment(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Installment>(size)
    }
}