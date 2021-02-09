package com.mercadopago.android.px.internal.mappers

import com.meli.android.carddrawer.configuration.CardDrawerStyle
import com.meli.android.carddrawer.configuration.FontType
import com.meli.android.carddrawer.configuration.SecurityCodeLocation
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessCardDisplayInfo
import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.internal.viewmodel.CardUiConfiguration
import com.mercadopago.android.px.model.AccountMoneyDisplayInfo
import com.mercadopago.android.px.model.CardDisplayInfo

internal object CardUiMapper {

    fun map(cardDisplayInfo: BusinessCardDisplayInfo): CardUiConfiguration {
        with(cardDisplayInfo) {
            return CardUiConfiguration(
                cardholderName,
                expiration,
                cardPatternMask,
                issuerImageUrl,
                paymentMethodImageUrl,
                fontType,
                cardPattern,
                color,
                fontColor,
                securityCodeLocation,
                securityCodeLength,
                null,
                null
            )
        }
    }

    fun map(cardDisplayInfo: CardDisplayInfo): CardUiConfiguration {
        with(cardDisplayInfo) {
            return CardUiConfiguration(
                cardholderName,
                expiration,
                getCardPattern(),
                issuerImageUrl,
                paymentMethodImageUrl,
                fontType,
                cardPattern,
                color,
                fontColor,
                securityCode.cardLocation,
                securityCode.length,
                null,
                null
            )
        }
    }

    fun map(accountMoneyDisplayInfo: AccountMoneyDisplayInfo): CardUiConfiguration {
        with(accountMoneyDisplayInfo) {
            return CardUiConfiguration(
                TextUtil.EMPTY,
                TextUtil.EMPTY,
                TextUtil.EMPTY,
                null,
                paymentMethodImageUrl,
                FontType.NONE,
                intArrayOf(),
                color,
                null,
                SecurityCodeLocation.NONE,
                0,
                gradientColors,
                if (type == AccountMoneyDisplayInfo.Type.HYBRID) {
                    CardDrawerStyle.ACCOUNT_MONEY_HYBRID
                } else {
                    CardDrawerStyle.ACCOUNT_MONEY_DEFAULT
                }
            )
        }
    }
}
