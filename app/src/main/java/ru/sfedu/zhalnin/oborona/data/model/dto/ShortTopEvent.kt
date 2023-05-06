package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата класс исп. для хранения информации об особых событиях
 *
 * @property id Int Id события
 * @property header String Название события
 * @property imgUrl String Ссылка на иллюстрацию события
 *
 * @property SerializedName имя свойства в АПИ
 */
data class ShortTopEvent(
    @SerializedName("pk")
    val id: Int,

    @SerializedName("name")
    val header: String,

    @SerializedName("pic_url")
    val imgUrl: String
)

