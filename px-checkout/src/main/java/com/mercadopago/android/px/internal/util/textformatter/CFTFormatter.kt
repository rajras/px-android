package com.mercadopago.android.px.internal.util.textformatter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import com.mercadopago.android.px.internal.font.PxFont
import com.mercadopago.android.px.internal.util.PayerCostHelper
import com.mercadopago.android.px.internal.util.RateType
import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.internal.util.ViewUtils
import java.util.regex.Pattern


internal class CFTFormatter(
        private val spannableStringBuilder: SpannableStringBuilder,
        private val context: Context,
        private val rates: Map<String, String>?) : ChainFormatter() {

    private var textColor = 0
    private lateinit var rateType: RateType

    fun withTextColor(color: Int): CFTFormatter {
        textColor = color
        return this
    }

    fun withRate(type: RateType): CFTFormatter {
        rateType = type
        return this
    }

    fun build(): Spannable {
        return apply(PayerCostHelper.getRatePercent(rates, rateType))
    }

    override fun apply(amount: CharSequence?): Spannable {
        if (TextUtil.isEmpty(amount) || !REGEX_NON_ZERO_PATTERN.matcher(amount).matches()) {
            return spannableStringBuilder
        }
        val initialIndex = spannableStringBuilder.length
        val cftDescription = TextUtil.format(context, rateType.getResource(), amount)
        val separator = " "
        spannableStringBuilder.append(separator).append(cftDescription)
        val textLength = separator.length + cftDescription.length
        ViewUtils.setColorInSpannable(textColor, initialIndex, initialIndex + textLength, spannableStringBuilder)
        ViewUtils.setFontInSpannable(context, PxFont.REGULAR, spannableStringBuilder, initialIndex,
                initialIndex + textLength)
        return spannableStringBuilder
    }

    companion object {
        private val REGEX_NON_ZERO_PATTERN = Pattern.compile(".*[1-9]+.*$")
    }
}