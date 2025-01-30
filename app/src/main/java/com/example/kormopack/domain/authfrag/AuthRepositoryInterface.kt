package com.example.kormopack.domain.authfrag

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepositoryInterface {
    fun getGoogleAuthUser(): AuthenticatedModel
    fun signInWithGoogle(account: GoogleSignInAccount): Result<GoogleSignInAccount>
    fun signOut()
}