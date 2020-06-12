package com.mercadopago

import android.content.Context
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.mercadopago.android.px.core.SplitPaymentProcessor
import com.mercadopago.android.px.model.BusinessPayment
import com.mercadopago.android.px.model.IPayment
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.internal.IParcelablePaymentDescriptor
import com.mercadopago.android.px.preferences.CheckoutPreference

open class SamplePaymentProcessor() : SplitPaymentProcessor {
    val visualProcessor = false
    private var genericPayment: IParcelablePaymentDescriptor? = null
    private var businessPayment: BusinessPayment? = null
    private val handler = Handler()

    constructor(payment: IPayment) : this() {
        genericPayment = IParcelablePaymentDescriptor.with(payment)
    }

    constructor(payment: IPaymentDescriptor) : this() {
        when(payment) {
            is IParcelablePaymentDescriptor -> genericPayment = payment
            is BusinessPayment -> businessPayment = payment
        }
    }

    protected constructor(parcel: Parcel) : this() {
        genericPayment = parcel.readParcelable(IParcelablePaymentDescriptor::class.java.classLoader)
        businessPayment = parcel.readParcelable(BusinessPayment::class.java.classLoader)
    }

    override fun startPayment(context: Context, data: SplitPaymentProcessor.CheckoutData,
        paymentListener: SplitPaymentProcessor.OnPaymentListener) {
        handler.postDelayed({ paymentListener.onPaymentFinished(getPayment()) }, LOADING_TIME.toLong())
    }

    override fun getPaymentTimeout(checkoutPreference: CheckoutPreference) = TIMEOUT

    override fun shouldShowFragmentOnPayment(checkoutPreference: CheckoutPreference) = visualProcessor

    override fun supportsSplitPayment(checkoutPreference: CheckoutPreference?) = true

    override fun getFragment(data: SplitPaymentProcessor.CheckoutData, context: Context): Fragment? {
        return SamplePaymentProcessorFragment.with(getPayment() as Parcelable)
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(genericPayment, flags)
        parcel.writeParcelable(businessPayment, flags)
    }

    protected open fun getPayment(): IPaymentDescriptor {
        return if (genericPayment != null) {
            genericPayment as IPaymentDescriptor
        } else {
            businessPayment as IPaymentDescriptor
        }
    }

    companion object {
        private const val TIMEOUT = 20000
        private const val LOADING_TIME = 2000
        @JvmField val CREATOR = object : Parcelable.Creator<SamplePaymentProcessor> {
            override fun createFromParcel(parcel: Parcel) = SamplePaymentProcessor(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SamplePaymentProcessor>(size)
        }
    }
}