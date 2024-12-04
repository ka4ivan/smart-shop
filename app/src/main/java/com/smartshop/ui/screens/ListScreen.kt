package com.smartshop.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.data.model.ListData
import com.smartshop.data.model.ListitemData
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel

@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel, listId: String, modifier: Modifier = Modifier) {
    val listData = remember { mutableStateOf<ListData?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var listItems = emptyList<ListitemData>()

    LaunchedEffect(listId) {
        listData.value = viewModel.showList(listId.toString())
    }

    val list = listData.value

    Box(modifier = modifier.fillMaxSize()) {
        if (list != null) {
            // Верх
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                .background(
//                    color = LocalCustomColors.current.listBackground,
//                    shape = RoundedCornerShape(0.dp)
//                ) TODO Подумати чи робити
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

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = list.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalCustomColors.current.text,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )

                    Box {
                        IconButton(onClick = { menuExpanded = !menuExpanded }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.menu_dots),
                                contentDescription = stringResource(R.string.menu),
                                modifier = Modifier.size(20.dp),
                                tint = LocalCustomColors.current.listMenu,
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier
                                .background(color = LocalCustomColors.current.bottomMenuBackground)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.repeat),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(28.dp)
                                                .padding(end = 8.dp),
                                            tint = LocalCustomColors.current.text,
                                        )
                                        Text(
                                            text = stringResource(R.string.uncheck_all_items),
                                            color = LocalCustomColors.current.text,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(end = 36.dp),
                                        )
                                    }
                                },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Screen.TrashScreen.route)
                                },
                                modifier = Modifier
                                    .padding(horizontal = 0.dp, vertical = 0.dp)
                                    .fillMaxWidth()
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.trash),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(28.dp)
                                                .padding(end = 8.dp),
                                            tint = Color.Red,
                                        )
                                        Text(
                                            text = stringResource(R.string.clear_purchased_items),
                                            color = Color.Red,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(end = 36.dp),
                                        )
                                    }
                                },
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate(Screen.TrashScreen.route)
                                },
                                modifier = Modifier
                                    .padding(horizontal = 0.dp, vertical = 0.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }

            if (listItems.isEmpty()) {
                val images = listOf(
                    "carrot", "carrot_2", "beetroot", "broccoli", "granola", "kawaii", "onion",
                    "pepper", "pumpkin", "salad", "watermelon", "coffee"
                )
                val randomImageName = remember { images.random() }
                val randomImageResId = context.resources.getIdentifier(randomImageName, "drawable", context.packageName)

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = randomImageResId),
                        contentDescription = "Empty List",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(bottom = 36.dp)
                    )
                    Text(
                        text = stringResource(R.string.what_do_you_need_to_buy),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalCustomColors.current.text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.click_on_the_plus_to_start_adding_products),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = LocalCustomColors.current.textSecondary,
                        modifier = Modifier.padding(bottom = 64.dp)
                    )
                }
            } else {

            }


            // Кнопка для додавання нового списку
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("create_listitem_screen/${list.id}")
                },
                containerColor = LocalCustomColors.current.btnAddBackground,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(18.dp, 18.dp, 18.dp, 64.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                    contentDescription = stringResource(R.string.add),
                    tint = LocalCustomColors.current.btnAddText,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.add).uppercase())
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = LocalCustomColors.current.btnAddText,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
    }
}
