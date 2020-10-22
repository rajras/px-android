package com.mercadopago.android.px.internal.features.security_code.domain.use_case

import com.meli.android.carddrawer.configuration.SecurityCodeLocation
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.base.use_case.UseCase
import com.mercadopago.android.px.internal.callbacks.Response
import com.mercadopago.android.px.internal.callbacks.map
import com.mercadopago.android.px.internal.extensions.notNull
import com.mercadopago.android.px.internal.features.security_code.data.SecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.domain.model.BusinessSecurityCodeDisplayData
import com.mercadopago.android.px.internal.features.security_code.mapper.BusinessSecurityCodeDisplayDataMapper
import com.mercadopago.android.px.internal.repository.InitRepository
import com.mercadopago.android.px.internal.viewmodel.LazyString
import com.mercadopago.android.px.model.CvvInfo

internal class DisplayDataUseCase(
    private val initRepository: InitRepository,
    private val securityCodeDisplayDataMapper: BusinessSecurityCodeDisplayDataMapper,
    override val contextProvider: CoroutineContextProvider = CoroutineContextProvider()
) : UseCase<DisplayDataUseCase.CardParams, BusinessSecurityCodeDisplayData>() {

    override suspend fun doExecute(param: CardParams) = run {
        val securityCodeLength = param.securityCodeLength ?: 0
        param.cvvInfo?.let {
            Response.Success(SecurityCodeDisplayData(LazyString(it.title), LazyString(it.message), securityCodeLength))
        } ?: notNull(initRepository.loadInitResponse()).let { initResponse ->
            val cardDisplayInfo = initResponse.express.takeIf { it.isNotEmpty() }?.find { data ->
                data.isCard && data.card.id == param.id
            }?.card?.displayInfo

            val title = LazyString(R.string.px_security_code_screen_title)
            val message = LazyString(if (param.securityCodeLocation == SecurityCodeLocation.FRONT) {
                R.string.px_security_code_subtitle_front
            } else {
                R.string.px_security_code_subtitle_back
            }, securityCodeLength.toString())

            Response.Success(SecurityCodeDisplayData(title, message, securityCodeLength, cardDisplayInfo))
        }
    }.map { securityCodeDisplayDataMapper.map(it) }

    data class CardParams(
        val id: String?,
        val cvvInfo: CvvInfo?,
        val securityCodeLength: Int?,
        val securityCodeLocation: String
    )
}