package com.mercadopago.android.px.securitycode

import android.os.Build
import com.mercadopago.android.px.internal.features.security_code.tracking.*
import com.mercadopago.android.px.tracking.internal.events.FrictionEventTracker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class SecurityCodeTrackerTest {

    private lateinit var securityCodeTracker: SecurityCodeTracker

    @Mock
    private lateinit var securityCodeViewTrackMock: SecurityCodeViewTrack

    @Mock
    private lateinit var confirmSecurityCodeTrackMock: ConfirmSecurityCodeTrack

    @Mock
    private lateinit var abortSecurityCodeTrackMock: AbortSecurityCodeTrack

    @Mock
    private lateinit var securityCodeFrictionsMock: SecurityCodeFrictions

    @Before
    fun setUp() {
        securityCodeTracker = SecurityCodeTracker(
            securityCodeViewTrackMock,
            confirmSecurityCodeTrackMock,
            abortSecurityCodeTrackMock,
            securityCodeFrictionsMock)
    }

    @Test
    fun whenSecurityCodeViewTrack() {
        securityCodeTracker.trackSecurityCode()

        verify(securityCodeViewTrackMock).track()
    }

    @Test
    fun whenConfirmSecurityCodeTrack() {
        securityCodeTracker.trackConfirmSecurityCode()

        verify(confirmSecurityCodeTrackMock).track()
    }

    @Test
    fun whenAbortSecurityCodeTrack() {
        securityCodeTracker.trackAbortSecurityCode()

        verify(abortSecurityCodeTrackMock).track()
    }

    @Test
    fun whenSecurityCodeFrictions() {
        val apiErrorFrictionMock = mock(FrictionEventTracker::class.java)
        val tokenFrictionMock = mock(FrictionEventTracker::class.java)

        `when`(securityCodeFrictionsMock.paymentApiErrorFriction()).thenReturn(apiErrorFrictionMock)
        `when`(securityCodeFrictionsMock.tokenApiErrorFriction()).thenReturn(tokenFrictionMock)
        securityCodeTracker.trackPaymentApiError()
        securityCodeTracker.trackTokenApiError()

        verify(securityCodeFrictionsMock).paymentApiErrorFriction()
        verify(securityCodeFrictionsMock).tokenApiErrorFriction()
        verify(apiErrorFrictionMock).track()
        verify(tokenFrictionMock).track()
    }

}