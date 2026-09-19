package uz.gita.recipesapp.domain.module

enum class AppLanguage(val code: String) {
    UZ("uz"),
    RU("ru");

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code } ?: UZ
    }
}
