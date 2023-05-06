package ru.sfedu.zhalnin.oborona.ui.top_toolbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.sfedu.zhalnin.oborona.data.model.dto.User

class TopToolbarViewModel : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val _result = MutableStateFlow<Result?>(null)
    val result: StateFlow<Result?> get() = _result

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.TextChanged -> {
                    _state.emit(_state.value.copy(text = action.text))
                    _result.emit(Result.Filter(action.text))
                }
                Action.BackClicked -> {
                    _result.emit(Result.BackClicked)
                }
                Action.OpenMenu -> {
                    _result.emit(Result.OpenMenu)
                }
                is Action.ChangeMode -> {
                    _state.emit(_state.value.copy(mode = action.mode))
                }

                is Action.ResultCaught -> {
                    _result.emit(null)
                }
                is Action.ChangeBackButtonSate -> {
                    _state.emit(_state.value.copy(backButtonEnabled = action.state))
                }
                is Action.ChangeTittle -> {
                    _state.emit(_state.value.copy(tittle = action.tittle))
                }
                is Action.ProvideUser -> {
                    _state.emit(_state.value.copy(userInfo = action.user))
                }
            }
        }
    }

    sealed class Action {
        class TextChanged(val text: String) : Action()
        object BackClicked : Action()
        object OpenMenu : Action()

        class ChangeMode(val mode: Mode) : Action()
        class ChangeTittle(val tittle: String) : Action()
        class ChangeBackButtonSate(val state: Boolean) : Action()
        object ResultCaught : Action()
        class ProvideUser(val user: User?) : Action()
    }

    sealed class Result {
        object BackClicked : Result()
        class Filter(val value: String) : Result()
        object OpenMenu : Result()
    }

    data class State(
        val text: String = "",
        val mode: Mode = Mode.SEARCH,
        val tittle: String = "",
        val userInfo: User? = null,
        val backButtonEnabled: Boolean = true
    )


    enum class Mode {
        SEARCH, USER, COMMON
    }
}