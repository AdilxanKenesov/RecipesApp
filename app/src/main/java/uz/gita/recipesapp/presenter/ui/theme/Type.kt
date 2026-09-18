package uz.gita.recipesapp.presenter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import uz.gita.recipesapp.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val Literata = FontFamily(
    Font(GoogleFont("Literata"), provider, FontWeight.Normal),
    Font(GoogleFont("Literata"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Literata"), provider, FontWeight.Bold),
)

val Manrope = FontFamily(
    Font(GoogleFont("Manrope"), provider, FontWeight.Normal),
    Font(GoogleFont("Manrope"), provider, FontWeight.Medium),
    Font(GoogleFont("Manrope"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Manrope"), provider, FontWeight.Bold),
)

val OshxonaTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 54.sp, lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 34.sp, lineHeight = 40.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 30.sp, lineHeight = 38.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 22.sp, lineHeight = 28.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Literata, fontWeight = FontWeight.Bold,
        fontSize = 19.sp, lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Bold,
        fontSize = 15.sp, lineHeight = 20.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp
    ),

    labelLarge = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Bold,
        fontSize = 15.sp, lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp, lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Manrope, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp
    ),
)

val Overline = TextStyle(
    fontFamily = Manrope,
    fontWeight = FontWeight.Bold,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.88.sp
)

val BadgeLabel = TextStyle(
    fontFamily = Manrope,
    fontWeight = FontWeight.Bold,
    fontSize = 11.sp,
    lineHeight = 14.sp
)
