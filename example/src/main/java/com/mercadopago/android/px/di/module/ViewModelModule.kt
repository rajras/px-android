package com.mercadopago.android.px.di.module

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mercadopago.android.px.di.ViewModelFactory

internal class ViewModelModule() {
    private val factory = ViewModelFactory()
    fun <T : ViewModel?> get(fragment: Fragment, modelClass: Class<T>): T {
        return ViewModelProviders.of(fragment, factory).get(modelClass)
    }

    fun <T : ViewModel?> get(activity: FragmentActivity, modelClass: Class<T>): T {
        return ViewModelProviders.of(activity, factory).get(modelClass)
    }
}