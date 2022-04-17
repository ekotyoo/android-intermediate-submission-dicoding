package com.ekotyoo.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.TestCoroutineRule
import com.ekotyoo.storyapp.getOrAwaitValue
import com.ekotyoo.storyapp.utils.AuthError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        `when`(userRepository.userModel).thenReturn(
            flow {
                emit(UserModel(email = "dummy@mail.com", password = "password"))
            }
        )

        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when userModel observed should return UserModel`() = testCoroutineRule.runBlockingTest {
        val actualUser = loginViewModel.userModel.getOrAwaitValue()
        assertNotNull(actualUser)
    }

    @Test
    fun `when login called should not throw error`() = testCoroutineRule.runBlockingTest {
        loginViewModel.login("dummy@mail.com", "123456")
        Mockito.verify(userRepository).login("dummy@mail.com", "123456")
    }

    @Test
    fun `when login failed should throw error`() = testCoroutineRule.runBlockingTest {
        val email = "dummy@email.com"
        val password = "dummypassword"

        doThrow(AuthError("Error")).`when`(userRepository).login(email, password)
        loginViewModel.login(email, password)

        Mockito.verify(userRepository).login(email, password)
        assertTrue(loginViewModel.errorMessage.getOrAwaitValue() == "Error")
    }
}