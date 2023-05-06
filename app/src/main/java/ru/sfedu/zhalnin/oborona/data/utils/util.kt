package ru.sfedu.zhalnin.oborona.data.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.User
import java.security.MessageDigest
import java.time.temporal.ChronoUnit
import java.util.*

fun String.asPassword(): String {
    return hash(this)
}

/**
 * Функция хеширования строки в SHA-256
 * @param pass String
 * @return String
 */
private fun hash(pass: String): String {
    val bytes = pass.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

@Composable
fun monthsPeriod(date1: Date, date2: Date?): String {
    val cal1 = Calendar.getInstance()
    val cal2 = if (date2 != null) Calendar.getInstance() else null

    cal1.time = date1
    if (date2 != null) {
        cal2?.time = date2
    }

    val month1 = cal1.get(Calendar.MONTH)
    val month2 = cal2?.get(Calendar.MONTH)

    val day1 = cal1.get(Calendar.DAY_OF_MONTH)
    val day2 = cal2?.get(Calendar.DAY_OF_MONTH)

    return if (month1 == month2) {
        if (day1 == day2) {
            "$day1 ${stringArrayResource(id = R.array.months)[month1]}"
        } else {
            if (day2 != null) {
                "$day1 - $day2 ${stringArrayResource(id = R.array.months)[month1]}"
            } else {
                "$day1 ${stringArrayResource(id = R.array.months)[month1]}"
            }
        }
    } else {
        if (month2 != null && day2 != null)
            "$day1 ${stringArrayResource(id = R.array.months)[month1]} - $day2 ${
                stringArrayResource(
                    id = R.array.months
                )[month2]
            }"
        else
            "$day1 ${stringArrayResource(id = R.array.months)[month1]}"
    }
}

@Composable
fun timePeriod(date1: Date, date2: Date?, newLine: Boolean = false): String {
    val cal1 = Calendar.getInstance()
    val cal2 = if (date2 != null) Calendar.getInstance() else null

    cal1.time = date1
    if (date2 != null) {
        cal2?.time = date2
    }

    val hours1 = cal1.get(Calendar.HOUR_OF_DAY)
    val hours2 = cal2?.get(Calendar.HOUR_OF_DAY)

    val min1 = cal1.get(Calendar.MINUTE)
    val min2 = cal2?.get(Calendar.MINUTE)

    return if (hours1 == hours2 && min1 == min2) {
        "${hours1.expand()}:${min1.expand()}"
    } else {
        if (hours2 != null)
            "${hours1.expand()}:${min1.expand()} - ${if (newLine) "\n" else ""}${hours2.expand()}:${min2!!.expand()}"
        else
            "${hours1.expand()}:${min1.expand()}"
    }
}

fun Int.expand(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

fun User.qrData(): String {
    return """
        {
            "id": $id,
            "firstname": "$firstname",
            "lastname": "$lastname",
            "patronymic": "$middlename",
            "gender": "$sex",
            "email": "$email",
            "phone": "${phonenumber.orEmpty()}",
            "sponsor": "${this.isSponsor}",
            "vip": "${this.isVip}",
        }
    """.trimIndent()
}

fun User.getFullName(): String {
    return "$lastname $firstname $middlename"
}

fun Date.removeTime(): Date {
    return Date(this.toInstant().truncatedTo(ChronoUnit.DAYS).toEpochMilli())
}

