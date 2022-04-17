package com.ekotyoo.storyapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.repositories.UserRepository
import com.ekotyoo.storyapp.TestCoroutineRule
import com.ekotyoo.storyapp.getOrAwaitValue
import com.ekotyoo.storyapp.utils.AuthError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SignupViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var signupViewModel: SignupViewModel

    @Before
    fun setUp() {
        signupViewModel = SignupViewModel(userRepository)
    }

    @Test
    fun `when signup success isSuccess should be true`() = testCoroutineRule.runBlockingTest {
        val name = "dummy"
        val email = "dummy@email.com"
        val password = "dummypassword"

        signupViewModel.signup(name, email, password)
        Mockito.verify(userRepository).signup(name, email, password)
        Assert.assertTrue(signupViewModel.isSuccess.getOrAwaitValue())
    }

    @Test
    fun `when signup failed should throw error`() = testCoroutineRule.runBlockingTest {
        val name = "dummy"
        val email = "dummy@email.com"
        val password = "dummypassword"

        doThrow(AuthError("Error")).`when`(userRepository).signup(name, email, password)
        signupViewModel.signup(name, email, password)

        Mockito.verify(userRepository).signup(name, email, password)
        Assert.assertTrue(signupViewModel.errorMessage.getOrAwaitValue() == "Error")
    }
}