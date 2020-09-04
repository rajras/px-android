package com.mercadopago.android.px.internal.viewmodel

import android.os.Parcel
import com.mercadopago.android.px.internal.util.KParcelable
import com.mercadopago.android.px.internal.util.parcelableCreator
import com.mercadopago.android.px.model.TextUrl
import com.mercadopago.android.px.model.internal.Text

class DiscountBody(val summary: Text, val description: Text, val legalTerms: TextUrl) : KParcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readParcelable(Text::class.java.classLoader)!!,
        parcel.readParcelable(Text::class.java.classLoader)!!,
        parcel.readParcelable(TextUrl::class.java.classLoader)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(summary, flags)
        parcel.writeParcelable(description, flags)
        parcel.writeParcelable(legalTerms, flags)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::DiscountBody)
    }
}