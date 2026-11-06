package com.neuroverse.auth.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun `when email is valid format, returns valid`() {
        val result = validateEmail("user@example.com")
        assertTrue(result.isValid)
    }

    @Test
    fun `when email has whitespace around, trims and returns valid`() {
        val result = validateEmail("  user.name+tag@sub.domain.co.uk  ")
        assertTrue(result.isValid)
    }

    @Test
    fun `when email is empty, returns invalid`() {
        val result = validateEmail("")
        assertFalse(result.isValid)
    }

    @Test
    fun `when email is missing domain, returns invalid`() {
        val result = validateEmail("alex@")
        assertFalse(result.isValid)
    }

    @Test
    fun `when email has no at symbol, returns invalid`() {
        val result = validateEmail("alex.example.com")
        assertFalse(result.isValid)
    }
}
