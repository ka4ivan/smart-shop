package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.data.model.ListitemData
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.theme.Red
import com.smartshop.ui.viewmodel.ListitemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListitemScreen(
    navController: NavController,
    viewModel: ListitemViewModel,
    listitemId: String,
    modifier: Modifier = Modifier
) {
    val listitemData = remember { mutableStateOf<ListitemData?>(null) }
    val qty = remember { mutableStateOf(1) } // Ініціалізація кількості

    LaunchedEffect(listitemId) {
        listitemData.value = viewModel.showListitem(listitemId)
    }

    val listitem = listitemData.value

    if (listitem != null) {
        var inputName by remember { mutableStateOf(listitem.name) }
        var inputQty by remember { mutableStateOf(listitem.qty.toString()) }
        var inputUnit by remember { mutableStateOf(listitem.unit) }

        Box(modifier = modifier.fillMaxSize()) {
            // Кнопка назад
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("list_screen/${listitem.listId}")
                    },
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

            // Форма для редагування Listitem
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp, top = 50.dp, bottom = 16.dp)
            ) {
                // Назва
                OutlinedTextField(
                    value = inputName,
                    onValueChange = { newText ->
                        inputName = newText
                        // Зберігаємо зміни в listitem
                        viewModel.updateListitem(listitemId, listitem.copy(name = newText))
                    },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Одиниця виміру
                OutlinedTextField(
                    value = inputUnit,
                    onValueChange = { newText ->
                        inputUnit = newText
                        // Зберігаємо зміни в listitem
                        viewModel.updateListitem(listitemId, listitem.copy(unit = newText))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.unit),
                            fontWeight = FontWeight.ExtraBold,
                            color = LocalCustomColors.current.textSecondary
                        )
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.ExtraBold),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = LocalCustomColors.current.inputBackground,
                        focusedBorderColor = LocalCustomColors.current.lightGray,
                        unfocusedBorderColor = LocalCustomColors.current.lightGray,
                        focusedTextColor = LocalCustomColors.current.text,
                        unfocusedTextColor = LocalCustomColors.current.text,
                    ),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Кількість і кнопки для її зміни
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Кількість
                    OutlinedTextField(
                        value = inputQty,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() }) {
                                inputQty = newText
                                // Зберігаємо зміни в listitem
                                viewModel.updateListitem(
                                    listitemId,
                                    listitem.copy(qty = newText.toDouble())
                                )
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.qty),
                                fontWeight = FontWeight.ExtraBold,
                                color = LocalCustomColors.current.textSecondary
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.ExtraBold),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = LocalCustomColors.current.inputBackground,
                            focusedBorderColor = LocalCustomColors.current.lightGray,
                            unfocusedBorderColor = LocalCustomColors.current.lightGray,
                            focusedTextColor = LocalCustomColors.current.text,
                            unfocusedTextColor = LocalCustomColors.current.text,
                        ),
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
                        modifier = Modifier.weight(1f)
                    )

                    // Кнопки + і -
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Кнопка +
                        IconButton(
                            onClick = {
                                val newQty = (inputQty.toDoubleOrNull() ?: 0.0) + 1.0
                                inputQty = newQty.toString()
                                // Зберігаємо зміни в listitem
                                viewModel.updateListitem(
                                    listitemId,
                                    listitem.copy(qty = newQty)
                                )
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(LocalCustomColors.current.inputBackground)
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.plus_skiny),
                                contentDescription = stringResource(R.string.add),
                                tint = LocalCustomColors.current.blue,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Кнопка -
                        IconButton(
                            onClick = {
                                val newQty = (inputQty.toDoubleOrNull() ?: 0.0) - 1.0
                                if (newQty >= 0) {
                                    inputQty = newQty.toString()
                                    // Зберігаємо зміни в listitem
                                    viewModel.updateListitem(
                                        listitemId,
                                        listitem.copy(qty = newQty)
                                    )
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(LocalCustomColors.current.inputBackground)
                                .size(56.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.minus),
                                contentDescription = "remove",
                                tint = LocalCustomColors.current.blue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                // Кнопка видалення
                Button(
                    onClick = {
                        viewModel.deleteListitem(listitem.id)
                        navController.navigate("list_screen/${listitem.listId}")
                    },
                    modifier = Modifier.padding(top = 14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.trash),
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp),
                            tint = Red
                        )
                        Text(
                            text = stringResource(R.string.delete),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Red
                        )
                    }
                }

            }
        }
    }
}
