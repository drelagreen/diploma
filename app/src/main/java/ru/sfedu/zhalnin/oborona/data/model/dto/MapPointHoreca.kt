package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для хранения информации о метке заведения для отображения на карте
 *
 * @property id Int Id метки
 * @property name String Название заведения
 * @property description String? Описание заведения (необязательное поле)
 * @property latitude Double Латитуда метки на карте
 * @property longitude Double Лонгитуда метки на карте
 * @property phone String Номер телефона контактного лица
 *
 * @property SerializedName имя свойства в АПИ
 */
data class MapPointHoreca(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("discription")
    val description: String?,
    @SerializedName("coord_lat")
    val latitude: Double,
    @SerializedName("coord_long")
    val longitude: Double,
    @SerializedName("phonenumber")
    val phone: String
)
