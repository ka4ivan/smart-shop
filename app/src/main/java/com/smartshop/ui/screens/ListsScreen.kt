package com.smartshop.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.smartshop.data.utils.UserUtils
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ListsScreen(navController: NavController, viewModel: ListViewModel, modifier: Modifier = Modifier) {

    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userId = UserUtils.getUserId(context)

    // TODO доробити отримання даних
    LaunchedEffect(userId) {
        viewModel.getLists(userId)
    }

    val lists by viewModel.lists.collectAsState()

    Log.d("ListsScreen", "List data: $lists")

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
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.wrapContentSize(Alignment.TopEnd)
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
                                    tint = LocalContentColor.current
                                )
                                Text(
                                    text = stringResource(R.string.trash),
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
                        }
                    )
                }
            }
        }

        if (lists.isEmpty()) {
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
