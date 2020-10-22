package com.mercadopago.android.px.internal.features.security_code.mapper

import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessSecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.model.SecurityCodeDisplayModel
import com.mercadopago.android.px.internal.viewmodel.mappers.CardUiMapper

internal class SecurityCodeDisplayModelMapper(private val cardUiMapper: CardUiMapper) {

    fun map(model: BusinessSecurityCodeDisplayData) = model.run {
        SecurityCodeDisplayModel(
            title,
            message,
            securityCodeLength,
            cardDisplayInfo?.let { cardUiMapper.map(it) }
        )
    }
}