package com.mercadopago.android.px.model.carddrawer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDrawerSwitchOption(
    val id: String,
    val name: String
): Parcelable