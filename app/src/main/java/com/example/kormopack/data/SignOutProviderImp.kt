package com.example.kormopack.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.kormopack.domain.mainact.SignOutProvider

class SignOutProviderImp(private val context: Context, private val googleSignInClientProvider: GoogleSignInClientProvider) : SignOutProvider {
    override fun signOut() {
        if (googleSignInClientProvider.getGoogleSignInClient()[true] != null) {
            googleSignInClientProvider.getGoogleSignInClient()[true]?.signOut()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SignOutLog", "SignOut Ok")
                    } else {
                        Log.d("SignOutLog", task.exception.toString())
                    }
                }
        } else {
            Toast.makeText(context, "Немає інтернет-з'єднання", Toast.LENGTH_SHORT).show()
        }
    }
}
