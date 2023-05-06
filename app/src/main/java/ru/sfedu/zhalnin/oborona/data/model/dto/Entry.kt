package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Дата-класс исп. для получения подробностей о записи на событие. Используется в календаре событий
 *
 * @property event Event Обект дата-класса исп. для получения информации о событии, на которое происходила запись
 * @property entryRole Role Обект дата-класса исп. для получения информации о роли, на которую записался пользователь при записи на событие
 * @property withDate Boolean Свойство для календаря событий для правильной отрисовки карточки события - с датой или без
 *
 * @property SerializedName имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.responses.EntryResponse
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.requests.EntryRequest
 */
data class Entry(
    @SerializedName("event")
    val event: Event,

    @SerializedName("role")
    val entryRole: EntryRole,

    @SerializedName("withDate")
    var withDate: Boolean = false
)

/**
 * Дата-класс исп. для получения информации о событии, на которое происходила запись
 *
 * @property id String Id события
 * @property name String Название события
 * @property timeStart Date Дата и время начала события
 * @property timeEnd Date? Дата и время окончания события (необязательное поле)
 *
 * @property SerializedName имя свойства в АПИ
 */
data class Event(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("time_start")
    val timeStart: Date,

    @SerializedName("time_end")
    val timeEnd: Date?
)

/**
 * Дата-класс содержит название роли, которую выбрал пользователь при записи на событие
 *
 * @property name String
 *
 * @property SerializedName имя свойства в АПИ
 */
data class EntryRole(
    @SerializedName("role_name")
    val name: String
)