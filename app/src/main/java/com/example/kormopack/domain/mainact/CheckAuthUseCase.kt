package com.example.kormopack.domain.mainact

import com.example.kormopack.domain.authfrag.AuthRepositoryInterface
import com.example.kormopack.domain.authfrag.AuthenticatedModel

class CheckAuthUseCase(private val authRepository: AuthRepositoryInterface) {
    fun execute(): AuthenticatedModel {
        return authRepository.getGoogleAuthUser()
    }
}
