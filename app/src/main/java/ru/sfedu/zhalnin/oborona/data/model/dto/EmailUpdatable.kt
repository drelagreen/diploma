package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс используемый для намерения обновить почту пользователя
 * @property email String Новая почта пользователя
 */
data class EmailUpdatable(
    @SerializedName("email")
    val email: String,
)