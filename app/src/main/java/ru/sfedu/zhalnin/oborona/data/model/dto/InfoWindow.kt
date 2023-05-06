package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-клас исп. для хранения информации о "инфоокнах"
 *
 * @property id String Id информационного окна
 * @property name String Название окна
 * @property description String Текст в окне
 * @property pictureUrl String Ссылка на иллюстрацию в окне
 * @property phoneNumber String? Номер телеыона контактного лица (необязательное поле)
 *
 * @property SerializedName имя свойства в АПИ
 */
data class InfoWindow(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("discription")
    val description: String,
    @SerializedName("pic_url")
    val pictureUrl: String,
    @SerializedName("phonenumber")
    val phoneNumber: String?,
)
