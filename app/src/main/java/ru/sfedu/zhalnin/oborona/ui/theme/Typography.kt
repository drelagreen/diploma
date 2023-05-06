package ru.sfedu.zhalnin.oborona.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.sfedu.zhalnin.oborona.R

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

private val robotoRegular = FontFamily(
    Font(R.font.roboto_regular, FontWeight.W400),
)

private val robotoNormal = FontFamily(
    Font(R.font.roboto_medium, FontWeight.W500),
)

private val philosopher = FontFamily(
    Font(R.font.philosopher_regular, FontWeight.Normal),
)

data class AppTypography(
    val header1: TextStyle = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoNormal,
    ),
    val header2: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoNormal,
    ),

    val special1: TextStyle = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = philosopher,
    ),

    val special2: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = philosopher,
    ),

    val button1: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoNormal,
    ),

    val text1: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoRegular,
    ),

    val text2: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoRegular,
        lineHeight = 24.sp
    ),

    val text3: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoRegular,
        lineHeight = 24.sp
    ),
    val text3Bold: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoNormal,
    ),


    val text4: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoRegular,
    ),

    val link1: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = robotoRegular,
    ),
)