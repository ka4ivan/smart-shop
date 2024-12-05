package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartshop.R
import com.smartshop.ui.theme.LocalCustomColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.hello),
            fontSize = 24.sp,
            color = LocalCustomColors.current.text,
            modifier = Modifier
                .padding(start = 24.dp, top = 32.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Button(
                onClick = { isBottomSheetVisible = true },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColors.current.btnSuggestionBackground, // Колір фону кнопки
                    contentColor = LocalCustomColors.current.text // Колір тексту на кнопці
                )
            ) {
                Text(
                    text = stringResource(R.string.change_theme),
                    fontSize = 16.sp,
                    color = LocalCustomColors.current.text // Колір тексту
                )
            }
        }
    }

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false }
        ) {
            BottomSheetContent(
                currentTheme = currentTheme,
                onThemeChange = {
                    onThemeChange(it)
                    isBottomSheetVisible = false
                },
                onClose = { isBottomSheetVisible = false }
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    currentTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    var selectedOption by remember { mutableStateOf(currentTheme) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dark_theme),
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_uppercase),
                    tint = LocalCustomColors.current.text
                )
            }
        }

        listOf(
            stringResource(R.string.on_uppercase) to true,
            stringResource(R.string.off_uppercase) to false
        ).forEach { (label, value) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { // Реакція на натискання тексту
                        selectedOption = value
                        onThemeChange(value)
                    }
            ) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    color = LocalCustomColors.current.text
                )
                RadioButton(
                    selected = selectedOption == value,
                    onClick = {
                        selectedOption = value
                        onThemeChange(value)
                    }
                )
            }
        }
    }
}