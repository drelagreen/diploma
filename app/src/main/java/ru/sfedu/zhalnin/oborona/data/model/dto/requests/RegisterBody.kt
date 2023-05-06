package ru.sfedu.zhalnin.oborona.data.model.dto.requests

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для формирования запроса регистрации на сервер
 *
 * @args:
 * @property firstname имя пользователя, указываемый при регистрации
 * @property middlename отчество пользователя указываемый при регистрации (не обязательное поле)
 * @property lastname фамилия пользователя указываемый при регистрации
 * @property password пароль пользователя указываемый при регистрации
 * @property phonenumber номер телефона пользователя указываемый при регистрации (не обязательное поле)
 * @property sex пароль пользователя указываемый при регистрации
 * @property email адрес электронной почты пользователя указываемый при регистрации
 *
 * @property SerializedName имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.responses.AuthResponse
 * */
data class RegisterBody(
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("middlename")
    val middlename: String?,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phonenumber")
    val phonenumber: String?,
    @SerializedName("sex")
    val sex: String,
    @SerializedName("email")
    val email: String
)
