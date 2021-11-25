package com.example.covid_19info.data

import com.example.covid_19info.data.model.PwchangeUser

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ChangeResult<out T : Any> {

    data class Success<out T : Any>(val data: PwchangeUser) : ChangeResult<T>()
    data class Error(val exception: Exception) : ChangeResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}