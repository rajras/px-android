package com.mercadopago.android.px.model.carddrawer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDrawerSwitchStates(
    val checkedState: CardDrawerSwitchState,
    val uncheckedState: CardDrawerSwitchState,
    val disabledState: CardDrawerSwitchState
): Parcelable