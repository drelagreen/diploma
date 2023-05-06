package ru.sfedu.zhalnin.oborona.data.validators

/**
 * Интерфейс валидатора полей
 */
interface FieldsValidator {
    /**
     * Валидация почты
     * @param value String
     * @return Boolean
     */
    fun validateEmail(value: String): Boolean

    /**
     * Валидация пароля
     * @param value String
     * @return Boolean
     */
    fun validatePassword(value: String): Boolean

    /**
     * Валидация телефона
     * @param value String
     * @return Boolean
     */
    fun validatePhone(value: String): Boolean

    /**
     * Валидация имени
     * @param value String
     * @return Boolean
     */
    fun validateFirstname(value: String): Boolean

    /**
     * Валидация отчества
     * @param value String
     * @return Boolean
     */
    fun validatePatronymic(value: String): Boolean

    /**
     * Валидация фамилии
     * @param value String
     * @return Boolean
     */
    fun validateLastname(value: String): Boolean
}