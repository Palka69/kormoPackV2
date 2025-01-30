package com.example.kormopack.domain.authfrag

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class AuthenticatedModel(
    val isUserAuthenticated: Boolean = false,
    val googleUser:  GoogleSignInAccount? = null
)