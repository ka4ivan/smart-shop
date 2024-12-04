package com.smartshop.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.data.model.ListData
import com.smartshop.data.utils.UserUtils
import com.smartshop.ui.theme.BlueSky
import com.smartshop.ui.theme.BtnAddBackgroundDark
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.theme.Red
import com.smartshop.ui.viewmodel.ListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ListsScreen(navController: NavController, viewModel: ListViewModel, modifier: Modifier = Modifier) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userId = UserUtils.getUserId(context)

    var lists by remember { mutableStateOf(emptyList<ListData>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        lists = viewModel.getListsOnce(userId)
        isLoading = false
    }

    var menuVisible by remember { mutableStateOf(false) } // Відображення меню
    var renameSheetVisible by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<ListData?>(null) } // Вибраний список
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isDeleteInProgress by remember { mutableStateOf(false) }
    var showUndoNotification by remember { mutableStateOf(false) }
    var deletedList by remember { mutableStateOf<ListData?>(null) }
    var isDeleteCancelled by remember { mutableStateOf(false) }

    LaunchedEffect(showUndoNotification) {
        if (showUndoNotification) {
                deletedList?.let { list ->
                    viewModel.deleteList(list.id)
                    coroutineScope.launch {
                        lists = viewModel.getListsOnce(userId)
                    }
                }

                delay(3000)
                showUndoNotification = false
        }
    }

    // Функція для скасування видалення
    fun cancelDelete() {
        isDeleteCancelled = true
        showUndoNotification = false
        deletedList?.let { list ->
            viewModel.undoList(list.id)
            coroutineScope.launch {
                lists = viewModel.getListsOnce(userId)
            }
        }
        coroutineScope.launch {
            lists = viewModel.getListsOnce(userId)
        }
    }

    // Функція для оновлення імені списку
    fun renameList(newName: String) {
        selectedList?.let { list ->
            viewModel.updateList(list.id, list.copy(name = newName))
        }
        coroutineScope.launch {
            lists = viewModel.getListsOnce(userId)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.my_lists),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LocalCustomColors.current.text
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
                        .wrapContentSize(Alignment.TopEnd)
                        .background(
                            color = LocalCustomColors.current.bottomMenuBackground,
                        )
                        .width(200.dp)
                        .padding(0.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.trash),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(end = 8.dp),
                                    tint = LocalCustomColors.current.text,
                                )
                                Text(
                                    text = stringResource(R.string.trash),
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
                            .background(LocalCustomColors.current.bottomMenuBackground)
                    )
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = LocalCustomColors.current.green
            )
        } else if (lists.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vegetables_wrtite),
                    contentDescription = "Empty List",
                    modifier = Modifier
                        .size(325.dp)
                        .padding(bottom = 36.dp)
                )
                Text(
                    text = stringResource(R.string.lets_plan_your_shopping),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalCustomColors.current.text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = stringResource(R.string.click_on_the_plus_to_create_your_first_list),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = LocalCustomColors.current.textSecondary,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(start = 16.dp, top = 65.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                LazyColumn {
                    items(lists) { list ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 0.dp, vertical = 7.dp)
                                .shadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    clip = false
                                )
                                .background(
                                    LocalCustomColors.current.listBackground,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(start = 15.dp, top = 10.dp, end = 10.dp, bottom = 15.dp)
                                .clickable {
                                    navController.navigate("list_screen/${list.id}")
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = list.name,
                                    color = LocalCustomColors.current.text,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(end = 25.dp),
                                )

                                IconButton(
                                    onClick = {
                                        selectedList = list
                                        menuVisible = true
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .clip(CircleShape)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.menu_dots),
                                        contentDescription = "Menu",
                                        modifier = Modifier.size(12.dp).align(Alignment.CenterEnd),
                                        tint = LocalCustomColors.current.textSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 6.25.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(LocalCustomColors.current.progressBarBackground)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(fraction = 2.toFloat() / 7)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(LocalCustomColors.current.green)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "2/7", // TODO отримати кількості
//                                    text = "${list.completedTasks}/${list.totalTasks}",
                                    color = LocalCustomColors.current.text,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
                if (menuVisible) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            coroutineScope.launch { sheetState.hide() }
                            menuVisible = false
                        },
                        sheetState = sheetState,
                        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Кнопка перейменування
                            Button(
                                onClick = {
                                    coroutineScope.launch { sheetState.hide() }
                                    menuVisible = false
                                    renameSheetVisible = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                                        contentDescription = "Rename",
                                        modifier = Modifier.size(24.dp),
                                        tint = LocalCustomColors.current.textThird
                                    )
                                    Text(
                                        text = stringResource(R.string.rename),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = LocalCustomColors.current.textThird
                                    )
                                }
                            }

                            // Кнопка видалення
                            Button(
                                onClick = {
                                    coroutineScope.launch { sheetState.hide() }
                                    menuVisible = false
                                    selectedList?.let { list ->
                                        lists = lists.filterNot { it.id == list.id }
                                        deletedList = list
                                        showUndoNotification = true
                                        isDeleteInProgress = true
                                        isDeleteCancelled = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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

                // Діалогове вікно для перейменування
                if (renameSheetVisible) {
                    RenameBottomSheet(
                        listName = selectedList?.name ?: "",
                        onDismiss = { renameSheetVisible = false },
                        onRename = { newName ->
                            renameList(newName)
                            renameSheetVisible = false
                        }
                    )
                }
            }
        }

        // Кнопка для додавання нового списку
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.CreateListScreen.route)
            },
            containerColor = LocalCustomColors.current.btnAddBackground,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp, 18.dp, 18.dp, 64.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.plus),
                contentDescription = stringResource(R.string.new_list),
                tint = LocalCustomColors.current.btnAddText,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.new_list).uppercase())
                },
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = LocalCustomColors.current.btnAddText,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        AnimatedVisibility(
            visible = showUndoNotification,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 110.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(BtnAddBackgroundDark, RoundedCornerShape(10.dp))
                    .padding(vertical = 5.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.item_removed),
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { cancelDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = BtnAddBackgroundDark, contentColor = BlueSky),
                ) {
                    Text(text = stringResource(R.string.cancel).uppercase(), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameBottomSheet(
    listName: String,
    onDismiss: () -> Unit,
    onRename: (String) -> Unit
) {
    var newName by remember { mutableStateOf(listName) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, (-3).dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.rename_list),
                color = LocalCustomColors.current.text,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .border(
                        width = 2.dp,
                        color = LocalCustomColors.current.green,
                        shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp))
                    ),
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = LocalCustomColors.current.inputBackground,
                    focusedBorderColor = LocalCustomColors.current.green,
                    unfocusedBorderColor = LocalCustomColors.current.green,
                    focusedTextColor = LocalCustomColors.current.text,
                    unfocusedTextColor = LocalCustomColors.current.text,
                ),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)),
                placeholder = { Text(text = stringResource(R.string.rename_list)) },
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.cancel).uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Button(
                    onClick = {
                        onRename(newName)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColors.current.btnSaveBackground,
                        contentColor = LocalCustomColors.current.btnSaveText
                    )
                ) {
                    Text(
                        text = stringResource(R.string.save).uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

