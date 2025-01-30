package com.example.kormopack.data

import android.content.Context
import com.example.kormopack.domain.personalfrag.SheetsDataRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.Calendar

class SheetsDataRepositoryImp(private val myContext: Context) : SheetsDataRepository {
    override suspend fun fetchSheetsData(account: GoogleSignInAccount): List<List<Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val transport = GoogleNetHttpTransport.newTrustedTransport()
                val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
                val credential = GoogleAccountCredential.usingOAuth2(
                    myContext, listOf(SheetsScopes.SPREADSHEETS_READONLY)
                ).setSelectedAccount(account.account)

                val service = Sheets.Builder(transport, jsonFactory, credential)
                    .setApplicationName("KormoPack")
                    .build()

                val spreadsheetId = "1ErfWPiMF3bXh97FFfB1i7Eb6exdSG50I38EvTTdph7M"

                val calendar = Calendar.getInstance()
                val listMonth = getMyMonth(calendar.get(Calendar.MONTH) + 1)
                var currentMonth = ""
                val currentYear = calendar.get(Calendar.YEAR).toString()

                for (month in listMonth) {
                    if (sheetExists(service, spreadsheetId, "$month $currentYear")) {
                        currentMonth = month
                        break
                    }
                }

                val range = "$currentMonth $currentYear!A2:AA"

                service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute()
                    .getValues() ?: emptyList()
            } catch (e: IOException) {
                throw e
            } catch (e: GeneralSecurityException) {
                throw e
            }
        }
    }

    private fun getMyMonth(get: Int): List<String> {
        val monthMap = mapOf<Int, String>(1 to "січень",2 to "лютий",3 to "березень",4 to "квітень",5 to "травень",6 to "червень",
            7 to "липень",8 to "серпень",9 to "вересень",10 to "жовтень",11 to "листопад",12 to "грудень")

        val myMonth = monthMap[get]

        return if (myMonth != null) {
            listOf(
                myMonth,
                myMonth.uppercase(),
                myMonth.replaceFirstChar { it.uppercaseChar() }
            )
        } else {
            emptyList()
        }
    }

    private fun sheetExists(service: Sheets, spreadsheetId: String, sheetName: String): Boolean {
        return try {
            val spreadsheet = service.spreadsheets().get(spreadsheetId).execute()
            val sheetNames = spreadsheet.sheets.map { it.properties.title }
            sheetNames.contains(sheetName)
        } catch (e: IOException) {
            false
        }
    }
}
