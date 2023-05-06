package ru.sfedu.zhalnin.oborona.data.model.dto.requests

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для формирования запроса авторизации на сервер
 *
 * @property username email пользователя, указываемый при авторизации
 * @property password пароль пользователя указываемый при авторизации
 *
 * @property SerializedName имя свойства в АПИ
 * @see AuthResponse
 * */
data class LoginBody(
    @SerializedName("username")
    val username: String?,
    @SerializedName("password")
    var password: String?
)
