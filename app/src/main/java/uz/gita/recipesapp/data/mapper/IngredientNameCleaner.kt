package uz.gita.recipesapp.data.mapper

private val brandNames = listOf(
    "365 kun",
    "365 everyday",
    "365 дней",
    "milter 80%",
    "milter",
    "crafers",
    "lurpak",
    "maselkodan",
    "makfa",
    "sherin",
    "kamil",
    "zayka",
    "blanc bleu",
    "из корзинки",
    "jo’ja",
    "jo'ja",
    "derevenskoe",
    "konditer premium"
)

private val brandRegex = Regex(
    brandNames.joinToString(
        separator = "|",
        prefix = """["«“„]?\s*(?<![\p{L}\d])(?:""",
        postfix = """)(?![\p{L}])\s*["»”“]?"""
    ) { Regex.escape(it) },
    RegexOption.IGNORE_CASE
)

private val spacesRegex = Regex("""\s+""")
private val latinWordRegex = Regex("""^[A-Za-z’'`ʻʼ%\d-]+$""")

fun String.withoutBrand(): String {
    val original = trim()
    if (!brandRegex.containsMatchIn(original)) return original
    val cleaned = brandRegex.replace(original, " ")
        .replace(spacesRegex, " ")
        .trim(' ', ',', '-', '–')
    if (cleaned.isEmpty()) return original
    return if (' ' !in cleaned && latinWordRegex.matches(cleaned)) cleaned.withoutPossessive() else cleaned
}

private fun String.withoutPossessive(): String =
    when {
        length > 4 && endsWith("si") -> dropLast(2)
        length > 2 && endsWith("i") -> dropLast(1)
        else -> this
    }
