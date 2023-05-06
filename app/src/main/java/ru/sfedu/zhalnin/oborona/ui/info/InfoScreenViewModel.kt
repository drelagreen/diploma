package ru.sfedu.zhalnin.oborona.ui.info

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sfedu.zhalnin.oborona.R
import ru.sfedu.zhalnin.oborona.data.model.dto.InfoWindow
import ru.sfedu.zhalnin.oborona.data.model.dto.requests.SponsorCodeRequestBody
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.SponsorCodeResponse
import ru.sfedu.zhalnin.oborona.data.model.repository.ServiceRepository


@Suppress("UNCHECKED_CAST")
class InfoScreenViewModelFactory(
    private val serviceRepository: ServiceRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = InfoScreenViewModel(
        serviceRepository
    ) as T
}

class InfoScreenViewModel(
    private val repository: ServiceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.ProvideToken -> {
                    _state.emit(
                        _state.value.copy(
                            token = action.value
                        )
                    )
                }

                Action.LoadInfoWindows -> {
                    _state.emit(
                        _state.value.copy(
                            dataHasLoaded = false,
                            isError = false
                        )
                    )

                    val infoWindowsData: Array<InfoWindow?> = Array(7) { null }

                    repeat(7) {
                        if (!_state.value.isError) {
                            val infoWindowResult = withContext(Dispatchers.IO) {
                                repository.getInfoWindow(
                                    "${it + 1}",
                                )
                            }

                            if (infoWindowResult.result == ModelResponse.ModelResponseResult.OK) {
                                infoWindowsData[it] = infoWindowResult.data
                            } else {
                                _state.emit(
                                    _state.value.copy(
                                        isError = true
                                    )
                                )
                            }
                        }
                    }
                    if (!_state.value.isError) {
                        val list = infoWindowsData.toList().filterNotNull()
                        if (list.size != 7) {
                            _state.emit(
                                _state.value.copy(
                                    isError = true
                                )
                            )
                        } else {
                            _state.emit(
                                _state.value.copy(
                                    dataHasLoaded = true,
                                    isError = false,
                                    infoWindowsData = list
                                )
                            )
                        }
                    }
                }

                Action.OpenSponsorDialog -> {
                    _state.emit(
                        _state.value.copy(
                            dialogState = State.DialogState(
                                isDialogOpen = true,
                                messageType = State.DialogState.MessageType.NONE,
                                dialogValue = ""
                            )
                        )
                    )
                }

                Action.DialogSendBtnClicked -> {
                    if (_state.value.dialogState.dialogValue.isNotEmpty()) {
                        _state.emit(
                            _state.value.copy(
                                dialogState = _state.value.dialogState.copy(
                                    sendBtnEnabled = false,
                                    messageType = State.DialogState.MessageType.NONE
                                )
                            )
                        )

                        val result = withContext(Dispatchers.IO) {
                            repository.sendSponsorCode(
                                SponsorCodeRequestBody(
                                    _state.value.dialogState.dialogValue
                                ),
                                token = _state.value.token.orEmpty()
                            )
                        }

                        when (result) {
                            SponsorCodeResponse.OK -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.SUCCESS,
                                        dialogMessage = R.string.csdTY,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                            SponsorCodeResponse.ERROR -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.ERROR,
                                        dialogMessage = R.string.csdBadPromo,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                            SponsorCodeResponse.SERVER_ERROR -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.ERROR,
                                        dialogMessage = R.string.csdNoConnection,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                            SponsorCodeResponse.ACTIVATED -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.ERROR,
                                        dialogMessage = R.string.csdBadPromo2,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                            SponsorCodeResponse.NO_PROMO -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.ERROR,
                                        dialogMessage = R.string.csdBadPromo3,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                            SponsorCodeResponse.PROMO_ACTIVATED -> _state.emit(
                                _state.value.copy(
                                    dialogState = _state.value.dialogState.copy(
                                        messageType = State.DialogState.MessageType.NONE,
                                        dialogMessage = R.string.csdPromoAlreadyActivated,
                                        sendBtnEnabled = true
                                    )
                                )
                            )
                        }
                    }
                }

                is Action.DialogValueChanged -> {
                    _state.emit(
                        _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                dialogValue = action.value
                            )
                        )
                    )
                }

                Action.DismissDialog -> {
                    _state.emit(
                        _state.value.copy(
                            dialogState = State.DialogState()
                        )
                    )
                }
            }
        }
    }

    sealed class Action {
        object LoadInfoWindows : Action()
        object OpenSponsorDialog : Action()
        object DialogSendBtnClicked : Action()
        object DismissDialog : Action()
        class DialogValueChanged(val value: String) : Action()
        class ProvideToken(val value: String?) : Action()
    }

    data class State(
        val token: String? = null,
        val dataHasLoaded: Boolean = false,
        val isError: Boolean = false,
        val infoWindowsData: List<InfoWindow> = emptyList(),
        val dialogState: DialogState = DialogState()
    ) {
        data class DialogState(
            val dialogValue: String = "",
            val isDialogOpen: Boolean = false,
            @StringRes val dialogMessage: Int? = null,
            val messageType: MessageType = MessageType.SUCCESS,
            val sendBtnEnabled: Boolean = true
        ) {
            enum class MessageType {
                SUCCESS, ERROR, NONE
            }
        }
    }
}