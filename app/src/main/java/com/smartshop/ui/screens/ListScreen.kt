package com.smartshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.data.model.ListData
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel

@Composable
fun ListScreen(navController: NavController, viewModel: ListViewModel, listId: String, modifier: Modifier = Modifier) {
    val listData = remember { mutableStateOf<ListData?>(null) }
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(listId) {
        listData.value = viewModel.showList(listId.toString())
    }

    val list = listData.value

    Box(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
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

            Spacer(modifier = Modifier.width(3.dp))

            if (list != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = list.name,
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
            }
        }
    }
}
