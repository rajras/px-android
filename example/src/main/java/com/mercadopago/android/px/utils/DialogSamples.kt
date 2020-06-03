package com.mercadopago.android.px.utils

import android.content.Context
import android.os.Parcel
import com.mercadopago.SampleDialog
import com.mercadopago.android.px.core.DynamicDialogCreator

object DialogSamples {
    fun getDialog() = object : DynamicDialogCreator {
        override fun shouldShowDialog(context: Context, checkoutData: DynamicDialogCreator.CheckoutData) = true
        override fun create(context: Context, checkoutData: DynamicDialogCreator.CheckoutData) = SampleDialog()
        override fun describeContents() = 0
        override fun writeToParcel(parcel: Parcel, i: Int) = Unit
    }
}