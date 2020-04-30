package com.mercadopago.android.px.tracking.internal.events

object SessionFrictionEventTracker :
    FrictionEventTracker("${EventTracker.BASE_PATH}/session", Id.GENERIC, Style.NON_SCREEN)