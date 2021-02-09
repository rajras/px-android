package com.mercadopago.android.px.internal.view

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Looper.getMainLooper
import android.view.View
import com.mercadolibre.android.mlbusinesscomponents.common.MLBusinessSingleItem
import com.mercadolibre.android.mlbusinesscomponents.components.discount.MLBusinessDiscountBoxData
import com.mercadolibre.android.mlbusinesscomponents.components.loyalty.MLBusinessLoyaltyRingData
import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.features.business_result.CongratsViewModel
import com.mercadopago.android.px.internal.features.business_result.PXDiscountBoxData
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class PaymentResultBodyTest : BasicRobolectricTest() {

    @Mock
    private lateinit var congratsViewModel: CongratsViewModel

    private lateinit var modelBuilder: PaymentResultBody.Model.Builder

    private lateinit var body: PaymentResultBody

    @Before
    fun setUp() {
        //Esto es para que de como instalado MP
        val packageManager = mock(PackageManager::class.java)
        val context = spy(getContext())
        `when`(packageManager.getApplicationInfo(anyString(), anyInt())).thenReturn(ApplicationInfo())
        `when`(context.packageManager).thenReturn(packageManager)

        body = PaymentResultBody(context)
        modelBuilder = PaymentResultBody.Model.Builder().setCongratsViewModel(congratsViewModel)
    }

    @Test
    fun initWithEmptyModel() {
        body.init(modelBuilder.build(), mock(PaymentResultBody.Listener::class.java))

        shadowOf(getMainLooper()).idle()

        assertEquals(body.findViewById<View>(R.id.loyaltyView).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.dividingLineView).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.showAllDiscounts).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.money_split_view).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.downloadView).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.receipt).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.help).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.primaryMethod).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.secondaryMethod).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.view_receipt_action).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.px_fragment_container_important).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.px_fragment_container_top).visibility, View.GONE)
        assertEquals(body.findViewById<View>(R.id.px_fragment_container_bottom).visibility, View.GONE)
    }

    @Test
    fun initWithFullModel() {
        val loyaltyRingData = mock(MLBusinessLoyaltyRingData::class.java)
        `when`(loyaltyRingData.ringHexaColor).thenReturn("#ffffff")
        val discountBoxData = mock(MLBusinessDiscountBoxData::class.java)
        val discountBoxItem = mock(MLBusinessSingleItem::class.java)
        `when`(discountBoxItem.imageUrl).thenReturn("https://i.dlpng.com/static/png/5417315-logo-oasis-png-animesubindoco-oasis-png-1024_474_preview.png")
        `when`(discountBoxData.items).thenReturn(listOf(discountBoxItem, discountBoxItem))
        val pxDiscountBoxData = mock(PXDiscountBoxData::class.java)
        `when`(pxDiscountBoxData.discountBoxData).thenReturn(discountBoxData)
        `when`(congratsViewModel.loyaltyRingData).thenReturn(loyaltyRingData)
        `when`(congratsViewModel.discountBoxData).thenReturn(pxDiscountBoxData)
        `when`(congratsViewModel.showAllDiscounts).thenReturn(PaymentCongratsResponse.Action("label", "target"))

        body.init(modelBuilder.build(), mock(PaymentResultBody.Listener::class.java))

        shadowOf(getMainLooper()).idle()

        assertEquals(body.findViewById<View>(R.id.loyaltyView).visibility, View.VISIBLE)
    }

    @Ignore
    @Test
    fun initWithTouchPointData() {
        val loyaltyRingData = mock(MLBusinessLoyaltyRingData::class.java)
        `when`(loyaltyRingData.ringHexaColor).thenReturn("#ffffff")
        val discountBoxData = mock(MLBusinessDiscountBoxData::class.java)
        val discountBoxItem = mock(MLBusinessSingleItem::class.java)
        `when`(discountBoxItem.imageUrl).thenReturn("https://i.dlpng.com/static/png/5417315-logo-oasis-png-animesubindoco-oasis-png-1024_474_preview.png")
        `when`(discountBoxData.items).thenReturn(listOf(discountBoxItem, discountBoxItem))
        val pxDiscountBoxData = mock(PXDiscountBoxData::class.java)
        `when`(pxDiscountBoxData.discountBoxData).thenReturn(discountBoxData)
        `when`(congratsViewModel.loyaltyRingData).thenReturn(loyaltyRingData)
        `when`(congratsViewModel.discountBoxData).thenReturn(pxDiscountBoxData)

        body.init(modelBuilder.build(), mock(PaymentResultBody.Listener::class.java))

        shadowOf(getMainLooper()).idle()
    }
}
