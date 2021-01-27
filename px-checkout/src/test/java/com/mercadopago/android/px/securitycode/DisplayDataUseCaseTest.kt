package com.mercadopago.android.px.securitycode

import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.CallbackTest
import com.mercadopago.android.px.TestContextProvider
import com.mercadopago.android.px.any
import com.mercadopago.android.px.argumentCaptor
import com.mercadopago.android.px.internal.di.Session
import com.mercadopago.android.px.internal.features.security_code.data.SecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessSecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.domain.use_case.DisplayDataUseCase
import com.mercadopago.android.px.internal.features.security_code.mapper.BusinessSecurityCodeDisplayDataMapper
import com.mercadopago.android.px.internal.repository.InitRepository
import com.mercadopago.android.px.internal.util.JsonUtil
import com.mercadopago.android.px.internal.viewmodel.LazyString
import com.mercadopago.android.px.model.CvvInfo
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.InitResponse
import com.mercadopago.android.px.utils.ResourcesUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DisplayDataUseCaseTest : BasicRobolectricTest() {

    @Mock
    private lateinit var initRepository: InitRepository

    @Mock
    private lateinit var success: CallbackTest<BusinessSecurityCodeDisplayData>

    @Mock
    private lateinit var failure: CallbackTest<MercadoPagoError>

    private lateinit var displayDataUseCase: DisplayDataUseCase
    private val securityCodeDisplayDataMapper = BusinessSecurityCodeDisplayDataMapper()

    @Before
    fun setUp() {
        initMocks(this)
        Session.initialize(getContext())
        val contextProvider = TestContextProvider()

        displayDataUseCase = DisplayDataUseCase(
            initRepository,
            securityCodeDisplayDataMapper,
            contextProvider
        )
    }

    @Test
    fun whenIsVirtualCard() {
        val cardParams = mock(DisplayDataUseCase.CardParams::class.java)
        val cvvInfo = mock(CvvInfo::class.java)
        val resultBusinessCaptor = argumentCaptor<BusinessSecurityCodeDisplayData>()
        `when`(cvvInfo.title).thenReturn("title")
        `when`(cvvInfo.message).thenReturn("message")
        `when`(cardParams.cvvInfo).thenReturn(cvvInfo)
        `when`(cardParams.id).thenReturn("123")
        `when`(cardParams.securityCodeLength).thenReturn(3)
        `when`(cardParams.securityCodeLocation).thenReturn("front")
        val expectedResult = BusinessSecurityCodeDisplayData(
            LazyString(cvvInfo.title),
            LazyString(cvvInfo.message),
            cardParams.securityCodeLength!!,
            null
        )

        displayDataUseCase.execute(
            cardParams,
            success::invoke,
            failure::invoke
        )

        verify(success).invoke(resultBusinessCaptor.capture())
        verifyZeroInteractions(failure)
        with(resultBusinessCaptor.value) {
            assertTrue(ReflectionEquals(title).matches(expectedResult.title))
            assertTrue(ReflectionEquals(message).matches(expectedResult.message))
            assertTrue(ReflectionEquals(securityCodeLength).matches(expectedResult.securityCodeLength))
            assertTrue(ReflectionEquals(cardDisplayInfo).matches(expectedResult.cardDisplayInfo))
        }
    }

    @Test
    fun whenIsCardWithOneTap() = runBlocking {
        val cardParams = mock(DisplayDataUseCase.CardParams::class.java)
        val resultBusinessCaptor = argumentCaptor<BusinessSecurityCodeDisplayData>()
        val cardId = "268434496"
        val initResponse = loadInitResponseWithOneTap()
        val displayInfo = initResponse.express.find { it.isCard && it.card.id == cardId }?.card?.displayInfo
        `when`(cardParams.id).thenReturn(cardId)
        `when`(cardParams.securityCodeLength).thenReturn(3)
        `when`(cardParams.securityCodeLocation).thenReturn("back")
        `when`(initRepository.loadInitResponse()).thenReturn(initResponse)
        val expectedResult = SecurityCodeDisplayData(
            LazyString(0),
            LazyString(0, cardParams.securityCodeLength.toString()),
            cardParams.securityCodeLength!!,
            displayInfo).let {
            BusinessSecurityCodeDisplayDataMapper().map(it)
        }

        displayDataUseCase.execute(
            cardParams,
            success::invoke,
            failure::invoke
        )

        verify(success).invoke(resultBusinessCaptor.capture())
        verifyZeroInteractions(failure)
        with(resultBusinessCaptor.value) {
            assertTrue(ReflectionEquals(title, "resId").matches(expectedResult.title))
            assertTrue(ReflectionEquals(message, "resId").matches(expectedResult.message))
            assertTrue(ReflectionEquals(securityCodeLength).matches(expectedResult.securityCodeLength))
            assertTrue(ReflectionEquals(cardDisplayInfo).matches(expectedResult.cardDisplayInfo))
        }
    }

    @Test
    fun whenIsCardWithGroups() = runBlocking {
        val cardParams = mock(DisplayDataUseCase.CardParams::class.java)
        val resultBusinessCaptor = argumentCaptor<BusinessSecurityCodeDisplayData>()
        val cardId = "268434496"
        val initResponse = loadInitResponseWithGroup()
        `when`(cardParams.id).thenReturn(cardId)
        `when`(cardParams.securityCodeLength).thenReturn(3)
        `when`(cardParams.securityCodeLocation).thenReturn("back")
        `when`(initRepository.loadInitResponse()).thenReturn(initResponse)
        val expectedResult = BusinessSecurityCodeDisplayData(
            LazyString(0),
            LazyString(0, cardParams.securityCodeLength.toString()),
            cardParams.securityCodeLength!!
        )

        displayDataUseCase.execute(
            cardParams,
            success::invoke,
            failure::invoke
        )

        verify(success).invoke(resultBusinessCaptor.capture())
        verifyZeroInteractions(failure)
        with(resultBusinessCaptor.value) {
            assertTrue(ReflectionEquals(title, "resId").matches(expectedResult.title))
            assertTrue(ReflectionEquals(message, "resId").matches(expectedResult.message))
            assertTrue(ReflectionEquals(securityCodeLength).matches(expectedResult.securityCodeLength))
            assertTrue(ReflectionEquals(cardDisplayInfo).matches(expectedResult.cardDisplayInfo))
        }
    }

    @Test
    fun whenUseCaseFail() = runBlocking {
        val cardParams = mock(DisplayDataUseCase.CardParams::class.java)

        `when`(cardParams.id).thenReturn("")
        `when`(cardParams.securityCodeLength).thenReturn(0)
        `when`(cardParams.securityCodeLocation).thenReturn("")
        `when`(initRepository.loadInitResponse()).thenReturn(null)

        displayDataUseCase.execute(
            cardParams,
            success::invoke,
            failure::invoke
        )

        verify(failure).invoke(any())
        verifyZeroInteractions(success)
    }

    private fun loadInitResponseWithOneTap() = JsonUtil
        .fromJson(
            ResourcesUtil.getStringResource("init_response_one_tap.json"),
            InitResponse::class.java
        )

    private fun loadInitResponseWithGroup() = JsonUtil
        .fromJson(
            ResourcesUtil.getStringResource("init_response_group.json"),
            InitResponse::class.java
        )
}
