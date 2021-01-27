package com.mercadopago.android.px.securitycode

import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.CallbackTest
import com.mercadopago.android.px.TestContextProvider
import com.mercadopago.android.px.addons.ESCManagerBehaviour
import com.mercadopago.android.px.any
import com.mercadopago.android.px.argumentCaptor
import com.mercadopago.android.px.internal.base.use_case.TokenizeParams
import com.mercadopago.android.px.internal.base.use_case.TokenizeUseCase
import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.internal.callbacks.TaggedCallback
import com.mercadopago.android.px.internal.di.Session
import com.mercadopago.android.px.internal.repository.CardTokenRepository
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import com.mercadopago.android.px.model.*
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TokenizeUseCaseTest : BasicRobolectricTest() {

    @Mock
    private lateinit var cardTokenRepository: CardTokenRepository

    @Mock
    private lateinit var escManagerBehaviour: ESCManagerBehaviour

    @Mock
    private lateinit var settingRepository: PaymentSettingRepository

    @Mock
    private lateinit var success: CallbackTest<Token>

    @Mock
    private lateinit var failure: CallbackTest<MercadoPagoError>

    @Mock
    private lateinit var mpCallCreateToken: MPCall<Token>
    private lateinit var tokenizeUseCase: TokenizeUseCase
    private lateinit var contextProvider: TestContextProvider

    @Before
    fun setUp() {
        initMocks(this)
        Session.initialize(getContext())
        contextProvider = TestContextProvider()
        tokenizeUseCase = TokenizeUseCase(
            cardTokenRepository,
            escManagerBehaviour,
            settingRepository,
            contextProvider)
    }

    @Test
    fun whenIsSecurityCodeAndSuccess() {
        val securityCode = "123"
        val cardMock = mock(Card::class.java)
        val params = TokenizeParams(securityCode, cardMock)
        val tokenMock = mock(Token::class.java)
        val captor = argumentCaptor<TaggedCallback<Token>>()

        `when`(cardMock.paymentMethod).thenReturn(mock(PaymentMethod::class.java))
        `when`(cardTokenRepository.createToken(any(SavedCardToken::class.java))).thenReturn(mpCallCreateToken)

        tokenizeUseCase.execute(params, success::invoke, failure::invoke)

        verify(mpCallCreateToken).enqueue(captor.capture())
        captor.value.onSuccess(tokenMock)
        verify(settingRepository).configure(tokenMock)
        verify(success).invoke(tokenMock)
        verifyZeroInteractions(failure)
    }

    @Test
    fun whenIsSecurityCodeAndFail() {
        val securityCode = "123"
        val cardMock = mock(Card::class.java)
        val params = TokenizeParams(securityCode, cardMock)

        tokenizeUseCase.execute(params, success::invoke, failure::invoke)

        verify(failure).invoke(any(MercadoPagoError::class.java))
        verifyZeroInteractions(success)
    }

    @Test
    fun whenIsPaymentRecoveryAndSuccess() {
        val cvv = "123"
        val paymentRecovery = mock(PaymentRecovery::class.java)
        val securityCode = mock(SecurityCode::class.java)
        val cardMock = mock(Card::class.java)
        val params = TokenizeParams(cvv, cardMock, paymentRecovery)
        val tokenResultMock = mock(Token::class.java)
        val captor = argumentCaptor<TaggedCallback<Token>>()

        `when`(paymentRecovery.card).thenReturn(cardMock)
        `when`(paymentRecovery.token).thenReturn(null)
        `when`(paymentRecovery.paymentMethod).thenReturn(mock(PaymentMethod::class.java))
        `when`(cardMock.id).thenReturn("456")
        `when`(cardMock.securityCode).thenReturn(securityCode)
        `when`(securityCode.length).thenReturn(cvv.length)
        `when`(cardTokenRepository.createToken(any())).thenReturn(mpCallCreateToken)
        `when`(escManagerBehaviour.isESCEnabled).thenReturn(true)

        tokenizeUseCase.execute(params, success::invoke, failure::invoke)

        verify(mpCallCreateToken).enqueue(captor.capture())
        captor.value.onSuccess(tokenResultMock)
        verify(settingRepository).configure(tokenResultMock)
        verify(success).invoke(tokenResultMock)
        verifyZeroInteractions(failure)
    }

    @Test
    fun whenIsPaymentRecoveryAndFail() {
        val cvv = "123"
        val paymentRecovery = mock(PaymentRecovery::class.java)
        val cardMock = mock(Card::class.java)
        val params = TokenizeParams(cvv, cardMock, paymentRecovery)

        `when`(paymentRecovery.card).thenReturn(null)
        `when`(paymentRecovery.token).thenReturn(null)
        `when`(paymentRecovery.paymentMethod).thenReturn(null)
        tokenizeUseCase.execute(params, success::invoke, failure::invoke)

        verify(failure).invoke(any(MercadoPagoError::class.java))
        verifyZeroInteractions(success)
    }
}
