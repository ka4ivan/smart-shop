package com.smartshop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay

@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel, listId: String, modifier: Modifier = Modifier) {
    val listData = remember { mutableStateOf<ListData?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var listitems by remember { mutableStateOf<List<ListitemData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Додаємо змінну для завантаження

    LaunchedEffect(listId) {
        isLoading = true
        listitems = viewModel.getListitems(listId)
        listData.value = viewModel.showList(listId.toString())
        isLoading = false
    }

    val list = listData.value

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            // Показуємо індикатор завантаження
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = LocalCustomColors.current.green
                )
            }
        } else if (list != null) {
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

            if (listitems.isEmpty()) {
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
                CheckableList(listitems = listitems, viewModel = viewModel, navController = navController)
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

@Composable
fun CheckableList(listitems: List<ListitemData>, viewModel: ListViewModel, navController: NavController) {
    val mutableListItems = remember { mutableStateListOf<ListitemData>().apply { addAll(listitems) } }

    LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
        items(
            items = mutableListItems,
            key = { it.id }
        ) { listItem ->
            SwipeToDeleteContainer(
                item = listItem,
                onDelete = { itemToRemove ->
                    mutableListItems.remove(itemToRemove)
                    viewModel.deleteListitem(itemToRemove.id)
                }
            ) { item ->
                // Вміст рядка з чекбоксом
                CheckableListItem(
                    listitem = item,
                    onCheckedChange = { updatedItem ->
                        viewModel.updateListitem(item.id, updatedItem)
                    },
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun CheckableListItem(
    listitem: ListitemData,
    onCheckedChange: (ListitemData) -> Unit,
    navController: NavController,
) {
    var checked by remember { mutableStateOf(listitem.isCheck) }
    val animatedScale by animateFloatAsState(
        targetValue = if (checked) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (checked) LocalCustomColors.current.background else LocalCustomColors.current.listBackground)
            .padding(vertical = 15.dp, horizontal = 10.dp)
            .clickable { navController.navigate("listitem_screen/${listitem.id}") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, if (checked) LocalCustomColors.current.background else LocalCustomColors.current.blue, CircleShape)
                .background(LocalCustomColors.current.background, shape = CircleShape)
                .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                .clickable {
                    checked = !checked
                    val updatedItem = listitem.copy(isCheck = checked)
                    onCheckedChange(updatedItem)
                },
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.check),
                    contentDescription = "Checked",
                    tint = LocalCustomColors.current.green,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = listitem.name,
            fontSize = 16.sp,
            color = LocalCustomColors.current.text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        if (listitem.qty > 1) {
            Text(
                text = listitem.qty.toString().removeSuffix(".0") + " " + listitem.unit,
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = LocalCustomColors.current.textSecondary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 25.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )


    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = { DeleteBackground(swipeDismissState = state) },
            dismissContent = { content(item) },
            directions = setOf(SwipeToDismissBoxValue.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = when (swipeDismissState.targetValue) {
        SwipeToDismissBoxValue.EndToStart -> Color.Red
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.trash),
            contentDescription = null,
            tint = Color.White
        )
    }
}
