package ru.sfedu.zhalnin.oborona.data.model.dto.requests

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для формирования запроса отписки от события на сервер
 *
 * @property[eventId] Id мероприятия
 *
 * @property[SerializedName] имя свойства в АПИ
 * */
data class UnsubscribeBody(
    @SerializedName("event_id")
    val eventId: String
)
