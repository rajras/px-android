package com.mercadopago.android.px.securitycode

import com.mercadopago.android.px.internal.features.security_code.data.SecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessCardDisplayInfo
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessSecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.mapper.BusinessSecurityCodeDisplayDataMapper
import com.mercadopago.android.px.internal.viewmodel.LazyString
import com.mercadopago.android.px.model.CardDisplayInfo
import com.mercadopago.android.px.model.SecurityCode
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.powermock.reflect.Whitebox

class BusinessSecurityCodeDisplayDataMapperTest {
    
    @Test
    fun whenMapSecurityCodeDisplayDataToBusinessSecurityCodeDisplayData() {
        val securityCodeDisplayData = mock(SecurityCodeDisplayData::class.java)
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
            Whitebox.setInternalState(this, "issuerId", 1234L)
            Whitebox.setInternalState(this, "issuerImage", "issuerImage")
            Whitebox.setInternalState(this, "issuerImageUrl", "issuerImageUrl")
            Whitebox.setInternalState(this, "lastFourDigits", "7890")
            Whitebox.setInternalState(this, "securityCode", securityCode)
        }

        `when`(cardDisplayInfo.getCardPattern()).thenReturn("*****")
        `when`(securityCodeDisplayData.title).thenReturn(mock(LazyString::class.java))
        `when`(securityCodeDisplayData.message).thenReturn(mock(LazyString::class.java))
        `when`(securityCodeDisplayData.securityCodeLength).thenReturn(3)
        `when`(securityCodeDisplayData.cardDisplayInfo).thenReturn(cardDisplayInfo)
        `when`(securityCode.cardLocation).thenReturn("back")
        `when`(securityCode.length).thenReturn(3)

        val businessCardDisplayInfo = with(cardDisplayInfo) {
            BusinessCardDisplayInfo(
                cardholderName,
                expiration,
                color,
                fontColor,
                issuerId,
                cardPattern,
                getCardPattern(),
                securityCode.cardLocation,
                securityCode.length,
                lastFourDigits,
                paymentMethodImage,
                issuerImage,
                fontType,
                paymentMethodImageUrl,
                issuerImageUrl
            )
        }
        
        
        val expectedResult = BusinessSecurityCodeDisplayData(
            securityCodeDisplayData.title,
            securityCodeDisplayData.message,
            securityCodeDisplayData.securityCodeLength,
            businessCardDisplayInfo
        )

        val mapper = BusinessSecurityCodeDisplayDataMapper()
        val actualResult = mapper.map(securityCodeDisplayData)
        
        assertTrue(ReflectionEquals(actualResult).matches(expectedResult))
    }
}
