package ru.sfedu.zhalnin.oborona.data.model.repository

import retrofit2.Call
import retrofit2.Response
import ru.sfedu.zhalnin.oborona.data.model.dto.CostumeForm
import ru.sfedu.zhalnin.oborona.data.model.dto.EmailUpdatable
import ru.sfedu.zhalnin.oborona.data.model.dto.Entry
import ru.sfedu.zhalnin.oborona.data.model.dto.FullEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.InfoWindow
import ru.sfedu.zhalnin.oborona.data.model.dto.PasswordUpdatable
import ru.sfedu.zhalnin.oborona.data.model.dto.ResetBody
import ru.sfedu.zhalnin.oborona.data.model.dto.RoleExpanded
import ru.sfedu.zhalnin.oborona.data.model.dto.ShortMainEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.ShortTopEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.User
import ru.sfedu.zhalnin.oborona.data.model.dto.UserUpdatable
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.EntryRequest
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.LoginBody
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.RegisterBody
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.SponsorCodeRequestBody
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.UnsubscribeBody
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.AuthResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.MapResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.SponsorCodeResponse
import java.net.ConnectException

/**
 * Реализация серверного репозитория, содержит методы для отправки, приема, обработки серверной информации
 * @property api ServerApi
 * @see ServiceRepository
 */
class DefaultServiceRepository(private val api: ServerApi) : ServiceRepository {
    /**
     * Обертка для серверных запросов. На вход поступает метод Retrofit в виде лямбды.
     * Возвращает объект ModelResponce, который содержит ответ от сервера и результат запроса.
     * @param call Call<T>
     * @return ModelResponse<T>
     * @see ModelResponse
     */
    private fun <T> handleCall(call: Call<T>): ModelResponse<T> {
        return try {
            val response = call.execute()

            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body == null) {
                        ModelResponse(
                            ModelResponse.ModelResponseResult.SERVER_ERROR,
                            null,
                            response.errorBody()
                        )
                    } else {
                        ModelResponse(ModelResponse.ModelResponseResult.OK, body)
                    }
                }

                response.code().is400Error() -> {
                    ModelResponse(
                        ModelResponse.ModelResponseResult.USER_ERROR,
                        null,
                        response.errorBody()
                    )
                }

                else -> {
                    ModelResponse(
                        ModelResponse.ModelResponseResult.SERVER_ERROR,
                        null,
                        response.errorBody()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ModelResponse(ModelResponse.ModelResponseResult.SERVER_ERROR, null)
        }
    }

    /**
     * Метод для произведения авторизации через API. На вход получает "форму" LoginBody.
     * Возвращает объект AuthResponse, который содержит ответ от сервера и результат запроса.
     * Если запрос выполнен неуспешно, происходит обработка с помощью метода handleAuthError
     * @param loginBody LoginBody
     * @return AuthResponse
     * @see LoginBody
     * @see AuthResponse
     * @see handleAuthError
     */
    override fun login(loginBody: LoginBody): AuthResponse {
        return try {
            val call = api.auth(loginBody)
            val response = call.execute()
            Exception().printStackTrace()
            return if (response.isSuccessful) {
                response.body() ?: handleAuthError(response)
            } else {
                handleAuthError(response)
            }
        } catch (e: Exception) {
            AuthResponse("SERVER ERROR", null)
        }
    }

    /**
     * Метод для произведения регистрации через API. На вход получает "форму" регистрации RegisterBody.
     * Возвращает объект AuthResponse, который содержит ответ от сервера и результат запроса.
     * Если запрос выполнен неуспешно, происходит обработка с помощью метода handleRegisterError
     * @param registerBody RegisterBody
     * @return AuthResponse
     * @see RegisterBody
     * @see AuthResponse
     * @see handleRegisterError
     */
    override fun register(registerBody: RegisterBody): AuthResponse {
        val call = api.createUser(registerBody)
        return try {
            val response = call.execute()

            if (response.isSuccessful) {
                return AuthResponse("ok", "ok")
            } else {
                handleRegisterError(response)
            }
        } catch (e: Exception) {
            AuthResponse("SERVER ERROR", null)
        }
    }

    /**
     * Метод для загрузки списка доступных пользователю ролей на мероприятиях с помощью цикла.
     * На вход получает токен авторизции пользователя.
     * В качестве результата возвращается объект ModelResponse.
     * При удачном запросе внутри ответа будет содержаться список с доступными ролями
     *
     * Метод разработан исходя из потребности получать список ролей по ID не имея возможности получить список целиком.
     * Точно известно что роли нумеруются с "1".
     * Метод пробует получить следющую роль с ID от 1 до N, пока сервер не выдаст ошибку.
     * Ошибка от сервера = список ролей кончился
     *
     * @return ModelResponse<List<RoleExpanded>>
     */
    override fun loadRoles(): ModelResponse<List<RoleExpanded>> {
        return api.getExpandedRoles().run(::handleCall)
    }

    /**
     * Метод для обработки ошибок от API при процедуре регистрации.
     * Если ответ от сервера содержит ошибку от 400 до 499 будет указана "ошибка клиента" - USER_ERROR
     * В иных случаях, при ответе отличном от 200-299 метод вернет "ошибку сервера" - SERVER_ERROR
     * В качестве результата позвращает объект AuthResponse с пустым полем "token"
     *
     * @param response Response<Unit> Ответ от сервера при регистрации
     * @return AuthResponse
     * @see AuthResponse
     * @see Response
     */
    private fun handleRegisterError(response: Response<Unit>): AuthResponse {
        val error = when (response.code()) {
            in 400..499 -> {
                response.errorBody()?.string()
            }

            else -> {
                SERVER_ERROR
            }
        }

        return AuthResponse(error, null)
    }

    /**
     * Метод для обработки ошибок от API при процедуре авторизации.
     * Если ответ от сервера содержит ошибку от 400 до 499 будет указана "ошибка клиента" - USER_ERROR
     * В иных случаях, при ответе отличном от 200-299 метод вернет "ошибку сервера" - SERVER_ERROR
     * В качестве результата позвращает объект AuthResponse с пустым полем "token"
     *
     * @param response Response<Unit> Ответ от сервера при регистрации
     * @return AuthResponse
     * @see AuthResponse
     * @see Response
     */
    private fun handleAuthError(response: Response<AuthResponse>): AuthResponse {
        val error = when (response.code()) {
            in 400..499 -> {
                response.errorBody()?.string()
            }

            else -> {
                SERVER_ERROR
            }
        }

        return AuthResponse(error, null)
    }

    /**
     * Метод для получения списка "особых" событий от сервера. События представлены в упрощенном виде
     * Запрос обернут в метод handleCall
     * @return ModelResponse<List<ShortTopEvent>>
     * @see ShortTopEvent
     * @see handleCall
     */
    override fun getTopEvents(): ModelResponse<List<ShortTopEvent>> {
        return api.getTopEvents().run(::handleCall)
    }

    /**
     * Метод для получения списка "обычных" событий от сервера. События представлены в упрощенном виде
     * Запрос обернут в метод handleCall
     * @return ModelResponse<List<ShortMainEvent>>
     * @see ShortMainEvent
     * @see handleCall
     */
    override fun getMainEvents(): ModelResponse<List<ShortMainEvent>> {
        return api.getMainEvents().run(::handleCall)
    }

    /**
     * Метод для получения информации об авторизированном пользователе
     * Запрос обернут в метод handleCall
     * @param token String
     * @return ModelResponse<User>
     */
    override fun getUser(token: String): ModelResponse<User> {
        return api.getUser(token.asToken()).run(::handleCall)
    }

    /**
     * Метод для получения информации о событии с передаваемым id события
     * Запрос обернут в метод handleCall
     * @param id String
     * @return ModelResponse<FullEvent>
     */
    override fun getEvent(id: String): ModelResponse<FullEvent> {
        return api.getEvent(id).run(::handleCall)
    }

    /**
     * Запрос на запись на событие
     * Запрос обернут в метод handleCall
     * @param request EntryRequest
     * @param token String
     * @return ModelResponse<String>
     */
    override fun addEntry(request: EntryRequest, token: String): ModelResponse<String> {
        return api.createEntry(request, token.asToken()).run(::handleCall)
    }

    /**
     * Запрос на получение списка событий, на которые записан пользователь
     * Запрос обернут в метод handleCall
     * @param token String
     * @return ModelResponse<List<Entry>>
     */
    override fun getUserEntries(token: String): ModelResponse<List<Entry>> {
        return api.getEntries(token.asToken()).run(::handleCall)
    }

    /**
     * Загрузка списка меток для карты
     * Запрос обернут в метод handleCall
     * @return ModelResponse<MapResponse>
     */
    override fun getMapPoints(): ModelResponse<MapResponse> {
        return api.getMapPoints().run(::handleCall)
    }

    /**
     * Запрос об отписке от события
     * Запрос обернут в метод handleCall
     * @param token String
     * @param body UnsubscribeBody
     * @return ModelResponse<String>
     */
    override fun unsubscribe(token: String, body: UnsubscribeBody): ModelResponse<String> {
        return api.unsubscribe(token.asToken(), body).run(::handleCall)
    }

    /**
     * Отправка формы заказа костюма
     * Запрос обернут в метод handleCall
     * @param token String
     * @param body CostumeForm
     * @return ModelResponse<String>
     */
    override fun sendCostumeForm(token: String, body: CostumeForm): ModelResponse<String> {
        return api.sendCostumeForm(token.asToken(), body).run(::handleCall)
    }

    /**
     * Получить данные информационного окна по Id
     * Запрос обернут в метод handleCall
     * @param id String
     * @return ModelResponse<InfoWindow>
     */
    override fun getInfoWindow(id: String): ModelResponse<InfoWindow> {
        return api.getInfoWindow(id).run(::handleCall)
    }

    /**
     * Обновить данные пользователя
     * Запрос обернут в метод handleCall
     * @param token String
     * @param body UserUpdatable
     * @return ModelResponse<Unit>
     */
    override fun updateUser(token: String, body: UserUpdatable): ModelResponse<Unit> {
        return api.updateUser(token.asToken(), body).run(::handleCall)
    }

    /**
     * Обновить пароль пользователя
     * Запрос обернут в метод handleCall
     * @param token String
     * @param body PasswordUpdatable
     * @return ModelResponse<Unit>
     */
    override fun updatePassword(token: String, body: PasswordUpdatable): ModelResponse<Unit> {
        return api.updatePassword(token.asToken(), body).run(::handleCall)
    }

    /**
     * Обновить почту пользователя
     * Запрос обернут в метод handleCall
     * @param token String
     * @param body EmailUpdatable
     * @return ModelResponse<Unit>
     */
    override fun updateEmail(token: String, body: EmailUpdatable): ModelResponse<Unit> {
        return api.updateEmail(token.asToken(), body).run(::handleCall)
    }

    /**
     * Отправить код на почту
     * Запрос обернут в метод handleCall
     * @param body EmailUpdatable
     * @return ModelResponse<Unit>
     */
    override fun sendCode(body: EmailUpdatable): ModelResponse<Unit> {
        return api.sendCode(body).run(::handleCall)
    }

    /**
     * Восстановить пароль
     * Запрос обернут в метод handleCall
     * @param body ResetBody
     * @return ModelResponse<Unit>
     */
    override fun resetPassword(body: ResetBody): ModelResponse<Unit> {
        return api.resetPassword(body).run(::handleCall)
    }

    /**
     * Отправить код спонсора
     * @param request SponsorCodeRequestBody
     * @param token String
     * @return SponsorCodeResponse
     */
    override fun sendSponsorCode(
        request: SponsorCodeRequestBody,
        token: String
    ): SponsorCodeResponse {
        val call = api.sendSponsorCode(request, token.asToken())
        return try {
            val response = call.execute()
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body == null) {
                        SponsorCodeResponse.SERVER_ERROR
                    } else {
                        SponsorCodeResponse.OK
                    }
                }

                response.code().is400Error() -> {
                    when {
                        response.errorBody()?.charStream()?.readText()
                            ?.contains(PROMO_ACTIVATED_CODE) ?: false -> {
                            SponsorCodeResponse.ACTIVATED
                        }

                        response.errorBody()?.charStream()?.readText()?.contains(BAD_PROMO)
                            ?: false -> {
                            SponsorCodeResponse.NO_PROMO
                        }

                        response.errorBody()?.charStream()?.readText()
                            ?.contains(PROMO_ACTIVATED_ACC) ?: false -> {
                            SponsorCodeResponse.PROMO_ACTIVATED
                        }

                        else -> {
                            SponsorCodeResponse.ERROR
                        }
                    }
                }

                else -> {
                    SponsorCodeResponse.SERVER_ERROR
                }
            }
        } catch (e: ConnectException) {
            SponsorCodeResponse.SERVER_ERROR
        }
    }

    companion object {
        const val SERVER_ERROR = "Server Error"

        //server answers
        const val PROMO_ACTIVATED_CODE = "Promocode already activated"
        const val PROMO_ACTIVATED_ACC = "You already activated promocode"
        const val BAD_PROMO = "such"
    }
}

fun String.asToken(): String = "Token $this"

private fun Int.is400Error(): Boolean = this in 400..499

