package com.example.kormopack.domain.mainact

import com.example.kormopack.domain.authfrag.AuthRepositoryInterface
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class AuthorizationUseCase(
    private val authRepository: AuthRepositoryInterface
) {
    fun signIn(account: GoogleSignInAccount): Result<GoogleSignInAccount> {
        return authRepository.signInWithGoogle(account)
    }

    fun signOut() {
        authRepository.signOut()
    }
}
