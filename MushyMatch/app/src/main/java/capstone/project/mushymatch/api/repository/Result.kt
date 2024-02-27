package capstone.project.mushymatch.api.repository

sealed class Result<T> {
    data class Success<T>(val response: T): Result<T>()
    data class Error<Nothing>(val message: String): Result<Nothing>()
}