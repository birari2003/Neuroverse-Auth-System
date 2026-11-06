package com.neuroverse.auth.data.repository

import app.cash.turbine.test
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.AuthError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeAuthRepositoryTest {

    private lateinit var repository: FakeAuthRepository

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
    }

    @Test
    fun `seeded demo user can login successfully`() = runTest {
        val result = repository.login("demo@neuroverse.com", "Pass1234")
        assertTrue(result is AppResult.Success)
        assertEquals("Alex Mercer", (result as AppResult.Success).data.name)
    }

    @Test
    fun `login with wrong password returns InvalidCredentials`() = runTest {
        val result = repository.login("demo@neuroverse.com", "WrongPassword")
        assertTrue(result is AppResult.Error)
        assertEquals(AuthError.InvalidCredentials, (result as AppResult.Error).error)
    }

    @Test
    fun `registering duplicate email fails with EmailAlreadyInUse`() = runTest {
        val result = repository.register("Alex", "demo@neuroverse.com", "Pass1234")
        assertTrue(result is AppResult.Error)
        assertEquals(AuthError.EmailAlreadyInUse, (result as AppResult.Error).error)
    }

    @Test
    fun `registering new user emits active auth session`() = runTest {
        repository.authState.test {
            // Initial state is null
            assertNull(awaitItem())

            repository.register("New User", "new@neuroverse.com", "Secure123")
            val session = awaitItem()
            assertNotNull(session)
            assertEquals("new@neuroverse.com", session?.user?.email)

            repository.logout()
            assertNull(awaitItem())
        }
    }
}
