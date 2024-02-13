package com.kweku.armah.rdx.domain.util

import javax.inject.Inject

class IsValidEmail @Inject constructor(){

    operator fun invoke(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
    }
}

