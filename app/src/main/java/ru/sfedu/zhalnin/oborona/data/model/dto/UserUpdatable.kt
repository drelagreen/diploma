package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс для хранения данных об обновленной информации о пользователе
 * @property firstname String Имя
 * @property middlename String Отчество
 * @property lastname String Фамилия
 * @property phone String Телефон
 * @property sex String Пол
 */
data class UserUpdatable(
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("middlename")
    val middlename: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("phonenumber")
    val phone: String,
    @SerializedName("sex")
    val sex: String,
)