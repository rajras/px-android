package com.mercadopago.android.px.internal.features.express.slider

import androidx.fragment.app.Fragment
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.viewmodel.drawables.AccountMoneyDrawableFragmentItem

internal class AccountMoneyLowResFragment : AccountMoneyFragment() {

    override fun getLayout() = R.layout.px_fragment_account_money_low_res

    companion object {
        @JvmStatic fun getInstance(model: AccountMoneyDrawableFragmentItem): Fragment {
            return AccountMoneyLowResFragment().also {
                it.storeModel(model)
            }
        }
    }
}
