package com.mercadopago.android.px.internal.features.business_result

import androidx.annotation.ColorRes
import com.mercadopago.android.px.internal.base.MvpView
import com.mercadopago.android.px.internal.features.payment_result.presentation.PaymentResultFooter
import com.mercadopago.android.px.internal.view.PaymentResultBody
import com.mercadopago.android.px.model.ExitAction

internal interface BusinessPaymentResult {

    interface View : MvpView {
        fun configureViews(model: BusinessPaymentResultViewModel, bodyListener: PaymentResultBody.Listener,
            footerListener: PaymentResultFooter.Listener)
        fun processCustomExit()
        fun processCustomExit(action: ExitAction)
        fun setStatusBarColor(@ColorRes color: Int)
        fun launchDeepLink(deepLink: String)
        fun processCrossSellingBusinessAction(deepLink: String)
        fun updateAutoReturnLabel(label: String)
    }

    interface Presenter {
        fun onFreshStart()
        fun onAbort()
        fun onStart()
        fun onStop()
    }
}
