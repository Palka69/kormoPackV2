package com.example.kormopack.domain.personalfrag

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalculateWorkDayUseCase {
    fun execute(cash: List<List<Any>>, userName: String): MutableList<MutableList<Any>> {
        val resList = mutableListOf<MutableList<Any>>()

        for ((index, list) in cash.withIndex()) {
            if (index >= 65) {
                break
            }
            val tempList = mutableListOf<Any>()
            if (list[8].toString().contains(pibReg(userName))) {
                if (list[0].toString().isNotEmpty()) {

                    tempList.add(getDayOfMonth(list[0].toString()))
                    tempList.add(list[1].toString())

                    val bonusValue = list[25].toString().trim().replace(",", ".")
                    val bonus = try {
                        bonusValue.toDouble() / 100f
                    } catch (e: NumberFormatException) {
                        0f
                    }
                    tempList.add(bonus)
                    tempList.add(12)
                } else {

                    val lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                    tempList.add(getDayOfMonth(cash[index - 1][0].toString()))
                    tempList.add(list[1].toString())

                    val bonusValue = list[25].toString().replace(",", ".")
                    val bonus = try {
                        bonusValue.toDouble() / 100f
                    } catch (e: NumberFormatException) {
                        0f
                    }
                    tempList.add(bonus)
                    if (tempList[0] != lastDay) {
                        tempList.add(12)
                    } else {
                        tempList.add(4)
                    }
                }
            }
            resList.add(tempList)
        }

        return resList
    }

    private fun pibReg(username: String): String {
        val userList = username.split(" ")
        return "${userList[0]} ${userList[1][0]}."
    }

    private fun getDayOfMonth(dateString: String): Int {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val date = dateFormat.parse(dateString)

        return date?.let {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.DAY_OF_MONTH)
        } ?: 1
    }
}
