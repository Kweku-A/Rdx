package com.kweku.armah.rdx.domain.usecase

import com.kweku.armah.rdx.domain.util.IsValidPhoneNumber
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsValidPhoneNumberTest {
    private val sut = IsValidPhoneNumber()

    @Test
    fun should_return_true_when_telephone_number_is_correct(){
        val number = "+36301234567"
        val actual = sut(phoneNumber = number)
        assertTrue(actual)
    }

    @Test
    fun should_return_false_when_telephone_number_is_less_than_10_digits(){
        val number = "123456789"
        val actual = sut(phoneNumber = number)
        assertFalse(actual)
    }

    @Test
    fun should_return_false_when_telephone_number_is_more_than_13_digits(){
        val number = "12345678901234"
        val actual = sut(phoneNumber = number)
        assertFalse(actual)
    }

}