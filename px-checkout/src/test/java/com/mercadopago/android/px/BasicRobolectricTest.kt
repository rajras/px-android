package com.mercadopago.android.px

import android.content.Context
import org.robolectric.RuntimeEnvironment

open class BasicRobolectricTest {
    protected fun getContext(): Context = RuntimeEnvironment.application
}