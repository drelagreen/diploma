package ru.sfedu.zhalnin.oborona.domain

data class EventInfo(
    val type: Type,
    val tittle: String,
    val imgUrl: String,
    val description: String?,
    val id: String,
) {
    enum class Type {
        COMMON, SPECIAL
    }
}