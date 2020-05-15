package com.mercadopago

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.mercadopago.android.px.addons.BehaviourProvider

internal abstract class ExampleBaseActivity: AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        newBase?.let {super.attachBaseContext(BehaviourProvider.getLocaleBehaviour().attachBaseContext(it)) }
    }
}