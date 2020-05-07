package com.mercadopago.android.px.internal.util

import com.mercadolibre.android.cardform.internal.CardFormWithFragment.Builder.Companion.withAccessToken
import com.mercadopago.android.px.internal.core.FlowIdProvider
import com.mercadopago.android.px.internal.core.SessionIdProvider
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository
import java.util.*

class CardFormWithFragmentWrapper(
    settingRepository: PaymentSettingRepository,
    sessionIdProvider: SessionIdProvider,
    flowIdProvider: FlowIdProvider) {

    private val flowId = flowIdProvider.flowId
    private val privateKey = settingRepository.privateKey!!
    private val siteId = settingRepository.site.id
    private val sessionId = sessionIdProvider.sessionId
    private val excludedPaymentTypes = settingRepository.checkoutPreference?.excludedPaymentTypes
        ?: Collections.emptyList()

    fun getCardFormWithFragment() = withAccessToken(privateKey, siteId, flowId)
        .setSessionId(sessionId)
        .setExcludedTypes(excludedPaymentTypes).build()
}