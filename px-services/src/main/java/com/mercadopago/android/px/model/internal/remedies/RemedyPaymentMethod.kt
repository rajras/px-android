package com.mercadopago.android.px.model.internal.remedies

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

data class RemedyPaymentMethod(
    val customOptionId: String,
    val installments: Int?,
    val issuerName: String?,
    val lastFourDigit: String?,
    val paymentMethodId: String,
    val paymentTypeId: String,
    val securityCodeLength: Int?,
    val securityCodeLocation: String?,
    val totalAmount: BigDecimal?,
    val installmentsList: List<Installment>?,
    val escStatus: String?,
    val esc: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            if (!parcel.readString().isNullOrEmpty()) BigDecimal(parcel.readString()) else null,
            parcel.createTypedArrayList(Installment),
            parcel.readString()!!,
            parcel.readByte() == 1.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(customOptionId)
        parcel.writeValue(installments)
        parcel.writeString(issuerName)
        parcel.writeString(lastFourDigit)
        parcel.writeString(paymentMethodId)
        parcel.writeString(paymentTypeId)
        parcel.writeValue(securityCodeLength)
        parcel.writeString(securityCodeLocation)
        parcel.writeString(totalAmount?.toString())
        parcel.writeTypedList(installmentsList)
        parcel.writeString(escStatus)
        parcel.writeByte(if (esc) 1 else 0)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<RemedyPaymentMethod> {
        override fun createFromParcel(parcel: Parcel) = RemedyPaymentMethod(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RemedyPaymentMethod?>(size)
    }
}