package com.example.kormopack.domain.authfrag

import android.content.Context

interface ContextProvider {
    val context: Context
    fun getGoogleAccount(): AuthenticatedModel?
}
