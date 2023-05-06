package ru.sfedu.zhalnin.oborona.data.model.dto.responses

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для приема отаета от сервера при успешной авторизации
 *
 * @property id Id пользователя
 * @property token токен авторизации пользователя
 *
 * @property[SerializedName] имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.requests.LoginBody
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.requests.RegisterBody
 */
data class AuthResponse(
    @SerializedName("id")
    val id: String?,

    @SerializedName("token")
    val token: String?
)
