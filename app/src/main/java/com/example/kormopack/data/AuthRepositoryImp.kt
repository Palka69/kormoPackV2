package com.example.kormopack.data

import com.example.kormopack.domain.authfrag.AuthRepositoryInterface
import com.example.kormopack.domain.authfrag.AuthenticatedModel
import com.example.kormopack.domain.authfrag.ContextProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class AuthRepositoryImp(
    private val contextProvider: ContextProvider,
    private var googleSignInAccount: GoogleSignInAccount? = null
) : AuthRepositoryInterface {
    override fun getGoogleAuthUser(): AuthenticatedModel {
        return contextProvider.getGoogleAccount() ?: AuthenticatedModel(false, null)
    }

    override fun signInWithGoogle(account: GoogleSignInAccount): Result<GoogleSignInAccount> {
        return try {
            val allowedEmail = "operatorcvk@gmail.com"
            if (account.email == allowedEmail) {
                googleSignInAccount = account
                Result.success(account)
            } else {
                Result.failure(Exception("Недозволений email"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        googleSignInAccount = null
        GoogleSignIn.getClient(contextProvider.context, GoogleSignInOptions.DEFAULT_SIGN_IN)
            .signOut()
    }
}