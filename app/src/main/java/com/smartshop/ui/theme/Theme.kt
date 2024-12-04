package com.smartshop.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val bottomMenuBackground: Color,
    val text: Color,
    val textSecondary: Color,
    val blue: Color,
    val lightBlue: Color,
    val green: Color,
    val lightGray: Color,
    val btnAddBackground: Color,
    val btnSuggestionBackground: Color,
    val btnAddText: Color,
    val inputBackground: Color,
    val blackWhite: Color,
    val whiteBlack: Color,
    val progressBarBackground: Color,
    val listBackground: Color,
    val listMenu: Color,
    val textThird: Color,
    val btnSaveText: Color,
    val btnSaveBackground: Color,
    val btnAddListitem: Color,
)
val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        bottomMenuBackground = Color.Unspecified,
        text = Color.Unspecified,
        textSecondary = Color.Unspecified,
        blue = Color.Unspecified,
        lightBlue = Color.Unspecified,
        green = Color.Unspecified,
        lightGray = Color.Unspecified,
        btnAddBackground = Color.Unspecified,
        btnSuggestionBackground = Color.Unspecified,
        btnAddText = Color.Unspecified,
        inputBackground = Color.Unspecified,
        blackWhite = Color.Unspecified,
        whiteBlack = Color.Unspecified,
        progressBarBackground = Color.Unspecified,
        listBackground = Color.Unspecified,
        listMenu = Color.Unspecified,
        textThird = Color.Unspecified,
        btnSaveText = Color.Unspecified,
        btnSaveBackground = Color.Unspecified,
        btnAddListitem = Color.Unspecified,
    )
}

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    onBackground = BackgroundDark,
    surface = BackgroundDark,
    onSurface = BackgroundDark,
    primary = TextDark,
    secondary = TextSecondaryDark,
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    onBackground = BackgroundLight,
    surface = BackgroundLight,
    onSurface = BackgroundLight,
    primary = TextLight,
    secondary = TextSecondaryLight,
)

@Composable
fun SmartShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (darkTheme) {
        CustomColors(
            bottomMenuBackground = BackgroundBottomMenuDark,
            text = TextDark,
            textSecondary = TextSecondaryDark,
            blue = BlueDark,
            lightBlue = LightBlueDark,
            green = GreenDark,
            lightGray = LightGrayDark,
            btnAddBackground = BtnAddBackgroundDark,
            btnSuggestionBackground = BtnSuggestionBackgroundDark,
            btnAddText = BtnAddTextDark,
            inputBackground = InputBackgroundDark,
            blackWhite = BlackWhiteDark,
            whiteBlack = WhiteBlackDark,
            progressBarBackground = ProgressBarBackgroundDark,
            listBackground = ListBackgroundDark,
            listMenu = ListMenuDark,
            textThird = TextThirdDark,
            btnSaveText = BtnSaveTextDark,
            btnSaveBackground = BtnSaveBackgroundDark,
            btnAddListitem = BtnAddListitemDark,
        )
    } else {
        CustomColors(
            bottomMenuBackground = BackgroundBottomMenuLight,
            text = TextLight,
            textSecondary = TextSecondaryLight,
            blue = BlueLight,
            lightBlue = LightBlueLight,
            green = GreenLight,
            lightGray = LightGrayLight,
            btnAddBackground = BtnAddBackgroundLight,
            btnSuggestionBackground = BtnSuggestionBackgroundLight,
            btnAddText = BtnAddTextLight,
            inputBackground = InputBackgroundLight,
            blackWhite = BlackWhiteLight,
            whiteBlack = WhiteBlackLight,
            progressBarBackground = ProgressBarBackgroundLight,
            listBackground = ListBackgroundLight,
            listMenu = ListMenuLight,
            textThird = TextThirdLight,
            btnSaveText = BtnSaveTextLight,
            btnSaveBackground = BtnSaveBackgroundLight,
            btnAddListitem = BtnAddListitemLight,
        )
    }

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}