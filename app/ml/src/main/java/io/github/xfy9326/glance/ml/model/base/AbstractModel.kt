package io.github.xfy9326.glance.ml.model.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext

abstract class AbstractModel {
    private val initMutex = Mutex()
    private var initSuccess: Boolean? = null

    protected abstract fun onInitModel(): Boolean

    abstract fun isInitialized(): Boolean

    protected suspend fun internalInit(): Boolean {
        return initSuccess ?: initMutex.withLock {
            initSuccess ?: if (isInitialized()) {
                initSuccess = true
                true
            } else {
                onInitModel().also {
                    initSuccess = it
                }
            }
        }
    }

    internal suspend fun init() = with(CoroutineScope(coroutineContext)) {
        launch {
            internalInit()
        }
    }
}