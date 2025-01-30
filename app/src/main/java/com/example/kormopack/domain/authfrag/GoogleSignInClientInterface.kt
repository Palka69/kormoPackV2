package com.example.kormopack.domain.authfrag

import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface GoogleSignInClientInterface {
    fun getGoogleSignInClient(): Map<Boolean, GoogleSignInClient>
}