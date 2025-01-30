package com.example.kormopack.domain.personalfrag

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface SheetsDataRepository {
    suspend fun fetchSheetsData(account: GoogleSignInAccount): List<List<Any>>
}
