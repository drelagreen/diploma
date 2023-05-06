package ru.sfedu.zhalnin.oborona.data.model.repository

import ru.sfedu.zhalnin.oborona.data.model.dto.*
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.AuthResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.MapResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.SponsorCodeResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.*

/**
 * Интерфейс описывающий высокоуровневые методы для взаимодействия с АПИ
 * Все методы синхронны, их нельзя использовать в главном/UI потоке приложения
 * @see DefaultServiceRepository
 */
interface ServiceRepository {
    /**
     * Метод получает на вход дата-класс с логином и паролем для авторизации пользователя,
     * в результате возвращает результат авторизации - статус, токен авторизации, ID пользователя
     * @param loginBody LoginBody
     * @return AuthResponse
     */
    fun login(loginBody: LoginBody): AuthResponse

    /**
     * Метод получает на вход дата-класс с данными регистрации пользователя.
     * В результате возвращает результат регистрации - статус, токен авторизации, ID пользователя
     * @param registerBody RegisterBody
     * @return AuthResponse
     */
    fun register(registerBody: RegisterBody): AuthResponse

    /**
     * Метод для получения списка "особых" событий. На вход получает токен авторизации.
     * Результат - список событий (при успешном запросе) и статус запроса
     * @return ModelResponse<List<ShortTopEvent>>
     */
    fun getTopEvents(): ModelResponse<List<ShortTopEvent>>

    /**
     * Метод для получения списка "обычных" событий. На вход получает токен авторизации.
     * Результат - список событий (при успешном запросе) и статус запроса
     * @return ModelResponse<List<ShortMainEvent>>
     * @see ShortMainEvent
     */
    fun getMainEvents(): ModelResponse<List<ShortMainEvent>>

    /**
     * Метод для получения данных о пользователе. На вход получет токен авторизации.
     * Результат - дата-класс с информацией о пользователе (при успешном запросе), статус запроса
     * @param token String
     * @return ModelResponse<User>
     * @see User
     */
    fun getUser(token: String): ModelResponse<User>

    /**
     * Метод для получения описания события. На вход получает токен авторизации.
     * Результат - дата-класс с описанием события (при успешном запросе), статус запроса
     * @param id String ID события
     * @return ModelResponse<FullEvent>
     * @see FullEvent
     */
    fun getEvent(id: String): ModelResponse<FullEvent>

    /**
     * Метод для отправки кода-спонсора на сервер. На вход получает код спонсора от пользователя, токен авторизации
     * Результат - статус активации кода пользователя
     * @param request SponsorCodeRequestBody
     * @param token String
     * @return SponsorCodeResponse
     */
    fun sendSponsorCode(request: SponsorCodeRequestBody, token: String): SponsorCodeResponse

    /**
     * Метод для отправки запроса на регистрацию на событии. На вход получает дата-класс с информацией о участнике, токен авторизации.
     * Результат - статус регистрации на мероприятии
     * @param request EntryRequest
     * @param token String
     * @return ModelResponse<String>
     * @see EntryRequest
     */
    fun addEntry(request: EntryRequest, token: String): ModelResponse<String>

    /**
     * Загрузка доступных в приложении ролей
     * @return ModelResponse<List<RoleExpanded>>
     */
    fun loadRoles(): ModelResponse<List<RoleExpanded>>

    /**
     * Загрузка списка событий, на которые записан пользователь
     * @param token String
     * @return ModelResponse<List<Entry>>
     */
    fun getUserEntries(token: String): ModelResponse<List<Entry>>

    /**
     * Загрузка меток для карты
     * @return ModelResponse<MapResponse>
     */
    fun getMapPoints(): ModelResponse<MapResponse>

    /**
     * Отписка от события
     * @param token String
     * @param body UnsubscribeBody Дата-класс с информацией о событии
     * @return ModelResponse<String>
     */
    fun unsubscribe(token: String, body: UnsubscribeBody): ModelResponse<String>

    /**
     * Отправка формы заказа костюма
     * @param token String
     * @param body CostumeForm Дата-класс с формой
     * @return ModelResponse<String>
     */
    fun sendCostumeForm(token: String, body: CostumeForm): ModelResponse<String>

    /**
     * Загрузить данные инфоокна
     * @param id String Id инфоокна
     * @return ModelResponse<InfoWindow>
     */
    fun getInfoWindow(id: String): ModelResponse<InfoWindow>

    /**
     * Загрузить счетчик мкжчин/женщин для события "бал"
     * @return ModelResponse<People>
     */
    fun getPeople(): ModelResponse<People>

    /**
     * Обновить данные пользователя
     * @param token String
     * @param body UserUpdatable Данные пользователя
     * @return ModelResponse<Unit>
     */
    fun updateUser(token: String, body: UserUpdatable): ModelResponse<Unit>

    /**
     * Обновить пароль пользователя
     * @param token String
     * @param body PasswordUpdatable Новый пароль пользователя
     * @return ModelResponse<Unit>
     */
    fun updatePassword(token: String, body: PasswordUpdatable): ModelResponse<Unit>

    /**
     * Обновить почту пользователя
     * @param token String
     * @param body EmailUpdatable Новая почта пользователя
     * @return ModelResponse<Unit>
     */
    fun updateEmail(token: String, body: EmailUpdatable): ModelResponse<Unit>

    /**
     * Отправить код подтверждения на почту (восстановление пароля)
     * @param body EmailUpdatable Почта пользователя
     * @return ModelResponse<Unit>
     */
    fun sendCode(body: EmailUpdatable): ModelResponse<Unit>

    /**
     * Восстановить пароль пользователя
     * @param body ResetBody Новый пароль пользователя + токен восстановления, полученный с почты
     * @return ModelResponse<Unit>
     */
    fun resetPassword(body: ResetBody): ModelResponse<Unit>
}