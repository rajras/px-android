package com.mercadopago.android.px.internal.features.express.offline_methods

import com.mercadopago.android.px.internal.util.TextUtil
import com.mercadopago.android.px.model.OfflinePaymentMethod
import com.mercadopago.android.px.model.OfflinePaymentType
import java.util.*

internal object FromOfflinePaymentTypesMetadataToOfflineItems {
    @JvmStatic
    fun map(offlinePaymentTypes: List<OfflinePaymentType>): List<OfflineMethodItem> {
        val offlineMethodItems: MutableList<OfflineMethodItem> = ArrayList()
        for (offlinePaymentType in offlinePaymentTypes) {
            offlineMethodItems.add(OfflineMethodItem(offlinePaymentType.name))
            for (offlinePaymentMethod in offlinePaymentType.paymentMethods) {
                if (offlinePaymentMethod.status.isEnabled) {
                    offlineMethodItems.add(
                        OfflineMethodItem(offlinePaymentMethod.name, offlinePaymentMethod.id,
                            offlinePaymentMethod.instructionId, offlinePaymentMethod.description,
                            getResourceName(offlinePaymentType, offlinePaymentMethod),
                            offlinePaymentMethod.isAdditionalInfoNeeded))
                }
            }
        }
        return offlineMethodItems
    }

    private fun getResourceName(paymentType: OfflinePaymentType, paymentMethod: OfflinePaymentMethod): String {
        return paymentMethod.id +
            if (paymentMethod.instructionId != paymentType.id) {
                TextUtil.UNDERSCORE.toString() + paymentMethod.instructionId
            } else {
                TextUtil.EMPTY
            }
    }
}