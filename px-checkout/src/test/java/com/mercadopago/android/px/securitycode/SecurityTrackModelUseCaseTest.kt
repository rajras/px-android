package com.mercadopago.android.px.securitycode

import android.os.Build
import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.CallbackTest
import com.mercadopago.android.px.TestContextProvider
import com.mercadopago.android.px.internal.features.security_code.domain.use_case.SecurityTrackModelUseCase
import com.mercadopago.android.px.internal.features.security_code.tracking.SecurityCodeTracker
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.tracking.internal.model.Reason
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.mercadopago.android.px.any

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class SecurityTrackModelUseCaseTest : BasicRobolectricTest() {

    @Mock
    private lateinit var success: CallbackTest<SecurityCodeTracker>

    @Mock
    private lateinit var failure: CallbackTest<MercadoPagoError>
    private lateinit var securityTrackModelUseCase: SecurityTrackModelUseCase

    @Before
    fun setUp() {
        initMocks(this)
        val contextProvider = TestContextProvider()

        securityTrackModelUseCase = SecurityTrackModelUseCase(contextProvider)
    }

    @Test
    fun whenGetTrackData() {
        val cardParamsMock = mock(SecurityTrackModelUseCase.CardTrackParams::class.java)
        val trackingParams = SecurityTrackModelUseCase.SecurityTrackModelParams(
            cardParamsMock,
            Reason.INVALID_ESC
        )
        securityTrackModelUseCase.execute(
            trackingParams,
            success::invoke,
            failure::invoke
        )

        verify(success).invoke(any())
        verifyZeroInteractions(failure)
    }
}