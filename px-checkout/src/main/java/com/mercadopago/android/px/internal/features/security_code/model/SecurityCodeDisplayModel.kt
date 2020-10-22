package com.mercadopago.android.px.internal.features.security_code.model

import com.mercadopago.android.px.internal.viewmodel.CardUiConfiguration
import com.mercadopago.android.px.internal.viewmodel.LazyString

internal data class SecurityCodeDisplayModel(
    val title: LazyString,
    val message: LazyString,
    val securityCodeLength: Int,
    val cardUiConfiguration: CardUiConfiguration? = null
)