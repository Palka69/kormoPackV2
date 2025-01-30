package com.example.kormopack.domain.personalfrag

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SheetsDataUseCase(private val sheetsRepository: SheetsDataRepository) {
    suspend fun execute(account: GoogleSignInAccount): List<List<Any>> {
        return sheetsRepository.fetchSheetsData(account)
    }
}
