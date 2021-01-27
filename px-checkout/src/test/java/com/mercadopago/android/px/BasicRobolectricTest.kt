package com.mercadopago.android.px

import android.content.Context
import androidx.test.core.app.ApplicationProvider

open class BasicRobolectricTest {
    protected fun getContext(): Context = ApplicationProvider.getApplicationContext()
}
