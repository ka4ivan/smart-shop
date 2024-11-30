package com.smartshop.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListScreen(navController: NavController, modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
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
                        tint = colorResource(R.color.text),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            val images = listOf(
                "carrot", "carrot_2", "beetroot", "broccoli", "granola", "kawaii", "onion",
                "pepper", "pumpkin", "salad", "watermelon", "coffee"
            )

            val randomImageName = remember { images.random() }
            val context = LocalContext.current
            val randomImageResId = context.resources.getIdentifier(randomImageName, "drawable", context.packageName)

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
                placeholder = { // Плейсхолдер замість label
                    Text(
                        text = stringResource(R.string.new_list),
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.text_secondary)
                    )
                },
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(R.color.input_background),
                    focusedBorderColor = colorResource(R.color.light_gray),
                    unfocusedBorderColor = colorResource(R.color.light_gray),
                    focusedTextColor = colorResource(R.color.text),
                    unfocusedTextColor = colorResource(R.color.text),
                ),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
            )

            // Пропозиції
            Text(
                text = stringResource(id = R.string.suggestions),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.text),
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
                stringResource(id = R.string.travel)
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
                            containerColor = if (inputText == suggestion) colorResource(R.color.light_blue) else colorResource(R.color.btn_suggestion_background),
                        )
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 13.sp,
                            color = if (inputText == suggestion) colorResource(R.color.blue) else colorResource(R.color.text_secondary),
                            fontWeight = FontWeight(800),
                            modifier = Modifier.padding(0.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Дія при натисканні кнопки "Створити" */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.btn_add_background),
                )
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.create).uppercase())
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(R.color.btn_add_text),
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}
