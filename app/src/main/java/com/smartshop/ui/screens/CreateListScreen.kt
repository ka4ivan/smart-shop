package com.smartshop.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.data.model.ListData
import com.smartshop.data.utils.UserUtils
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel

@SuppressLint("HardwareIds")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListScreen(navController: NavController, viewModel: ListViewModel, modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val images = listOf(
        "carrot", "carrot_2", "beetroot", "broccoli", "granola", "kawaii", "onion",
        "pepper", "pumpkin", "salad", "watermelon", "coffee"
    )
    val randomImageName = remember { images.random() }
    val randomImageResId = context.resources.getIdentifier(randomImageName, "drawable", context.packageName)

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Кнопка Назад
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate(Screen.ListsScreen.route) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left),
                        contentDescription = stringResource(R.string.back),
                        tint = LocalCustomColors.current.text,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Зображення
            Image(
                painter = painterResource(id = randomImageResId),
                contentDescription = stringResource(R.string.new_list),
                modifier = Modifier
                    .size(120.dp)
                    .padding(top = 24.dp)
            )

            // Поле вводу
            OutlinedTextField(
                value = inputText,
                onValueChange = { newText -> inputText = newText },
                placeholder = {
                    Text(
                        text = stringResource(R.string.new_list),
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalCustomColors.current.textSecondary
                    )
                },
                maxLines = 1,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = LocalCustomColors.current.inputBackground,
                    focusedBorderColor = LocalCustomColors.current.lightGray,
                    unfocusedBorderColor = LocalCustomColors.current.lightGray,
                    focusedTextColor = LocalCustomColors.current.text,
                    unfocusedTextColor = LocalCustomColors.current.text,
                ),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
            )

            // Пропозиції
            Text(
                text = stringResource(id = R.string.suggestions),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            val suggestions = listOf(
                stringResource(id = R.string.products),
                stringResource(id = R.string.food),
                stringResource(id = R.string.medicines),
                stringResource(id = R.string.weekend),
                stringResource(id = R.string.friday),
                stringResource(id = R.string.trip),
                stringResource(id = R.string.supermarket),
                stringResource(id = R.string.home),
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(suggestions) { suggestion ->
                    Button(
                        onClick = { inputText = suggestion },
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputText == suggestion) LocalCustomColors.current.lightBlue else LocalCustomColors.current.btnSuggestionBackground,
                        )
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 13.sp,
                            color = if (inputText == suggestion) LocalCustomColors.current.blue else LocalCustomColors.current.textSecondary,
                            fontWeight = FontWeight(800),
                            modifier = Modifier.padding(0.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка створення
            Button(
                onClick = {
                    val listData = ListData(
                        id = "",
                        name = if (inputText.isBlank()) context.getString(R.string.new_list) else inputText,
                        userId = UserUtils.getUserId(context),
                        delete = false,
                        createdAt = null,
                        updatedAt = null,
                    )

                    viewModel.createList(listData) { newList ->
                        navController.navigate("list_screen/${newList.id}")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColors.current.btnAddBackground,
                )
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.create).uppercase())
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = LocalCustomColors.current.btnAddText,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}
