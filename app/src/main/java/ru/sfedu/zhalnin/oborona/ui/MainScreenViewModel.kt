package ru.sfedu.zhalnin.oborona.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                Action.OpenCalendar -> _state.emit(_state.value.copy(currentScreen = CurrentScreen.CALENDAR))
                Action.OpenHome -> _state.emit(_state.value.copy(currentScreen = CurrentScreen.HOME))
                Action.OpenMap -> _state.emit(_state.value.copy(currentScreen = CurrentScreen.MAP))
                Action.OpenUser -> _state.emit(_state.value.copy(currentScreen = CurrentScreen.USER))
            }
        }
    }

    sealed class Action {
        object OpenHome : Action()
        object OpenMap : Action()
        object OpenCalendar : Action()
        object OpenUser : Action()
    }

    data class State(
        val currentScreen: CurrentScreen = CurrentScreen.HOME,
    )

    enum class CurrentScreen {
        HOME, MAP, CALENDAR, USER
    }
}