package com.example.kormopack.domain.personalfrag

import kotlin.math.round

class InitDayChartUseCase {
    fun execute(workDays: MutableList<MutableList<Any>>, bonusMap: MutableMap<Int, Float>): MutableMap<String, Float> {

        var dayHour = 0f
        var nightHour = 0f
        for ((index, list) in workDays.withIndex()) {
            if (list.size > 0) {
                if (list[1] == "День") {
                    dayHour += list[3].toString().toInt()
                } else {
                    nightHour += list[3].toString().toInt()
                }
            }
        }

        var bonus = 0f
        for (value in bonusMap.values) {
            if (value >= 1f) bonus += (round(value * 100) / 100)
        }

        return mutableMapOf("day" to dayHour, "night" to nightHour, "bonus" to bonus)

    }
}