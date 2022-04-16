package com.ekotyoo.storyapp.data.repositories

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ekotyoo.storyapp.data.datasource.local.UserPreference
import com.ekotyoo.storyapp.data.datasource.remote.UserRemoteDataSource
import com.ekotyoo.storyapp.data.datasource.remote.api.FakeUserApi
import com.ekotyoo.storyapp.model.UserModel
import com.ekotyoo.storyapp.ui.TestCoroutineRule
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
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var userPref: UserPreference
    private lateinit var context: Context
    private lateinit var userRemoteDataSource: UserRemoteDataSource
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        userRemoteDataSource = UserRemoteDataSource(FakeUserApi())
        userRepository = UserRepository(context, userRemoteDataSource, userPref)
    }

    @Test
    fun `when login success should not throw error`() = testCoroutineRule.runBlockingTest {
        val email = "dummy@mail.com"
        val password = "123456"
        val name = "name"
        val token = "token"
        val id = "id"

        val expectedUser = UserModel(
            id = id,
            name = name,
            email = email,
            password = password,
            token = token,
            isLoggedIn = true
        )

        userRepository.login(email, password)
        Mockito.verify(userPref).updateUser(expectedUser)
    }

    @Test
    fun `when login failed should throw error`() {
        val email = "dummy@mail.com"
        val password = "123456"

        userRemoteDataSource = UserRemoteDataSource(FakeUserApi().apply { shouldThrowError = true })
        userRepository = UserRepository(context, userRemoteDataSource, userPref)

        Assert.assertThrows(AuthError::class.java) {
            runBlockingTest {
                userRepository.login(email, password)
            }
        }
    }

    @Test
    fun `when signup success should throw error`() {
        val email = "dummy@mail.com"
        val password = "123456"
        val name = "name"

        userRemoteDataSource = UserRemoteDataSource(FakeUserApi().apply { shouldThrowError = true })
        userRepository = UserRepository(context, userRemoteDataSource, userPref)

        Assert.assertThrows(AuthError::class.java) {
            runBlockingTest {
                userRepository.signup(name, email, password)
            }
        }
    }

    @Test
    fun `when logout should update user data in datastore with null token and false isLoggedin`() = testCoroutineRule.runBlockingTest {
        userRepository.logout()
        Mockito.verify(userPref).updateUser(UserModel(token = null, isLoggedIn = false))
    }
}