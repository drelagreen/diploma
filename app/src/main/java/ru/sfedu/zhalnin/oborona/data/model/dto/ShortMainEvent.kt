package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата класс исп. для хранения краткого описания обычного события
 *
 * @property id Int Id события
 * @property header String Название события
 * @property info String Краткое описание события
 * @property imgUrl String Ссылка на иллюстрацию события
 *
 * @property SerializedName имя свойства в АПИ
 */
data class ShortMainEvent(
    @SerializedName("pk")
    val id: Int,

    @SerializedName("name")
    val header: String,

    @SerializedName("brief_disc")
    val info: String,

    @SerializedName("pic_url")
    val imgUrl: String
)