package ru.sfedu.zhalnin.oborona.data.model.dto.responses

import com.google.gson.annotations.SerializedName
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointEvent
import ru.sfedu.zhalnin.oborona.data.model.dto.MapPointHoreca

/**
 * Дата-класс исп. для приема отаета от сервера при получения списков сероприятий и заведений / меток на карте
 *
 * @property horecas List<MapPointHoreca>
 * @property events List<MapPointEvent>
 *
 * @property[SerializedName] имя свойства в АПИ
 */
data class MapResponse(
    @SerializedName("HoReCas")
    val horecas: List<MapPointHoreca>,
    @SerializedName("Events")
    val events: List<MapPointEvent>
)