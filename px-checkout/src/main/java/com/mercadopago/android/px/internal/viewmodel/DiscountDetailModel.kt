package com.mercadopago.android.px.internal.viewmodel

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator

data class DiscountDetailModel(
    val discountHeader: DiscountHeader,
    val discountBody: DiscountBody
) : KParcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readParcelable(DiscountHeader::class.java.classLoader)!!,
        parcel.readParcelable(DiscountBody::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(discountHeader, flags)
        parcel.writeParcelable(discountBody, flags)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::DiscountDetailModel)
    }
}