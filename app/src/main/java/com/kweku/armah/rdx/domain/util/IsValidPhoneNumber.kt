package com.kweku.armah.rdx.domain.util

import javax.inject.Inject

class IsValidPhoneNumber @Inject constructor() {
    operator fun invoke(phoneNumber: String): Boolean {
        return phoneNumber.isNotBlank()
    }
}