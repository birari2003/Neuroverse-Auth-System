package com.neuroverse.auth.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `valid registration password passes all checks`() {
        val result = validatePassword.validateForRegistration("Pass1234")
        assertTrue(result.isValid)
    }

    @Test
    fun `registration password shorter than 8 chars fails`() {
        val result = validatePassword.validateForRegistration("Pass1")
        assertFalse(result.isValid)
    }

    @Test
    fun `registration password missing uppercase fails`() {
        val result = validatePassword.validateForRegistration("password123")
        assertFalse(result.isValid)
    }

    @Test
    fun `registration password missing digit fails`() {
        val result = validatePassword.validateForRegistration("PasswordOnly")
        assertFalse(result.isValid)
    }

    @Test
    fun `login password only requires non-empty`() {
        assertTrue(validatePassword.validateForLogin("short").isValid)
        assertFalse(validatePassword.validateForLogin("").isValid)
    }
}
