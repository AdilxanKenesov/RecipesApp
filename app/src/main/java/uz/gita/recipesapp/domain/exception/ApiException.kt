package uz.gita.recipesapp.domain.exception

class ApiException(val code: Int, message: String?) : Exception(message)
