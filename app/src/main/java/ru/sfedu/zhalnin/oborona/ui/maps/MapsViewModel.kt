package ru.sfedu.zhalnin.oborona.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointHoreca
import ru.sfedu.zhalnin.oborona.data.model.dto.responses.ModelResponse
import ru.sfedu.zhalnin.oborona.data.model.repository.DefaultServiceRepository
import ru.sfedu.zhalnin.oborona.data.model.repository.ServerApi
import ru.sfedu.zhalnin.oborona.data.model.repository.ServiceRepository
import ru.sfedu.zhalnin.oborona.ui.events.EventsViewModel

@Suppress("UNCHECKED_CAST")
class MapsViewModelFactory(
    private val serviceRepository: ServiceRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MapsViewModel(
        serviceRepository
    ) as T
}

class MapsViewModel(
    private val serviceRepository: ServiceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(State.EMPTY)
    val state: MutableStateFlow<State> get() = _state

    fun onAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LoadData -> {
                    _state.emit(_state.value.copy(isError = false, dataHasLoaded = false))

                    val mapPoints = withContext(Dispatchers.IO) {
                        serviceRepository.getMapPoints()
                    }

                    if (mapPoints.result == ModelResponse.ModelResponseResult.OK) {
                        _state.emit(
                            _state.value.copy(
                                horecas = mapPoints.data?.horecas ?: emptyList(),
                                events = mapPoints.data?.events ?: emptyList(),
                                dataHasLoaded = true
                            )
                        )
                    } else {
                        _state.emit(_state.value.copy(isError = true))
                    }

                }
                is Action.CheckHorecas -> {
                    _state.emit(_state.value.copy(horecasChecked = action.checked))
                }
                is Action.CheckEvents -> {
                    _state.emit(_state.value.copy(eventsChecked = action.checked))
                }
                is Action.OpenHoreca -> {
                    _state.emit(_state.value.copy(openedEvent = null, openedHoreca = action.horeca))
                }
                is Action.OpenEvent -> {
                    _state.emit(_state.value.copy(openedEvent = action.event, openedHoreca = null))
                }
                Action.CloseBottomSheet -> {
                    _state.emit(_state.value.copy(openedHoreca = null, openedEvent = null))
                }
            }
        }
    }

    sealed class Action {
        object LoadData : Action()
        class CheckHorecas(val checked: Boolean) : Action()
        class CheckEvents(val checked: Boolean) : Action()
        class OpenHoreca(val horeca: MapPointHoreca) : Action()
        class OpenEvent(val event: MapPointEvent) : Action()
        object CloseBottomSheet : Action()
    }

    data class State(
        val horecas: List<MapPointHoreca>,
        val events: List<MapPointEvent>,
        val horecasChecked: Boolean,
        val eventsChecked: Boolean,
        val openedHoreca: MapPointHoreca?,
        val openedEvent: MapPointEvent?,
        val dataHasLoaded: Boolean = false,
        val isError: Boolean = false,
    ) {
        companion object {
            val EMPTY = State(
                horecas = emptyList(),
                events = emptyList(),
                horecasChecked = true,
                eventsChecked = true,
                openedHoreca = null,
                openedEvent = null,
            )
        }
    }
}