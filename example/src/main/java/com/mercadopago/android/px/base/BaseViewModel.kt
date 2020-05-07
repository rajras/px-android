package com.mercadopago.android.px.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle

abstract class BaseViewModel : ViewModel() {
    open fun recoverFromBundle(bundle: Bundle) = Unit
    open fun storeInBundle(bundle: Bundle) = Unit
}