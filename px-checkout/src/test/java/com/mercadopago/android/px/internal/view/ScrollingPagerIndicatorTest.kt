package com.mercadopago.android.px.internal.view

import android.graphics.Canvas
import android.graphics.Color
import com.mercadopago.android.px.BasicRobolectricTest
import com.mercadopago.android.px.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class ScrollingPagerIndicatorTest : BasicRobolectricTest() {

    @Test
    fun initWithParametersAndSetsCurrentPosition() {
        val blackColor = "#000000"
        val whiteColor = "#ffffff"
        val visibleDotCount = 5
        val attr = Robolectric.buildAttributeSet()
            .addAttribute(R.attr.px_spi_dotColor, blackColor)
            .addAttribute(R.attr.px_spi_dotSelectedColor, whiteColor)
            .addAttribute(R.attr.px_spi_visibleDotCount, visibleDotCount.toString())
            .build()

        val indicator = ScrollingPagerIndicator(getContext(), attr)
        indicator.setDotCount(7)
        indicator.onPageScrolled(1, 0.5f)
        indicator.setCurrentPosition(2)
        indicator.onMeasure(0, 0)
        indicator.onDraw(mock(Canvas::class.java))

        assertEquals(ReflectionHelpers.getField<Int>(indicator, "dotColor"), Color.parseColor(blackColor))
        assertEquals(ReflectionHelpers.getField<Int>(indicator, "selectedDotColor"), Color.parseColor(whiteColor))
        assertEquals(ReflectionHelpers.getField<Int>(indicator, "visibleDotCount"), visibleDotCount)
    }
}
