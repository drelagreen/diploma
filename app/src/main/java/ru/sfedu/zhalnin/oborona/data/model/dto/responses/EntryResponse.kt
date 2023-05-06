package ru.sfedu.zhalnin.oborona.data.model.dto.responses

import com.google.gson.annotations.SerializedName

/**
 * Enum-класс исп. для приема ответа от сервера при записи на событие
 *
 * @property OK - запись прошла успешна
 * @property FAIL - запись не прошла (например при повторной записи)
 * @property SERVER_ERROR - внутренняя ошибка сервера
 * @property AUTH_ERROR - ошибка токена авторизации
 *
 * @property[SerializedName] имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.requests.EntryRequest
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.Entry
 */

enum class EntryResponse {
    OK, FAIL, SERVER_ERROR, AUTH_ERROR
}