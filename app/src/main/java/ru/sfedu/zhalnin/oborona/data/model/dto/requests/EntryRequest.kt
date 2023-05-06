package ru.sfedu.zhalnin.oborona.data.model.dto.requests

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для формирования запроса на сервер с намерением записаться на событие
 *
 * @property userId Id пользователя
 * @property eventId Id события
 * @property roleId Id роли
 *
 * @property SerializedName - имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.responses.EntryResponse
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.Entry
 * */
data class EntryRequest(
    @SerializedName("user")
    val userId: Int,

    @SerializedName("event")
    val eventId: Int,

    @SerializedName("role")
    val roleId: Int
)
