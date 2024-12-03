package com.smartshop.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel

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
                color = LocalCustomColors.current.text
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
                                    onClick = { /* Обробка натискання меню */ },
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
    }
}
