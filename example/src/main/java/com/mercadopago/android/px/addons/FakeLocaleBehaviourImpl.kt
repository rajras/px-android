package com.mercadopago.android.px.addons

import android.content.Context
import java.util.*

object FakeLocaleBehaviourImpl : LocaleBehaviour {
    var localeTag = "en-US"
    override fun attachBaseContext(context: Context): Context {
        val (language, country) = localeTag.split("-")
        return LocaleContextWrapper.wrap(context, Locale(language, country))
    }
}