package com.example.kormopack.domain.authfrag

class CheckStringUseClass {
    fun execute(string: String): List<Boolean> {
        val input = string.toString().trim()
        if (input.isNotEmpty() && input.length >= 12) {
            val regex = Regex("^[А-ЯҐЄІЇ][а-яґєії]+\\s[А-ЯҐЄІЇ][а-яґєії]+\\s[А-ЯҐЄІЇ][а-яґєії]+")
            if (!regex.matches(input)) {
                return listOf(true, false)
            } else {
                return listOf(false, true)
            }
        } else {
            return listOf(false, false)
        }
    }
}