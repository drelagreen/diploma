package ru.sfedu.zhalnin.oborona.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

/**
 * Класс репозиторий, используется для доступа к криптографической системе хранения аргументов для приложений (строки, числа...)
 *
 * Шифрование происходит алогритмом AES с длиной ключа 256 бит, ключи хранятся в аппаратно-защищенном хранилище
 *
 * Для работы необходим Application Context
 * SecretPreferencesRepo.get(context)
 * .edit()...
 * .getString(...)...
 */

class SecretPreferencesRepo : PreferencesRepo {
    /**
     * Метод возвращает объект защищенного хранилища данных
     * @param context Context
     * @return SharedPreferences
     */
    override fun get(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}