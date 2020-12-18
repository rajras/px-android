package com.mercadopago.android.px.internal.core.extensions

import android.view.View

internal fun CharSequence?.isNotNullNorEmpty() = !isNullOrEmpty()

internal fun <T : CharSequence>T?.orIfEmpty(fallback: T) = if (isNotNullNorEmpty()) this!! else fallback

internal fun View.gone() = apply { visibility = View.GONE }

internal fun View.visible() = apply { visibility = View.VISIBLE }

internal fun View.invisible() = apply { visibility = View.INVISIBLE }
