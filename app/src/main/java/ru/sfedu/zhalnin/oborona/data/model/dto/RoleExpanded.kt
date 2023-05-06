package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата класс для хранения расширенной информации о роли, которая отображается при записи на некоторые события
 *
 * @property id String Id роли
 * @property roleName String Название роли
 * @property verboseName String?
 * @property description String? Описание роли
 * @property phone String? Номер телефона контактного лица (необязательное поле)
 * @property moreInfo Boolean
 *
 * @property SerializedName имя свойства в АПИ
 */
data class RoleExpanded(
    @SerializedName("id")
    val id: String,

    @SerializedName("role_name")
    val roleName: String,

    @SerializedName("verbose_name")
    val verboseName: String?,

    @SerializedName("discription")
    val description: String?,

    @SerializedName("phonenumber")
    val phone: String?,

    @SerializedName("more_info")
    val moreInfo: Boolean
)