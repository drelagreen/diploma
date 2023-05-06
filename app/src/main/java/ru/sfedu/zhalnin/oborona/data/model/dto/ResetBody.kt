package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс для хранения данных при намерениии восстановить пароль
 * @property token String Токен восстановления
 * @property password String Новый пароль
 */
data class ResetBody(
    @SerializedName("token")
    val token: String,

    @SerializedName("password")
    val password: String
)
