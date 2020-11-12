package com.mercadopago.android.px.internal.viewmodel.mappers

import com.mercadopago.android.px.internal.extensions.isNotNull
import java.util.*

internal abstract class NonNullMapper<T, V> : Mapper<T, V>() {

    override fun map(values: Iterable<T>): List<V> {
        val returned: MutableList<V> = ArrayList()
        for (value in values) {
            val mappedValue = map(value)
            if (mappedValue.isNotNull()) {
                returned.add(mappedValue)
            }
        }
        return returned
    }
}
