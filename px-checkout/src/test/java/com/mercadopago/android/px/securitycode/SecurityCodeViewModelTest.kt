package com.mercadopago.android.px.securitycode

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.any
import com.mercadopago.android.px.argumentCaptor
import com.mercadopago.android.px.internal.base.use_case.CallBack
import com.mercadopago.android.px.internal.base.use_case.TokenizeUseCase
import com.mercadopago.android.px.internal.features.pay_button.PayButton
import com.mercadopago.android.px.internal.features.security_code.SecurityCodeViewModel
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessSecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.domain.use_case.DisplayDataUseCase
import com.mercadopago.android.px.internal.features.security_code.domain.use_case.SecurityTrackModelUseCase
import com.mercadopago.android.px.internal.features.security_code.domain.use_case.SecurityTrackModelUseCase.*
import com.mercadopago.android.px.internal.features.security_code.mapper.SecurityCodeDisplayModelMapper
import com.mercadopago.android.px.internal.features.security_code.mapper.TrackingParamModelMapper
import com.mercadopago.android.px.internal.features.security_code.model.SecurityCodeDisplayModel
import com.mercadopago.android.px.internal.features.security_code.tracking.SecurityCodeTracker
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.Token
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.Reason
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class SecurityCodeViewModelTest : BasicRobolectricTest() {

    @Mock
    private lateinit var tokenizeUseCaseTest: TokenizeUseCase

    @Mock
    private lateinit var trackModelUseCase: SecurityTrackModelUseCase

    @Mock
    private lateinit var displayDataUseCaseTest: DisplayDataUseCase

    @Mock
    private lateinit var trackingParamModelMapper: TrackingParamModelMapper

    @Mock
    private lateinit var securityCodeDisplayModelMapper: SecurityCodeDisplayModelMapper

    @Mock
    private lateinit var paymentConfiguration: PaymentConfiguration

    @Mock
    private lateinit var card: Card

    @Mock
    private lateinit var paymentRecovery: PaymentRecovery

    @Mock
    private lateinit var displayModelObserver: Observer<SecurityCodeDisplayModel>

    @Mock
    private lateinit var tokenizeErrorApiObserver: Observer<Unit>
    private lateinit var securityCodeViewModel: SecurityCodeViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        securityCodeViewModel = SecurityCodeViewModel(
            tokenizeUseCaseTest,
            displayDataUseCaseTest,
            trackModelUseCase,
            trackingParamModelMapper,
            securityCodeDisplayModelMapper
        )

        securityCodeViewModel.displayModelLiveData.observeForever(displayModelObserver)
        securityCodeViewModel.tokenizeErrorApiLiveData.observeForever(tokenizeErrorApiObserver)

        `when`(trackingParamModelMapper.map(any(), any())).thenReturn(mock(SecurityTrackModelParams::class.java))
        `when`(securityCodeDisplayModelMapper.map(any())).thenReturn(mock(SecurityCodeDisplayModel::class.java))
        `when`(card.id).thenReturn("123")
        `when`(card.getSecurityCodeLength()).thenReturn(3)
        `when`(card.getSecurityCodeLocation()).thenReturn("front")
    }

    @Test
    fun whenInitSecurityCodeViewModelWithCard() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val successDisplayDataCaptor = argumentCaptor<CallBack<BusinessSecurityCodeDisplayData>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)
        val displayBusinessDataMock = mock(BusinessSecurityCodeDisplayData::class.java)

        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            null,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)
        verify(securityCodeTrackerMock).trackSecurityCode()
        verify(displayDataUseCaseTest).execute(any(), successDisplayDataCaptor.capture(), any())
        successDisplayDataCaptor.value.invoke(displayBusinessDataMock)
        verify(displayModelObserver).onChanged(any())
        verifyZeroInteractions(tokenizeUseCaseTest)
    }

    @Test
    fun whenInitSecurityCodeViewModelWithPaymentRecovery() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val successDisplayDataCaptor = argumentCaptor<CallBack<BusinessSecurityCodeDisplayData>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)
        val displayBusinessDataMock = mock(BusinessSecurityCodeDisplayData::class.java)

        `when`(paymentRecovery.card).thenReturn(card)

        securityCodeViewModel.init(
            paymentConfiguration,
            null,
            paymentRecovery,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)
        verify(securityCodeTrackerMock).trackSecurityCode()
        verify(displayDataUseCaseTest).execute(any(), successDisplayDataCaptor.capture(), any())
        successDisplayDataCaptor.value.invoke(displayBusinessDataMock)
        verify(displayModelObserver).onChanged(any())
        verifyZeroInteractions(tokenizeUseCaseTest)
    }

    @Test
    fun whenSecurityCodeViewModelTokenizeAndSuccess() {
        val callbackMock = mock(PayButton.OnEnqueueResolvedCallback::class.java)
        val successTokenCaptor = argumentCaptor<CallBack<Token>>()
        val tokenMock = mock(Token::class.java)
        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            paymentRecovery,
            Reason.INVALID_ESC
        )
        securityCodeViewModel.enqueueOnExploding("123", callbackMock)

        verify(tokenizeUseCaseTest).execute(any(), successTokenCaptor.capture(), any())
        successTokenCaptor.value.invoke(tokenMock)
        verify(callbackMock).success()
    }

    @Test
    fun whenSecurityCodeViewModelTokenizeAndFail() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val callbackMock = mock(PayButton.OnEnqueueResolvedCallback::class.java)
        val failureTokenCaptor = argumentCaptor<CallBack<MercadoPagoError>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)
        val errorMock = mock(MercadoPagoError::class.java)

        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            paymentRecovery,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)
        securityCodeViewModel.enqueueOnExploding("123", callbackMock)

        verify(tokenizeUseCaseTest).execute(any(), any(), failureTokenCaptor.capture())
        failureTokenCaptor.value.invoke(errorMock)
        verify(tokenizeErrorApiObserver).onChanged(any())
        verify(callbackMock).failure()
    }

    @Test
    fun whenSecurityCodeViewModelAndHandlePrepayment() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)
        val callback = mock(PayButton.OnReadyForPaymentCallback::class.java)

        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            paymentRecovery,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)

        securityCodeViewModel.handlePrepayment(callback)

        verify(securityCodeTrackerMock).trackConfirmSecurityCode()
        verify(callback).call(paymentConfiguration, null)
    }

    @Test
    fun whenSecurityCodeViewModelOnBack() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)

        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            paymentRecovery,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)

        securityCodeViewModel.onBack()

        verify(securityCodeTrackerMock).trackAbortSecurityCode()
    }

    @Test
    fun whenSecurityCodeViewModelOnPaymentError() {
        val successTrackerCaptor = argumentCaptor<CallBack<SecurityCodeTracker>>()
        val securityCodeTrackerMock = mock(SecurityCodeTracker::class.java)

        securityCodeViewModel.init(
            paymentConfiguration,
            card,
            paymentRecovery,
            Reason.INVALID_ESC
        )

        verify(trackModelUseCase).execute(any(), successTrackerCaptor.capture(), any())
        successTrackerCaptor.value.invoke(securityCodeTrackerMock)

        securityCodeViewModel.onPaymentError()

        verify(securityCodeTrackerMock).trackPaymentApiError()
    }
}