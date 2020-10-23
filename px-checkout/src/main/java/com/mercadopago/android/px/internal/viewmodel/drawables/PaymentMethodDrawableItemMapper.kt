package com.mercadopago.android.px.internal.viewmodel.drawables

import com.meli.android.carddrawer.configuration.CardDrawerStyle
import com.mercadopago.android.px.internal.extensions.runIfNull
import com.mercadopago.android.px.internal.features.generic_modal.ActionType
import com.mercadopago.android.px.internal.features.generic_modal.FromModalToGenericDialogItem
import com.mercadopago.android.px.internal.features.generic_modal.GenericDialogItem
import com.mercadopago.android.px.internal.repository.ChargeRepository
import com.mercadopago.android.px.internal.repository.DisabledPaymentMethodRepository
import com.mercadopago.android.px.internal.repository.InitRepository
import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.internal.viewmodel.mappers.CardUiMapper
import com.mercadopago.android.px.internal.viewmodel.mappers.NonNullMapper
import com.mercadopago.android.px.model.AccountMoneyDisplayInfo
import com.mercadopago.android.px.model.CustomSearchItem
import com.mercadopago.android.px.model.ExpressMetadata
import com.mercadopago.android.px.model.internal.InitResponse
import com.mercadopago.android.px.model.one_tap.CheckoutBehaviour
import kotlinx.coroutines.runBlocking
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem.Parameters as Parameters

internal class PaymentMethodDrawableItemMapper(
    private val chargeRepository: ChargeRepository,
    private val initRepository: InitRepository,
    private val disabledPaymentMethodRepository: DisabledPaymentMethodRepository,
    private val cardUiMapper: CardUiMapper
) : NonNullMapper<ExpressMetadata, DrawableFragmentItem?>() {

    private var initResponse: InitResponse? = null

    override fun map(expressMetadata: ExpressMetadata): DrawableFragmentItem? {
        initResponse.runIfNull {
            //Acá asumimos que está cacheada la respuesta de init como en absolutamente todos lados salvo checkoutActivity
            runBlocking {
                initResponse = initRepository.loadInitResponse()
            }
        }
        return initResponse?.let { initResponse ->
            val genericDialogItem = expressMetadata.getBehaviour(CheckoutBehaviour.Type.TAP_CARD)?.modal?.let { modal ->
                initResponse.modals[modal]?.let {
                    FromModalToGenericDialogItem(ActionType.DISMISS, modal).map(it)
                }
            }
            val parameters = getParameters(expressMetadata, initResponse.customSearchItems, genericDialogItem)
            with(expressMetadata) {
                return when {
                    isCard -> SavedCardDrawableFragmentItem(parameters, paymentMethodId,
                        cardUiMapper.map(card.displayInfo))
                    isAccountMoney -> getAccountMoneyFragmentItem(parameters, accountMoney.displayInfo)
                    isConsumerCredits -> ConsumerCreditsDrawableFragmentItem(parameters, consumerCredits)
                    isNewCard || isOfflineMethods -> OtherPaymentMethodFragmentItem(parameters, newCard, offlineMethods)
                    else -> null
                }
            }
        }
    }

    private fun getAccountMoneyFragmentItem(parameters: Parameters, displayInfo: AccountMoneyDisplayInfo) =
        displayInfo.takeIf { it.type != null }?.let {
            AccountMoneyDrawableFragmentItem(parameters, cardUiMapper.map(it))
        } ?: AccountMoneyDrawableFragmentItem(parameters, CardDrawerStyle.ACCOUNT_MONEY_DEFAULT)

    private fun getParameters(
        expressMetadata: ExpressMetadata,
        customSearchItems: List<CustomSearchItem>,
        genericDialogItem: GenericDialogItem?
    ): Parameters {
        val charge = chargeRepository.getChargeRule(expressMetadata.paymentTypeId)
        val customOptionId = expressMetadata.customOptionId
        val (description, issuerName) = customSearchItems.firstOrNull { c -> c.id == customOptionId }?.let {
            Pair(it.description.orEmpty(), it.issuer?.name.orEmpty())
        } ?: Pair(TextUtil.EMPTY, TextUtil.EMPTY)

        return Parameters(
            customOptionId, expressMetadata.status, expressMetadata.displayInfo?.bottomDescription, charge?.message,
            expressMetadata.benefits?.reimbursement,
            disabledPaymentMethodRepository.getDisabledPaymentMethod(customOptionId),
            description, issuerName, genericDialogItem)
    }
}
