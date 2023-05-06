package ru.sfedu.zhalnin.oborona.data.prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Репозиторий для доступа к Preferences - аппаратному хранилищу данных ключ-значение
 */
interface PreferencesRepo {
    fun get(context: Context): SharedPreferences

    companion object {
        const val PINNED = "pinned"
        const val TOKEN = "token"
        const val ID = "id"
        const val DEBUG = "debug"
    }
}