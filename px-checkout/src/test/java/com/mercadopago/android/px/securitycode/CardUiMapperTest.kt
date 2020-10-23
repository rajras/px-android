package com.mercadopago.android.px.securitycode

import android.os.Build
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessCardDisplayInfo
import com.mercadopago.android.px.internal.viewmodel.CardUiConfiguration
import com.mercadopago.android.px.internal.viewmodel.mappers.CardUiMapper
import com.mercadopago.android.px.model.CardDisplayInfo
import com.mercadopago.android.px.model.SecurityCode
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.powermock.reflect.Whitebox
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class CardUiMapperTest {

    @Test
    fun whenMapBusinessCardDisplayInfoToCardUiConfiguration() {
        val businessCardDisplayInfo = mock(BusinessCardDisplayInfo::class.java)

        `when`(businessCardDisplayInfo.paymentMethodImage).thenReturn("paymentMethodImage")
        `when`(businessCardDisplayInfo.paymentMethodImageUrl).thenReturn("paymentMethodImageUrl")
        `when`(businessCardDisplayInfo.cardPattern).thenReturn(intArrayOf(1,2,3,4))
        `when`(businessCardDisplayInfo.cardPatternMask).thenReturn("*****")
        `when`(businessCardDisplayInfo.cardholderName).thenReturn("cardholderName")
        `when`(businessCardDisplayInfo.color).thenReturn("color")
        `when`(businessCardDisplayInfo.expiration).thenReturn("expiration")
        `when`(businessCardDisplayInfo.fontColor).thenReturn("fontColor")
        `when`(businessCardDisplayInfo.fontType).thenReturn("fontType")
        `when`(businessCardDisplayInfo.issuerId).thenReturn(1234)
        `when`(businessCardDisplayInfo.issuerImage).thenReturn("issuerImage")
        `when`(businessCardDisplayInfo.issuerImageUrl).thenReturn("issuerImageUrl")
        `when`(businessCardDisplayInfo.lastFourDigits).thenReturn("7890")
        `when`(businessCardDisplayInfo.securityCodeLocation).thenReturn("back")
        `when`(businessCardDisplayInfo.securityCodeLength).thenReturn(3)

        val expectedResult = CardUiConfiguration(
            businessCardDisplayInfo.cardholderName,
            businessCardDisplayInfo.expiration,
            businessCardDisplayInfo.cardPatternMask,
            businessCardDisplayInfo.issuerImageUrl,
            businessCardDisplayInfo.paymentMethodImageUrl,
            businessCardDisplayInfo.fontType,
            businessCardDisplayInfo.cardPattern,
            businessCardDisplayInfo.color,
            businessCardDisplayInfo.fontColor,
            businessCardDisplayInfo.securityCodeLocation,
            businessCardDisplayInfo.securityCodeLength
        )

        val actualResult = CardUiMapper.map(businessCardDisplayInfo)

        assertTrue(ReflectionEquals(actualResult).matches(expectedResult))
    }

    @Test
    fun whenMapCardDisplayInfoToCardUiConfiguration() {
        val cardDisplayInfo = mock(CardDisplayInfo::class.java)
        val securityCode = mock(SecurityCode::class.java)

        with(cardDisplayInfo) {
            Whitebox.setInternalState(this, "paymentMethodImage", "paymentMethodImage")
            Whitebox.setInternalState(this, "paymentMethodImageUrl", "paymentMethodImageUrl")
            Whitebox.setInternalState(this, "cardPattern", intArrayOf(1,2,3,4))
            Whitebox.setInternalState(this, "cardholderName", "cardholderName")
            Whitebox.setInternalState(this, "color", "color")
            Whitebox.setInternalState(this, "expiration", "expiration")
            Whitebox.setInternalState(this, "fontColor", "fontColor")
            Whitebox.setInternalState(this, "fontType", "fontType")
            Whitebox.setInternalState(this, "issuerId", 1234)
            Whitebox.setInternalState(this, "issuerImage", "issuerImage")
            Whitebox.setInternalState(this, "issuerImageUrl", "issuerImageUrl")
            Whitebox.setInternalState(this, "paymentMethodImage", "paymentMethodImage")
            Whitebox.setInternalState(this, "lastFourDigits", "7890")
            Whitebox.setInternalState(this, "securityCode", securityCode)
        }

        `when`(cardDisplayInfo.getCardPattern()).thenReturn("*****")
        `when`(securityCode.cardLocation).thenReturn("back")
        `when`(securityCode.length).thenReturn(3)

        val expectedResult = CardUiConfiguration(
            cardDisplayInfo.cardholderName,
            cardDisplayInfo.expiration,
            cardDisplayInfo.getCardPattern(),
            cardDisplayInfo.issuerImageUrl,
            cardDisplayInfo.paymentMethodImageUrl,
            cardDisplayInfo.fontType,
            cardDisplayInfo.cardPattern,
            cardDisplayInfo.color,
            cardDisplayInfo.fontColor,
            cardDisplayInfo.securityCode.cardLocation,
            cardDisplayInfo.securityCode.length,
            null,
            null
        )

        val actualResult = CardUiMapper.map(cardDisplayInfo)

        assertTrue(ReflectionEquals(actualResult).matches(expectedResult))
    }
}
