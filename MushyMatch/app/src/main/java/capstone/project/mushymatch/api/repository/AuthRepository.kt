package capstone.project.mushymatch.api.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await




class AuthRepository : IAuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(username: String, password: String): Result<AuthResponse> = try {
        val authResult = auth.signInWithEmailAndPassword(username, password).await()
        val user = authResult.user

        if (user != null) {
            Result.Success(AuthResponse(isError = false, message = "Selamat Datang"))
        } else {
            Result.Error("User is null")
        }
    } catch (e: Exception) {
        Result.Error("Email atau Password Salah")
    }

    override suspend fun register(email: String, password: String): Result<AuthResponse> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        Result.Success(AuthResponse(isError = false, message = "Pendaftaran Berhasil"))
    } catch (e: Exception) {
        val errorMessage = e.message ?: "Unknown error"
        Result.Error(message = errorMessage)
    }

    override suspend fun logout(): Result<Boolean> = try {
        auth.signOut()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun getCurrentUserEmail(): Result<String>  = try {
        val user = auth.currentUser ?: error("User Is Null")
        val userEmail = user.email ?: error("User Is Null")
        Result.Success(userEmail)
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

}
interface IAuthRepository {
    suspend fun login(username: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, password: String): Result<AuthResponse>
    suspend fun logout(): Result<Boolean>
    suspend fun getCurrentUserEmail(): Result<String>
}