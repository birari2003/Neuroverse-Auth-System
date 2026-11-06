package com.neuroverse.auth.ui.features.login

import app.cash.turbine.test
import com.neuroverse.auth.core.common.AppResult
import com.neuroverse.auth.core.common.AuthError
import com.neuroverse.auth.core.common.DispatcherProvider
import com.neuroverse.auth.domain.model.User
import com.neuroverse.auth.domain.repository.AuthRepository
import com.neuroverse.auth.domain.usecase.LoginUseCase
import com.neuroverse.auth.domain.validation.ValidateEmail
import com.neuroverse.auth.domain.validation.ValidatePassword
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mockk<AuthRepository>()
    private lateinit var viewModel: LoginViewModel

    private val testDispatchers = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val loginUseCase = LoginUseCase(repository)
        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword(),
            dispatchers = testDispatchers
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when submit with empty inputs, shows validation errors without calling repository`() = runTest {
        viewModel.onEvent(LoginUiEvent.Submit)

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertNotNull(state.passwordError)
    }

    @Test
    fun `when login succeeds, dispatches NavigateToHome effect`() = runTest {
        coEvery { repository.login(any(), any()) } returns AppResult.Success(
            User("1", "Alex", "alex@test.com", 1000L)
        )

        viewModel.effects.test {
            viewModel.onEvent(LoginUiEvent.EmailChanged("alex@test.com"))
            viewModel.onEvent(LoginUiEvent.PasswordChanged("Pass1234"))
            viewModel.onEvent(LoginUiEvent.Submit)

            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is LoginUiEffect.NavigateToHome)
        }
    }

    @Test
    fun `when login fails with InvalidCredentials, updates formError`() = runTest {
        coEvery { repository.login(any(), any()) } returns AppResult.Error(AuthError.InvalidCredentials)

        viewModel.onEvent(LoginUiEvent.EmailChanged("alex@test.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("WrongPass"))
        viewModel.onEvent(LoginUiEvent.Submit)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.formError)
    }
}
