package ru.sfedu.zhalnin.oborona.data.model.dto.responses

import okhttp3.ResponseBody

/**
 * Универсальный дата-класс исп. для создания обертки ответа от сервера при запросах
 *
 * @param T Класс, обьект которого нужно принять при успешном запросе
 * @property result ModelResponseResult Enum-класс исп. для уведомления о результате запроса
 * @property data T? Обьект который нужно принять при успешном запросе
 * @property errorBody ResponseBody? Объект, содержащий данные об ошибке при неуспешном запросе
 */
data class ModelResponse<T>(
    val result: ModelResponseResult,
    val data: T?,
    val errorBody: ResponseBody? = null
) {
    /**
     * Enum-класс исп. для уведомления о результате запроса
     *
     * @property OK Запрос выполнен успешно
     * @property SERVER_ERROR Произошла внутренняя ошибка сервера при выполнении запроса
     * @property USER_ERROR Произошла пользовательская ошибка (4XX) при выполнении запроса
     * @property FAIL Ответ от сервера получен, но операцию выполнить не удалось
     */
    enum class ModelResponseResult {
        OK, SERVER_ERROR, USER_ERROR, FAIL
    }
}