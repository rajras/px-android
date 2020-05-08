package com.mercadopago.android.px.di.module

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
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