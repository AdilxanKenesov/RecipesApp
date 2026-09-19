package uz.gita.recipesapp.data.source.remote.api

import com.google.gson.JsonParser
import kotlinx.coroutines.CancellationException
import retrofit2.Response
import uz.gita.recipesapp.domain.exception.ApiException
import uz.gita.recipesapp.domain.exception.NetworkException
import uz.gita.recipesapp.domain.exception.NotFoundException
import java.io.IOException

suspend fun <T, R> safeApiCall(
    request: suspend () -> Response<T>,
    transform: (T) -> R
): Result<R> =
    try {
        val response = request()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Result.success(transform(body))
        } else {
            Result.failure(response.toException())
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e.toDomainException())
    }

fun Response<*>.toException(): Exception {
    val message = errorBody()?.string()?.parseErrorDetail() ?: message().takeIf { it.isNotBlank() }
    return if (code() == HTTP_NOT_FOUND) NotFoundException(message) else ApiException(code(), message)
}

fun Exception.toDomainException(): Exception =
    if (this is IOException) NetworkException(this) else this

private fun String.parseErrorDetail(): String? =
    runCatching {
        val detail = JsonParser.parseString(this).asJsonObject.get("detail") ?: return null
        when {
            detail.isJsonPrimitive -> detail.asString
            detail.isJsonArray -> detail.asJsonArray.firstOrNull()?.asJsonObject?.get("msg")?.asString
            else -> null
        }
    }.getOrNull()?.takeIf { it.isNotBlank() }

private const val HTTP_NOT_FOUND = 404
