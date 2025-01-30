package com.example.kormopack.data

import android.content.Context
import android.util.Log
import com.example.kormopack.domain.authfrag.AuthenticatedModel
import com.example.kormopack.domain.authfrag.ContextProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ContextProviderImp(override val context: Context) : ContextProvider {
    override fun getGoogleAccount(): AuthenticatedModel? {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account?.let {
            Log.d("AuthLog", it.toString())
            AuthenticatedModel(true, it)
        }
    }
}