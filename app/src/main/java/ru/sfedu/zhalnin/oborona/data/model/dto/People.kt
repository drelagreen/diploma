package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс для хранения счетчика мужчин/женщин для отображения на экране события "Бал"
 * @property women Int Количество женщин
 * @property men Int Количество мужчин
 *
 * @property SerializedName имя свойства в АПИ
 */
data class People(
    @SerializedName("woman")
    val women: Int,
    @SerializedName("man")
    val men: Int
)
