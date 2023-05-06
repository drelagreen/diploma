package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для хранения информации о пользователе
 *
 * @property id String Айди пользователя
 * @property lastLogin String? Дата и время последней активности (необязательное поле)
 * @property firstname String Имя пользователя
 * @property lastname String Фамилия пользователя
 * @property middlename String? Отчество пользователя (необязательное поле)
 * @property birthday String? Дата рождения пользователя (необязательное поле)
 * @property phonenumber String? Номер телефона пользователя (необязательное поле)
 * @property isSponsor Boolean Флаг статуса спонсора
 * @property isVip Boolean Флаг вип-статуса
 * @property sex String Пол пользователя
 * @property email String Адрес электронной почты пользователя
 * @property entries List<Int> Список событий, на которые записан пользователь
 *
 * @property SerializedName имя свойства в АПИ
 */
data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("last_login")
    val lastLogin: String?,

    @SerializedName("firstname")
    val firstname: String,

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("middlename")
    val middlename: String?,

    @SerializedName("birthday")
    val birthday: String?,

    @SerializedName("phonenumber")
    val phonenumber: String?,

    @SerializedName("is_sponsor")
    var isSponsor: Boolean,

    @SerializedName("is_vip")
    var isVip: Boolean,

    @SerializedName("sex")
    val sex: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("entrys")
    val entries: List<Int>
)
