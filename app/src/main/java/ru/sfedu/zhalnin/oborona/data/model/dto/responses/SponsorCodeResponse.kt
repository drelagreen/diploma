package ru.sfedu.zhalnin.oborona.data.model.dto.responses

/**
 * Enum-класс исп. для приема ответа от сервера при предоставлении кода спонсора
 *
 * @see ru.sfedu.zhalnin.oborona.data.model.dto.requests.SponsorCodeRequestBody
 */
enum class SponsorCodeResponse {
    OK, ERROR, SERVER_ERROR, ACTIVATED, NO_PROMO, PROMO_ACTIVATED
}