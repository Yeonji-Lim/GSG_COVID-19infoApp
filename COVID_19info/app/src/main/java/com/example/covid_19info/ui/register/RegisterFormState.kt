package com.example.covid_19info.ui.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val emailError: String = "",
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)