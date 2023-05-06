package ru.sfedu.zhalnin.oborona.data.model.dto.requests

import com.google.gson.annotations.SerializedName

/**
 * Дата-класс исп. для формирования запроса с кодом спонсора на сервер
 *
 * @property[code] код спонсора, вводимый в форме
 *
 * @property[SerializedName] имя свойства в АПИ
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.responses.SponsorCodeResponse
 * */
data class SponsorCodeRequestBody(
    @SerializedName("code")
    val code: String
)