package com.mercadopago.android.px.feature.custom_initialize

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import com.mercadopago.android.px.addons.FakeLocaleBehaviourImpl
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.di.preference.InitializationDataPreferences
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.util.TextUtil

internal class CustomInitializationViewModel(private val preferences: InitializationDataPreferences) : BaseViewModel() {

    private val initializationData = preferences.getInitializationData()

    val stateUILiveData = MutableLiveData<CustomInitializeState>()

    override fun recoverFromBundle(bundle: Bundle) {
        super.recoverFromBundle(bundle)
        initializationData.updateModel(
            bundle.getString(EXTRA_LOCALE)!!,
            bundle.getString(EXTRA_PUBLIC_KEY)!!,
            bundle.getString(EXTRA_PREFERENCE_ID)!!,
            bundle.getString(EXTRA_ACCESS_TOKEN)!!
        )
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.putString(EXTRA_LOCALE, initializationData.locale.value)
        bundle.putString(EXTRA_PUBLIC_KEY, initializationData.publicKey.value)
        bundle.putString(EXTRA_PREFERENCE_ID, initializationData.preferenceId.value)
        bundle.putString(EXTRA_ACCESS_TOKEN, initializationData.accessToken.value)
    }

    fun initialize() {
        updateView()
    }

    fun onInputChanged(data: InitializationDataType) {
        initializationData.updateModel(data)
    }

    private fun updateView() {
        stateUILiveData.value = CustomInitializeState.LoadData(initializationData)
    }

    fun onClear() {
        initializationData.updateModel(TextUtil.EMPTY, TextUtil.EMPTY, TextUtil.EMPTY, TextUtil.EMPTY)
        updateView()
    }

    fun onStartButtonClicked() {
        preferences.saveInitializationData(initializationData)
        initializationData.locale.value.takeIf { it.isNotEmpty() }?.let { FakeLocaleBehaviourImpl.localeTag = it }
        val builder = MercadoPagoCheckout.Builder(
            initializationData.publicKey.value.replace(TAGGED_INPUT_REGEX, TextUtil.EMPTY),
            initializationData.preferenceId.value.replace(TAGGED_INPUT_REGEX, TextUtil.EMPTY))
            .setPrivateKey(initializationData.accessToken.value.replace(TAGGED_INPUT_REGEX, TextUtil.EMPTY))
        stateUILiveData.value = CustomInitializeState.InitCheckout(builder)
    }

    companion object {
        val TAGGED_INPUT_REGEX = Regex("\\[(.*?)\\] *")
        const val EXTRA_LOCALE = "locale"
        const val EXTRA_PUBLIC_KEY = "public_key"
        const val EXTRA_PREFERENCE_ID = "preference_id"
        const val EXTRA_ACCESS_TOKEN = "access_token"
    }
}