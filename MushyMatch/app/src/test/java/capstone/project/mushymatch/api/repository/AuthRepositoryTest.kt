package capstone.project.mushymatch.api.repository

import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: IAuthRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `When Login With Correct Credentials Should Return Success`() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val expectedResponse = Result.Success(AuthResponse(isError = false, message = "Login successful"))
        runTest {
            whenever(authRepository.login(email, password)).thenReturn(expectedResponse)
            val response = authRepository.login(email, password)
            assertEquals(expectedResponse, response)
        }
    }
    @Test
    fun `When Login With Incorrect Credentials Should Return Error`() {
        // Given
        val email = "test@example.com"
        val falsePassword = "12345"
        val expectedResponse = Result.Error<AuthResponse>("Invalid credentials")
        runTest {
            whenever(authRepository.login(email, falsePassword)).thenReturn(expectedResponse)
            val response = authRepository.login(email, falsePassword)
            assertEquals(expectedResponse, response)
        }
    }

    @Test
    fun `When Register With New Email Should Return Success`() {
        // Given
        val email = "test@example.com"
        val password = "12345"
        val expectedResponse = Result.Success(AuthResponse(isError = false, message = "Registration successful"))
        runTest {
            whenever(authRepository.register(email, password)).thenReturn(expectedResponse)
            val response = authRepository.register(email, password)
            assertEquals(expectedResponse, response)
        }
    }

    @Test
    fun `When Register With Used Email Should Return Error`() {
        // Given
        val usedEmail = "test@example.com"
        val password = "12345"
        val expectedResponse = Result.Error<AuthResponse>("Email Used")
        runTest {
            whenever(authRepository.register(usedEmail, password)).thenReturn(expectedResponse)
            val response = authRepository.register(usedEmail, password)
            assertEquals(expectedResponse, response)
        }
    }
    @Test
    fun `When Logout Should Return Success`() {
        val expectedResponse = Result.Success<Boolean>(true)
        runTest {
            whenever(authRepository.logout()).thenReturn(expectedResponse)
            val response = authRepository.logout()
            assertEquals(expectedResponse, response)
        }
    }
}
