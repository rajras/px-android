package com.mercadopago.android.px.internal.mappers

internal abstract class CacheableMapper<T, V, K> : Mapper<T, V>() {

    override fun map(values: Iterable<T>): List<V> {
        val cache = mutableMapOf<K, V>()
        val returned = mutableListOf<V>()

        for (value in values) {
            returned.add(mapWithCache(cache, value))
        }

        return returned
    }

    private fun mapWithCache(cache: MutableMap<K, V>, value: T) : V {
        val key = getKey(value)
        return if (cache.containsKey(key)) {
            cache[key]!!
        } else {
            map(value).also {
                cache[key] = it
            }
        }
    }

    protected abstract fun getKey(value: T) : K
}
