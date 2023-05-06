package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

/**
 * Дата-клас исп. для хранения информации о событии, которую необходимо отобразить на экране описания события
 *
 * @property id String? Id события
 * @property name String Название события
 * @property phone String? Номер телефона ответсвенного лица (необязательное поле)
 * @property imgUrl String Ссылка на иллюстрацию к событию
 * @property about String Описание события
 * @property adress String Адрес проведения события
 * @property startDateTime String Дата и время начала события
 * @property endDateTime String? Дата и время окончания события (необязательное поле)
 * @property roles List<Role> Список доступных ролей
 * @property coordinateLongitude String Лонгитуда для метки на карте
 * @property coordinateLatitude String Латитуда для метки на карте
 * @property isEpic Boolean Флаг особого события
 *
 * @property SerializedName имя свойства в АПИ
 */
data class FullEvent(
    @SerializedName("id")
    var id: String? = "1",

    @SerializedName("name")
    val name: String,

    @SerializedName("phonenumber")
    val phone: String?,

    @SerializedName("pic_url")
    val imgUrl: String,

    @SerializedName("full_disc")
    val about: String,

    @SerializedName("adress")
    val adress: String,

    @SerializedName("time_start")
    val startDateTime: Date,

    @SerializedName("time_end")
    val endDateTime: Date?,

    @SerializedName("roles")
    val roles: List<Role>,

    @SerializedName("coord_long")
    val coordinateLongitude: String,

    @SerializedName("coord_lat")
    val coordinateLatitude: String,

    @SerializedName("is_epic")
    val isEpic: Boolean
) : Serializable
