package com.mercadopago.android.px.model.carddrawer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDrawerSwitch(
    val states: CardDrawerSwitchStates,
    val default: String,
    val backgroundColor: String,
    val options: List<CardDrawerSwitchOption>
): Parcelable
