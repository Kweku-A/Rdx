package com.kweku.armah.rdx.domain.usecase


import com.kweku.armah.rdx.domain.util.IsValidEmail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsValidEmailTest {

    private lateinit var isValidEmail: IsValidEmail

    @Before
    fun setUp() {
        isValidEmail = IsValidEmail()
    }

    @Test
    fun `invoke with valid simple email returns true`() {
        val email = "test@example.com"
        val result = isValidEmail(email)
        assertTrue("Expected a valid simple email to return true", result)
    }

    @Test
    fun `invoke with valid email containing numbers returns true`() {
        val email = "test123@example.com"
        val result = isValidEmail(email)
        assertTrue("Expected a valid email with numbers to return true", result)
    }

    @Test
    fun `invoke with valid email containing dot returns true`() {
        val email = "test.name@example.com"
        val result = isValidEmail(email)
        assertTrue("Expected a valid email with a dot to return true", result)
    }

    @Test
    fun `invoke with valid email containing underscore returns true`() {
        val email = "test_name@example.com"
        val result = isValidEmail(email)
        assertTrue("Expected a valid email with an underscore to return true", result)
    }

    @Test
    fun `invoke with valid email containing hyphen returns true`() {
        val email = "test-name@example.com"
        val result = isValidEmail(email)
        assertTrue("Expected a valid email with a hyphen to return true", result)
    }

    @Test
    fun `invoke with email missing at symbol returns false`() {
        val email = "testexample.com"
        val result = isValidEmail(email)
        assertFalse("Expected an email missing '@' to return false", result)
    }

    @Test
    fun `invoke with email missing domain returns false`() {
        val email = "test@"
        val result = isValidEmail(email)
        assertFalse("Expected an email missing the domain to return false", result)
    }

    @Test
    fun `invoke with email missing top-level domain returns false`() {
        val email = "test@example"
        val result = isValidEmail(email)
        assertFalse("Expected an email missing the top-level domain to return false", result)
    }

    @Test
    fun `invoke with empty string returns false`() {
        val email = ""
        val result = isValidEmail(email)
        assertFalse("Expected an empty string to return false", result)
    }

    @Test
    fun `invoke with email containing invalid characters returns false`() {
        val email = "test#@example.com"
        val result = isValidEmail(email)
        assertFalse("Expected an email with invalid characters to return false", result)
    }

    @Test
    fun `invoke with email having uppercase characters in domain returns false`() {
        val email = "test@EXAMPLE.com" // The regex '[a-z]' for the domain is case-sensitive
        val result = isValidEmail(email)
        assertFalse("Expected an email with an uppercase domain to return false", result)
    }
}
