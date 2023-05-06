package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName

/**
 * Дата класс для хранения минимальной информации о роли для отображения на экране события
 *
 * @property id Int id роли
 * @property name String Название роли
 *
 * @property SerializedName имя свойства в АПИ
 */
data class Role(
    @SerializedName("id")
    val id: Int,

    @SerializedName("role_name")
    val name: String
)
