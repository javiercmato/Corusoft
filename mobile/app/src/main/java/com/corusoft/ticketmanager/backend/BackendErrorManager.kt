package com.corusoft.ticketmanager.backend

import com.corusoft.ticketmanager.backend.dtos.common.GlobalError

object BackendErrorManager {
    private var globalError: GlobalError? = null

    fun getGlobalError(): GlobalError? {
        return globalError
    }

    fun setGlobalError(error: GlobalError?) {
        globalError = error
    }

    fun setGlobalErrorFromString(error: String) {
        globalError = GlobalError(error)
    }

    fun getGlobalErrorAsString(): String? {
        return globalError?.globalError?.toString()
    }

    public fun hasError(): Boolean = globalError != null
}

