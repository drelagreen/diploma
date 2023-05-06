package ru.sfedu.zhalnin.oborona.ui.toolbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppToolbarViewModel : ViewModel() {
    private val _state = MutableStateFlow(ToolbarState())
    val state: StateFlow<ToolbarState> get() = _state

    private val _result = MutableStateFlow<Result>(Result.None)
    val result: StateFlow<Result> get() = _result

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.HomeButtonClicked -> {
                    _state.emit(
                        _state.value.copy(
                            homeChecked = true,
                            mapChecked = false,
                            calendarChecked = false,
                            userChecked = false
                        )
                    )
                    _result.emit(Result.HomeButtonClicked)
                }

                is Action.MapButtonClicked -> {
                    _state.emit(
                        _state.value.copy(
                            homeChecked = false,
                            mapChecked = true,
                            calendarChecked = false,
                            userChecked = false
                        )
                    )
                    _result.emit(Result.MapButtonClicked)
                }

                is Action.CalendarButtonClicked -> {
                    _state.emit(
                        _state.value.copy(
                            homeChecked = false,
                            mapChecked = false,
                            calendarChecked = true,
                            userChecked = false
                        )
                    )
                    _result.emit(Result.CalendarButtonClicked)
                }

                is Action.UserButtonClicked -> {
                    _state.emit(
                        _state.value.copy(
                            homeChecked = false,
                            mapChecked = false,
                            calendarChecked = false,
                            userChecked = true
                        )
                    )
                    _result.emit(Result.UserButtonClicked)
                }
            }
        }
    }

    sealed class Action() {
        data class HomeButtonClicked(val checked: Boolean) : Action()
        data class MapButtonClicked(val checked: Boolean) : Action()
        data class CalendarButtonClicked(val checked: Boolean) : Action()
        data class UserButtonClicked(val checked: Boolean) : Action()
    }

    sealed class Result() {
        object HomeButtonClicked : Result()
        object MapButtonClicked : Result()
        object CalendarButtonClicked : Result()
        object UserButtonClicked : Result()
        object None : Result()
    }

    data class ToolbarState(
        val homeChecked: Boolean = true,
        val mapChecked: Boolean = false,
        val calendarChecked: Boolean = false,
        val userChecked: Boolean = false
    )
}