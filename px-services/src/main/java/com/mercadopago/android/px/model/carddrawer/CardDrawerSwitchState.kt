package com.mercadopago.android.px.model.carddrawer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDrawerSwitchState(
    val backgroundColor: String,
    val textColor: String,
    val weight: String
): Parcelable