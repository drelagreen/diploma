package ru.sfedu.zhalnin.oborona.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sfedu.zhalnin.oborona.data.model.dto.*
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.EntryRequest
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.UnsubscribeBody
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.repository.ServiceRepository
import ru.sfedu.zhalnin.oborona.domain.EventInfo

@Suppress("UNCHECKED_CAST")
class EventsViewModelFactory(
    private val serviceRepository: ServiceRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = EventsViewModel(
        serviceRepository
    ) as T
}

private const val SERVER_OK = "201"

private const val SERVER_OK_2 = "200"

class EventsViewModel(
    private val serviceRepository: ServiceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(EventsState())
    val state: StateFlow<EventsState> get() = _state

    private val _result = MutableStateFlow<Result?>(null)
    val result: StateFlow<Result?> get() = _result

    private var mainEvents: List<ShortMainEvent> = emptyList()
    private var topEvents: List<ShortTopEvent> = emptyList()
    private var roles: List<RoleExpanded> = emptyList()

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.ShowReconstructorPage -> {
                    _state.emit(
                        _state.value.copy(
                            reconstructorPageIsOpen = action.open
                        )
                    )
                }

                is Action.SendReconstructorForm -> {
                    val enrollResult = withContext(Dispatchers.IO) {
                        serviceRepository.sendCostumeForm(
                            body = action.form,
                            token = state.value.userToken ?: ""
                        )
                    }

                    if (enrollResult.result == ModelResponse.ModelResponseResult.OK && enrollResult.data == SERVER_OK_2) {
                        _result.emit(Result.SuccessEnroll)
                    } else if (enrollResult.result == ModelResponse.ModelResponseResult.FAIL) {
                        _result.emit(Result.FailedEnroll)
                    } else if (enrollResult.result == ModelResponse.ModelResponseResult.USER_ERROR) {
                        _result.emit(Result.BadUser)
                    } else {
                        _result.emit(Result.FailedEnroll)
                    }
                }

                is Action.UnsubscribeEntry -> {
                    val result = withContext(Dispatchers.IO) {
                        serviceRepository.unsubscribe(
                            token = state.value.userToken.orEmpty(),
                            body = UnsubscribeBody(action.entry.event.id)
                        )
                    }

                    if (result.result == ModelResponse.ModelResponseResult.OK && result.data == SERVER_OK_2) {
                        _result.emit(Result.SuccessUnsubscribe)
                    } else {
                        _result.emit(Result.FailedUnsubscribe)
                    }
                }
                is Action.LoadUserEntries -> {
                    _state.emit(
                        _state.value.copy(
                            dataHasLoaded = false,
                            isError = false
                        )
                    )

                    if (_state.value.userToken.isNullOrBlank()) {
                        _state.emit(
                            _state.value.copy(
                                dataHasLoaded = true,
                                isError = false,
                                userEntries = emptyList()
                            )
                        )
                    } else {
                        val result = withContext(Dispatchers.IO) {
                            serviceRepository.getUserEntries(_state.value.userToken.orEmpty())
                        }

                        when (result.result) {
                            ModelResponse.ModelResponseResult.OK -> {
                                _state.emit(
                                    _state.value.copy(
                                        dataHasLoaded = true,
                                        isError = false,
                                        userEntries = result.data.orEmpty()
                                    )
                                )
                            }
                            ModelResponse.ModelResponseResult.SERVER_ERROR -> {
                                _state.emit(
                                    _state.value.copy(
                                        isError = true
                                    )
                                )
                            }
                            ModelResponse.ModelResponseResult.USER_ERROR -> {
                                _state.emit(
                                    _state.value.copy(
                                        isError = false,
                                        userToken = null,
                                        dataHasLoaded = true
                                    )
                                )

                                _result.emit(Result.BadUser)
                            }

                            ModelResponse.ModelResponseResult.FAIL -> {
                                _state.emit(
                                    _state.value.copy(
                                        isError = false,
                                        dataHasLoaded = true
                                    )
                                )

                                _result.emit(Result.CantLoadEntries)
                            }
                        }
                    }
                }

                is Action.ResultCaught -> {
                    _result.emit(null)
                }

                is Action.SetFilter -> {
                    val filteredMainEvents = mainEvents.filter {
                        it.header.contains(action.value, true) || it.info.contains(
                            action.value,
                            true
                        )
                    }
                    val filteredTopEvents = topEvents.filter {
                        it.header.contains(action.value, true)
                    }

                    _state.emit(
                        _state.value.copy(
                            commonItems = filteredMainEvents.map { it.toEventInfo() },
                            specialItems = filteredTopEvents.map { it.toEventInfo() }
                        )
                    )
                }

                Action.LoadEvents -> {
                    _state.emit(_state.value.copy(dataHasLoaded = false, isError = false))

                    val mainEvents =
                        withContext(Dispatchers.IO) { serviceRepository.getMainEvents() }
                    val topEvents =
                        withContext(Dispatchers.IO) { serviceRepository.getTopEvents() }
                    val roles =
                        withContext(Dispatchers.IO) { serviceRepository.loadRoles() }

                    if (mainEvents.result == ModelResponse.ModelResponseResult.OK
                        && topEvents.result == ModelResponse.ModelResponseResult.OK
                        && roles.result == ModelResponse.ModelResponseResult.OK
                    ) {
                        this@EventsViewModel.topEvents = topEvents.data ?: emptyList()
                        this@EventsViewModel.mainEvents = mainEvents.data ?: emptyList()
                        this@EventsViewModel.roles = roles.data ?: emptyList()

                        _state.emit(
                            _state.value.copy(
                                specialItems = this@EventsViewModel.topEvents.map { it.toEventInfo() },
                                commonItems = this@EventsViewModel.mainEvents.map { it.toEventInfo() },
                                dataHasLoaded = true
                            )
                        )
                    } else {
                        setErrorStatus()
                    }
                }
                is Action.OpenFullEventInfo -> {
                    _state.emit(
                        _state.value.copy(
                            dataHasLoaded = false,
                            openingFullEventInfo = action.eventInfo,
                            isError = false
                        )
                    )

                    val fullEventResult = withContext(Dispatchers.IO) {
                        serviceRepository.getEvent(
                            action.eventInfo.id,
                        )
                    }

                    if (fullEventResult.result == ModelResponse.ModelResponseResult.OK && fullEventResult.data != null) {
                        _state.emit(
                            _state.value.copy(
                                isEventScreenOpen = true,
                                openedFullEvent = fullEventResult.data,
                                dataHasLoaded = true
                            )
                        )
                    } else {
                        setErrorStatus()
                    }
                }
                Action.CloseFullEvent -> {
                    _state.emit(
                        _state.value.copy(
                            isEventScreenOpen = false,
                            openedFullEvent = null
                        )
                    )
                }

                is Action.OpenFullRole -> {
                    _state.emit(
                        _state.value.copy(
                            openedFullRole = roles.find {
                                it.id == action.roleId
                            }
                        )
                    )
                }

                is Action.CloseFullRole -> {
                    _state.emit(
                        _state.value.copy(
                            openedFullRole = null,
                        )
                    )
                }


                is Action.Enroll -> {
                    val enrollResult = withContext(Dispatchers.IO) {
                        serviceRepository.addEntry(
                            EntryRequest(
                                state.value.user?.id?.toInt() ?: 0,
                                action.eventInfo.id.toInt(),
                                action.roleId.toInt()
                            ),
                            state.value.userToken ?: ""
                        )
                    }

                    if (enrollResult.result == ModelResponse.ModelResponseResult.OK && enrollResult.data == SERVER_OK) {
                        _result.emit(Result.SuccessEnroll)
                    } else if (enrollResult.result == ModelResponse.ModelResponseResult.FAIL) {
                        _result.emit(Result.FailedEnroll)
                    } else if (enrollResult.result == ModelResponse.ModelResponseResult.USER_ERROR) {
                        _result.emit(Result.BadUser)
                    } else {
                        _result.emit(Result.FailedEnroll)
                    }
                }

                is Action.Unsubscribe -> {
                    val result = withContext(Dispatchers.IO) {
                        serviceRepository.unsubscribe(
                            token = state.value.userToken.orEmpty(),
                            body = UnsubscribeBody(state.value.openedFullEvent?.id.orEmpty())
                        )
                    }

                    if (result.result == ModelResponse.ModelResponseResult.OK && result.data == "200") {
                        _result.emit(Result.SuccessUnsubscribe)
                    } else {
                        _result.emit(Result.FailedUnsubscribe)
                    }
                }

                is Action.SetUserInfo -> {
                    _state.emit(
                        _state.value.copy(
                            user = action.user
                        )
                    )
                }

                is Action.SetUserToken -> {
                    _state.emit(
                        _state.value.copy(
                            userToken = action.token
                        )
                    )
                }
            }
        }
    }

    private suspend fun setErrorStatus() {
        _state.emit(
            _state.value.copy(
                isError = true
            )
        )
    }

    sealed class Action {
        object LoadEvents : Action()
        class OpenFullEventInfo(val eventInfo: EventInfo) : Action()
        object CloseFullEvent : Action()
        class SetFilter(val value: String) : Action()
        class Enroll(val eventInfo: EventInfo, val roleId: String) : Action()
        class OpenFullRole(val roleId: String) : Action()
        object CloseFullRole : Action()
        class SetUserInfo(val user: User?) : Action()
        class SetUserToken(val token: String?) : Action()
        object ResultCaught : Action()
        object LoadUserEntries : Action()
        object Unsubscribe : Action()
        class UnsubscribeEntry(val entry: Entry) : Action()
        class ShowReconstructorPage(val open: Boolean) : Action()
        class SendReconstructorForm(val form: CostumeForm) : Action()
    }

    sealed class Result {
        object SuccessEnroll : Result()
        object FailedEnroll : Result()
        object SuccessUnsubscribe : Result()
        object FailedUnsubscribe : Result()
        object BadUser : Result()
        object CantLoadEntries : Result()
    }

    data class EventsState(
        val openedFullRole: RoleExpanded? = null,
        val openedFullEvent: FullEvent? = null,
        val isEventScreenOpen: Boolean = false,
        val pictureUrl: String = "https://img-host.ru/V2tWl.png",
        val commonItems: List<EventInfo> = emptyList(),
        val specialItems: List<EventInfo> = emptyList(),
        val specialItemsIsShown: Boolean = true,
        val dataHasLoaded: Boolean = false,
        val openingFullEventInfo: EventInfo? = null,
        val isError: Boolean = false,
        val user: User? = null,
        val userToken: String? = null,
        val userEntries: List<Entry> = emptyList(),
        val reconstructorPageIsOpen: Boolean = false
    )
}

fun ShortMainEvent.toEventInfo(): EventInfo {
    return EventInfo(
        type = EventInfo.Type.COMMON,
        tittle = this.header,
        description = this.info,
        imgUrl = this.imgUrl,
        id = this.id.toString()
    )
}

fun ShortTopEvent.toEventInfo(): EventInfo {
    return EventInfo(
        type = EventInfo.Type.SPECIAL,
        tittle = this.header,
        imgUrl = this.imgUrl,
        id = this.id.toString(),
        description = null
    )
}