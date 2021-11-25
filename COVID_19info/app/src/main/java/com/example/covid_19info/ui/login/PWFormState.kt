package com.example.covid_19info.ui.login

/**
 * Data validation state of the login form.
 */
data class PWFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)