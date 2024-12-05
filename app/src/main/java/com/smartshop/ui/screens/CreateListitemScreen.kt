package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.ServerValue
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.data.model.ListitemData
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListitemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListitemScreen(navController: NavController, viewModel: ListitemViewModel, listId: String, modifier: Modifier = Modifier) {
    var inputValue: String by remember { mutableStateOf("") }
    val existingItems = remember { mutableStateOf<List<ListitemData>>(emptyList()) }
    val loading = remember { mutableStateOf(true) }

    LaunchedEffect(listId) {
        existingItems.value = viewModel.getListitemsOnce(listId)
        loading.value = false  // Set loading to false once data is loaded
    }

    val existingListitems = existingItems.value

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Верх
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    IconButton(
                        onClick = { navController.navigate(Screen.ListsScreen.route) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "Back",
                            tint = LocalCustomColors.current.text,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    // Текстовий інпут
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.add_new_product),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = LocalCustomColors.current.textSecondary
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = LocalCustomColors.current.inputBackground,
                            focusedBorderColor = LocalCustomColors.current.lightGray,
                            unfocusedBorderColor = LocalCustomColors.current.lightGray,
                            focusedTextColor = LocalCustomColors.current.text,
                            unfocusedTextColor = LocalCustomColors.current.text,
                        ),
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
                    )
                }
            }

            val allSuggestions = listOf(
                stringResource(R.string.bread),
                stringResource(R.string.milk),
                stringResource(R.string.cheese),
                stringResource(R.string.butter),
                stringResource(R.string.eggs),
                stringResource(R.string.fruits),
                stringResource(R.string.vegetables),
                stringResource(R.string.meat),
                stringResource(R.string.chicken),
                stringResource(R.string.fish),
                stringResource(R.string.pasta),
                stringResource(R.string.rice),
                stringResource(R.string.cereal),
                stringResource(R.string.sugar),
                stringResource(R.string.salt),
                stringResource(R.string.coffee),
                stringResource(R.string.tea),
                stringResource(R.string.spices),
                stringResource(R.string.flour),
                stringResource(R.string.sauce),
                stringResource(R.string.cookies),
                stringResource(R.string.chocolate),
                stringResource(R.string.ice_cream),
                stringResource(R.string.chips),
                stringResource(R.string.hygiene),
                stringResource(R.string.toothpaste),
                stringResource(R.string.toothbrush),
                stringResource(R.string.shampoo),
                stringResource(R.string.conditioner),
                stringResource(R.string.soap),
                stringResource(R.string.body_lotion),
                stringResource(R.string.deodorant),
                stringResource(R.string.razor),
                stringResource(R.string.tissues),
                stringResource(R.string.sanitary_pads),
                stringResource(R.string.tampons),
                stringResource(R.string.wet_wipes),
                stringResource(R.string.hair_dryer),
                stringResource(R.string.cotton_swabs),
                stringResource(R.string.nail_polish),
                stringResource(R.string.nail_clippers),
                stringResource(R.string.painkillers),
                stringResource(R.string.antibiotics),
                stringResource(R.string.cough_syrup),
                stringResource(R.string.bandages),
                stringResource(R.string.plasters),
                stringResource(R.string.pain_relief),
                stringResource(R.string.antiseptic),
                stringResource(R.string.suitcase),
                stringResource(R.string.tickets),
                stringResource(R.string.sunglasses),
                stringResource(R.string.sunblock),
                stringResource(R.string.backpack),
                stringResource(R.string.sofa),
                stringResource(R.string.bed),
                stringResource(R.string.table),
                stringResource(R.string.chair),
                stringResource(R.string.cupboard),
                stringResource(R.string.tv),
                stringResource(R.string.fridge),
                stringResource(R.string.washing_machine),
                stringResource(R.string.microwave),
                stringResource(R.string.kettle),
                stringResource(R.string.vacuum_cleaner),
                stringResource(R.string.iron),
                stringResource(R.string.lamps),
                stringResource(R.string.curtains),
                stringResource(R.string.bedding),
                stringResource(R.string.towels),
                stringResource(R.string.cleaning_supplies),
                stringResource(R.string.detergent),
                stringResource(R.string.dishwashing_liquid),
                stringResource(R.string.sponges),
                stringResource(R.string.currency),
                stringResource(R.string.adapter),
                stringResource(R.string.power_bank),
                stringResource(R.string.water_bottle),
                stringResource(R.string.snacks),
                stringResource(R.string.first_aid_kit),
                stringResource(R.string.camera),
                stringResource(R.string.travel_bag)
            )

            val filteredSuggestions = remember(inputValue) {
                allSuggestions.filter { it.contains(inputValue, ignoreCase = true) }
            }

            if (loading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        color = LocalCustomColors.current.green
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredSuggestions.isEmpty() && inputValue.isNotBlank()) {
                        val existingItem = existingListitems.find { it.name == inputValue && it.listId == listId }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                val isAdded = remember { mutableStateOf(existingItem != null) }
                                val qty = remember { mutableStateOf(existingItem?.qty ?: 0.0) }

                                SuggestionButton(
                                    text = inputValue,
                                    isAdded = isAdded.value,
                                    qty = qty.value,
                                    onIncreaseClick = {
                                        qty.value += 1.0
                                        val updatedItem = existingItem?.copy(qty = qty.value)
                                            ?: ListitemData(
                                                id = "",
                                                name = inputValue,
                                                qty = qty.value,
                                                unit = "",
                                                delete = false,
                                                isCheck = false,
                                                listId = listId,
                                                createdAt = ServerValue.TIMESTAMP,
                                                updatedAt = ServerValue.TIMESTAMP
                                            )
                                        viewModel.createListitem(updatedItem)
                                        isAdded.value = true
                                    },
                                    onDecreaseClick = {
                                        qty.value -= 1.0
                                        if (qty.value <= 0.0) {
                                            isAdded.value = false
                                        }
                                        viewModel.removeListitem(inputValue, listId, 1.0)
                                    }
                                )
                            }
                        }
                    } else {
                        items(filteredSuggestions) { suggestion ->
                            val existingItem = existingListitems.find { it.name == suggestion && it.listId == listId }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                val isAdded = remember { mutableStateOf(existingItem != null) }
                                val qty = remember { mutableStateOf(existingItem?.qty ?: 0.0) }

                                SuggestionButton(
                                    text = suggestion,
                                    isAdded = isAdded.value,
                                    qty = qty.value,
                                    onIncreaseClick = {
                                        qty.value += 1.0
                                        val updatedItem = existingItem?.copy(qty = qty.value)
                                            ?: ListitemData(
                                                id = "",
                                                name = suggestion,
                                                qty = qty.value,
                                                unit = "",
                                                delete = false,
                                                isCheck = false,
                                                listId = listId,
                                                createdAt = ServerValue.TIMESTAMP,
                                                updatedAt = ServerValue.TIMESTAMP
                                            )
                                        viewModel.createListitem(updatedItem)
                                        isAdded.value = true
                                    },
                                    onDecreaseClick = {
                                        qty.value -= 1.0
                                        if (qty.value <= 0.0) {
                                            isAdded.value = false
                                        }
                                        viewModel.removeListitem(suggestion, listId, 1.0)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionButton(
    text: String,
    isAdded: Boolean,
    qty: Double,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onIncreaseClick,
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ліва частина кнопки
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = if (isAdded) LocalCustomColors.current.blue
                            else LocalCustomColors.current.btnSuggestionBackground,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus_skiny),
                        contentDescription = if (isAdded) "Already added" else "Add product",
                        modifier = Modifier.size(22.dp),
                        tint = if (isAdded) LocalCustomColors.current.whiteBlack else Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = LocalCustomColors.current.text,
                    fontWeight = FontWeight.Bold
                )
            }

            // Права частина кнопки
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (qty > 1) {
                    Text(
                        text = qty.toString().removeSuffix(".0"),
                        fontSize = 14.sp,
                        color = LocalCustomColors.current.text,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onDecreaseClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.minus),
                            contentDescription = "Decrease quantity",
                            tint = Color.Red
                        )
                    }
                } else if (qty > 0.1 && isAdded) {
                    IconButton(
                        onClick = onDecreaseClick,
                        modifier = Modifier.size(14.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                            contentDescription = "Remove item",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
