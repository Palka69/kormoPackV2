package com.example.kormopack.domain.mainact

class SignOutUseCase(private val signOutProvider: SignOutProvider) {
    fun signOut() {
        signOutProvider.signOut()
    }
}