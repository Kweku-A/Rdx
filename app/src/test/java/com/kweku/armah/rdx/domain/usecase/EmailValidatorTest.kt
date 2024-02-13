package com.kweku.armah.rdx.domain.usecase

import com.kweku.armah.rdx.domain.util.EmailValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {
    private val sut = EmailValidator()

    @Test
    fun should_return_true_when_email_format_is_correct(){
        val email = "abc@abc.com"
        val actual = sut(email = email)
        assertTrue(actual)
    }

    @Test
    fun should_return_false_when_email_format_is_not_correct(){
        val email1 = "abc@abc"
        val email2 = "abc.com"
        val email3 = "abcabccom"
        val actual1 = sut(email = email1)
        val actual2 = sut(email = email2)
        val actual3 = sut(email = email3)
        assertFalse(actual1)
        assertFalse(actual2)
        assertFalse(actual3)
    }
}