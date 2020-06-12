package com.mercadopago.android.px.internal.datasource.cache

import android.os.Handler
import android.os.Looper
import com.mercadopago.android.px.internal.callbacks.MPCall
import com.mercadopago.android.px.internal.core.FileManager
import com.mercadopago.android.px.internal.util.JsonUtil
import com.mercadopago.android.px.model.exceptions.ApiException
import com.mercadopago.android.px.model.internal.InitResponse
import com.mercadopago.android.px.services.Callback
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InitDiskCache(private val fileManager: FileManager) : Cache<InitResponse?> {
    private val initFile: File
    private val executorService: ExecutorService
    private val mainHandler: Handler

    init {
        initFile = fileManager.create(DEF_FILE_NAME)
        executorService = Executors.newFixedThreadPool(1)
        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun get(): MPCall<InitResponse?> {
        return MPCall { callback: Callback<InitResponse> -> executorService.execute { read(callback) } }
    }

    override fun put(initResponse: InitResponse) {
        if (!isCached) {
            executorService.execute { fileManager.writeToFile(initFile, JsonUtil.toJson(initResponse)) }
        }
    }

    override fun evict() {
        if (isCached) {
            fileManager.removeFile(initFile)
        }
    }

    override fun isCached() = fileManager.exists(initFile)

    private fun read(callback: Callback<InitResponse>) {
        if (isCached) {
            val fileContent = fileManager.readText(initFile)
            val initResponse = JsonUtil.fromJson(fileContent, InitResponse::class.java)
            mainHandler.post { initResponse?.let { callback.success(it) } ?: callback.failure(ApiException()) }
        } else {
            mainHandler.post { callback.failure(ApiException()) }
        }
    }

    companion object {
        private const val DEF_FILE_NAME = "px_init"
    }
}