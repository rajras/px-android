package com.mercadopago.android.px.utils

import android.content.Context
import android.os.Parcel
import android.support.v4.app.DialogFragment
import com.mercadopago.SampleDialog
import com.mercadopago.android.px.core.DynamicDialogCreator

object DialogSamples {
    fun getDialog() = object : DynamicDialogCreator {
        override fun shouldShowDialog(context: Context,
            checkoutData: DynamicDialogCreator.CheckoutData): Boolean {
            return true
        }

        override fun create(context: Context,
            checkoutData: DynamicDialogCreator.CheckoutData): DialogFragment {
            return SampleDialog()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {}
    }
}