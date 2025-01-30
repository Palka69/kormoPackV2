package com.example.kormopack.data

import android.content.Context
import android.net.ConnectivityManager
import com.example.kormopack.domain.authfrag.GoogleSignInClientInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes

class GoogleSignInClientProvider (
    private val context: Context
): GoogleSignInClientInterface {
    override fun getGoogleSignInClient(): Map<Boolean, GoogleSignInClient> {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
            .requestEmail()
            .build()
        return mapOf(isNetworkAvailable() to GoogleSignIn.getClient(context, gso))
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}


