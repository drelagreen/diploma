package ru.sfedu.zhalnin.oborona.data.model.dto

import com.google.gson.annotations.SerializedName


/**
 * Дата-класс исп. для формирования запроса на сервер с дополнительными данными при записи на реконструкцию
 *
 * @property side String Сторона участия (RU/EN)
 * @property weapon String Есть ли у участника муляж оружия
 * @property costume String Есть ли у участника костюм
 * @property barrack String Будет ли участник проживать в казарме
 * @property chest String? Обхват груди в см (Если костюма нет / необязательное поле)
 * @property waist String? Объват талии в см (Если костюма нет / необязательное поле)
 * @property hips String? Обхват бедер в см (Если костюма нет / необязательное поле)
 * @property height String? Рост в см (Если костюма нет / необязательное поле)
 * @property shoes String? Размер обуви (Если костюма нет / необязательное поле)
 *
 * @property SerializedName имя свойства в АПИ
 * */
data class CostumeForm(

    @SerializedName("side")
    var side: String = "ru",

    @SerializedName("any_weapon")
    var weapon: String = "False",

    @SerializedName("any_costume")
    var costume: String = "False",

    @SerializedName("stay_in_barrack")
    var barrack: String = "False",

    @SerializedName("chest")
    var chest: String? = null,

    @SerializedName("waist")
    var waist: String? = null,

    @SerializedName("hips")
    var hips: String? = null,

    @SerializedName("height")
    var height: String? = null,

    @SerializedName("shoe_size")
    var shoes: String? = null,
)

/**
 * Константы используемые при заполнении Обьекта
 */
const val EN = "en"
const val RU = "ru"
const val FALSE = "False"
const val TRUE = "True"
