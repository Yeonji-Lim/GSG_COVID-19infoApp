package com.example.covid_19info.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class PWResult(
    val success: Int? = null,
    val error: Int? = null
)