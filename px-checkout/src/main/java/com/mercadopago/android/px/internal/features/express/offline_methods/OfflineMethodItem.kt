package com.mercadopago.android.px.internal.features.express.offline_methods

import com.mercadopago.android.px.model.internal.Text

internal class OfflineMethodItem {
    val name: Text
    val paymentMethodId: String?
    val paymentTypeId: String?
    val description: Text?
    val isAdditionalInfoNeeded: Boolean
    val isOfflinePaymentTypeItem: Boolean
        get() = description == null && iconResourceName == null
    var iconResourceName: String?

    constructor(name: Text) {
        this.name = name
        this.paymentMethodId = null
        this.paymentTypeId = null
        this.description = null
        this.iconResourceName = null
        isAdditionalInfoNeeded = false
    }

    constructor(name: Text, paymentMethodId: String,
        paymentTypeId: String, description: Text, iconResourceName: String,
        additionalInfoNeeded: Boolean) {
        this.name = name
        this.paymentMethodId = paymentMethodId
        this.paymentTypeId = paymentTypeId
        this.description = description
        this.iconResourceName = iconResourceName
        isAdditionalInfoNeeded = additionalInfoNeeded
    }
}