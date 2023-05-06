package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Дата-класс исп. для хранения информации о метке мероприятия для отображения на карте
 * @property id Int Id События
 * @property name String Название события
 * @property address String Адрес проведения события
 * @property timeStart String Дата и время начала события
 * @property timeEnd String? Дата и время окончания события (необязательное поле)
 * @property latitude Double Латитуда для метки на карте
 * @property longitude Double Лонгитуда для метки на карте
 *
 * @property SerializedName имя свойства в АПИ
 */
data class MapPointEvent(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("adress")
    val address:String,
    @SerializedName("time_start")
    val timeStart: Date,
    @SerializedName("time_end")
    val timeEnd: Date?,
    @SerializedName("coord_lat")
    val latitude: Double,
    @SerializedName("coord_long")
    val longitude: Double
)