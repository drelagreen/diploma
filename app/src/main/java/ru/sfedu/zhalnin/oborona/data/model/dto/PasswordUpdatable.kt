package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс для хранения данных при намерении сменить пароль
 * @property old String Старый пароль
 * @property new String Новый пароль
 */
data class PasswordUpdatable(
    @SerializedName("old_password")
    val old: String,
    @SerializedName("new_password")
    val new: String,
)