package com.mercadopago

import android.os.Parcel
import android.os.Parcelable
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.utils.PaymentUtils

open class SampleRemediesPaymentProcessor : SamplePaymentProcessor {

    private var showError = false

    constructor()

    constructor(parcel: Parcel) : super(parcel)

    override fun getPayment(): IPaymentDescriptor {
        showError = !showError
        return if (showError) PaymentUtils.getGenericPaymentRejected() else PaymentUtils.getGenericPaymentApproved()
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<SampleRemediesPaymentProcessor> {
            override fun createFromParcel(parcel: Parcel) = SampleRemediesPaymentProcessor(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SampleRemediesPaymentProcessor>(size)
        }
    }
}