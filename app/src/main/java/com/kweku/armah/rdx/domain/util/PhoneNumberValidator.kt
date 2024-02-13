package com.kweku.armah.rdx.domain.util

import javax.inject.Inject

class PhoneNumberValidator @Inject constructor() {

    operator fun invoke(phoneNumber: String): Boolean {
        val phoneNumberRegex =
            "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*\$".toRegex()
        return phoneNumber.matches(phoneNumberRegex)
    }
}