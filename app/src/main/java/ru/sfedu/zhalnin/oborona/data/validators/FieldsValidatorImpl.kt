package ru.sfedu.zhalnin.oborona.data.validators

import android.util.Patterns

/**
 * Реализация валидаторов полей
 */
class FieldsValidatorImpl : FieldsValidator {
    /**
     * Валидация почты - длина, паттерн
     * @param value String
     * @return Boolean
     */
    override fun validateEmail(value: String): Boolean {
        return if (value.isBlank() || value.length < EMAIL_MIN_LENGTH || value.length > EMAIL_MAX_LENGTH) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(value).matches()
        }
    }

    /**
     * Валидация пароля - длина
     * @param value String
     * @return Boolean
     */
    override fun validatePassword(value: String): Boolean {
        return !(value.isBlank() || value.length < PASSWORD_MIN_LENGTH || value.length > PASSWORD_MAX_LENGTH)
    }

    /**
     * Валидация телефона - длина, паттерн
     * @param value String
     * @return Boolean
     */
    override fun validatePhone(value: String): Boolean {
        return if (value.isBlank()) {
            true
        } else if (value.length < PHONE_MIN_LENGTH || value.length > PHONE_MAX_LENGTH) {
            false
        } else {
            Patterns.PHONE.matcher(value).matches()
        }
    }

    /**
     * Валидация имени - длина
     * @param value String
     * @return Boolean
     */
    override fun validateFirstname(value: String): Boolean {
        return !(value.isBlank() || value.length > FIRSTNAME_MAX_LENGTH || value.length < FIRSTNAME_MIN_LENGTH)
    }

    /**
     * Валидация отчества - длина (либо если длина == 0 - ОК)
     * @param value String
     * @return Boolean
     */
    override fun validatePatronymic(value: String): Boolean {
        return if (value.isBlank()) {
            true
        } else {
            !(value.isBlank() || value.length > MIDDLENAME_MAX_LENGTH || value.length < MIDDLENAME_MIN_LENGTH)
        }
    }

    /**
     * Валидация фамилии - длина
     * @param value String
     * @return Boolean
     */
    override fun validateLastname(value: String): Boolean {
        return !(value.isBlank() || value.length > LASTNAME_MAX_LENGTH || value.length < LASTNAME_MIN_LENGTH)
    }

    /**
     * Константы с минимальными и максимальными значениями полей
     */
    companion object {
        const val EMAIL_MAX_LENGTH = 100
        const val FIRSTNAME_MAX_LENGTH = 100
        const val MIDDLENAME_MAX_LENGTH = 100
        const val LASTNAME_MAX_LENGTH = 100
        const val PHONE_MAX_LENGTH = 12
        const val PASSWORD_MAX_LENGTH = 30

        const val EMAIL_MIN_LENGTH = 5
        const val FIRSTNAME_MIN_LENGTH = 2
        const val MIDDLENAME_MIN_LENGTH = 5
        const val LASTNAME_MIN_LENGTH = 2
        const val PHONE_MIN_LENGTH = 12
        const val PASSWORD_MIN_LENGTH = 6
    }
}