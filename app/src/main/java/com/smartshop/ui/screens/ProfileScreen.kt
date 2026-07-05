package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartshop.BuildConfig
import com.smartshop.R
import com.smartshop.ui.theme.LocalCustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    var isThemeSheetVisible by remember { mutableStateOf(false) }
    var isLanguageSheetVisible by remember { mutableStateOf(false) }

    // ModalBottomSheet renders its content in a separate Popup that does not
    // inherit the localized LocalContext override from MainActivity, so it
    // must be re-provided explicitly here to keep the sheets translated.
    val localizedContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ProfileHeader()

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(LocalCustomColors.current.listBackground)
        ) {
            SettingsRow(
                icon = R.drawable.pallet,
                title = stringResource(R.string.change_theme),
                value = stringResource(if (currentTheme) R.string.dark_theme else R.string.light_theme),
                onClick = { isThemeSheetVisible = true }
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 70.dp),
                thickness = 1.dp,
                color = LocalCustomColors.current.lightGray
            )
            SettingsRow(
                icon = R.drawable.language,
                title = stringResource(R.string.language),
                value = if (currentLanguage == "en") "English" else "Українська",
                onClick = { isLanguageSheetVisible = true }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.app_version, BuildConfig.VERSION_NAME),
            fontSize = 12.sp,
            color = LocalCustomColors.current.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }

    if (isThemeSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isThemeSheetVisible = false },
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
        ) {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                ThemeBottomSheetContent(
                    currentTheme = currentTheme,
                    onThemeChange = {
                        onThemeChange(it)
                        isThemeSheetVisible = false
                    }
                )
            }
        }
    }

    if (isLanguageSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isLanguageSheetVisible = false },
            windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
        ) {
            CompositionLocalProvider(LocalContext provides localizedContext) {
                LanguageBottomSheetContent(
                    currentLanguage = currentLanguage,
                    onLanguageChange = {
                        onLanguageChange(it)
                        isLanguageSheetVisible = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.blue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(46.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalCustomColors.current.text
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.hello),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.textSecondary
        )
    }
}

@Composable
private fun SettingsRow(
    icon: Int,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LocalCustomColors.current.lightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = LocalCustomColors.current.blue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.text
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = LocalCustomColors.current.textSecondary
            )
        }

        Icon(
            painter = painterResource(R.drawable.arrow_left),
            contentDescription = null,
            tint = LocalCustomColors.current.textSecondary,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer(rotationZ = 180f)
        )
    }
}

@Composable
private fun SelectableOptionsSheet(
    title: String,
    options: List<Pair<String, Boolean>>,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        options.forEachIndexed { index, (label, selected) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelect(index) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold,
                    color = LocalCustomColors.current.text
                )
                RadioButton(
                    selected = selected,
                    onClick = { onSelect(index) },
                    colors = RadioButtonDefaults.colors(selectedColor = LocalCustomColors.current.blue)
                )
            }
        }
    }
}

@Composable
fun ThemeBottomSheetContent(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val themeOptions = listOf(
        stringResource(R.string.light_theme) to false,
        stringResource(R.string.dark_theme) to true,
    )

    SelectableOptionsSheet(
        title = stringResource(R.string.change_theme),
        options = themeOptions.map { (label, isDark) -> label to (isDark == currentTheme) },
        onSelect = { index -> onThemeChange(themeOptions[index].second) }
    )
}

@Composable
fun LanguageBottomSheetContent(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val languageOptions = listOf(
        "Українська" to "uk",
        "English" to "en",
    )

    SelectableOptionsSheet(
        title = stringResource(R.string.language),
        options = languageOptions.map { (label, code) -> label to (code == currentLanguage) },
        onSelect = { index -> onLanguageChange(languageOptions[index].second) }
    )
}
