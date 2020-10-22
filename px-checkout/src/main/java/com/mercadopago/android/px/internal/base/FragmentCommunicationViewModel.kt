package com.mercadopago.android.px.internal.base

import com.mercadopago.android.px.internal.livedata.MutableSingleLiveData
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction

class FragmentCommunicationViewModel : BaseViewModel() {

    val postPaymentActionLiveData = MutableSingleLiveData<PostPaymentAction>()
}