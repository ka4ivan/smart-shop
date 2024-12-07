package com.smartshop.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.ui.theme.LocalCustomColors
import com.smartshop.ui.viewmodel.ListViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.smartshop.data.utils.UserUtils

@Composable
fun TrashScreen(navController: NavController, viewModel: ListViewModel, modifier: Modifier = Modifier) {
    val trashLists by viewModel.lists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val userId = UserUtils.getUserId(context)

    LaunchedEffect(Unit) {
        viewModel.loadDeletedLists(userId)
    }

    Box(modifier = modifier.fillMaxSize()) {
        // UI для кнопки назад і заголовка
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
                    contentDescription = stringResource(R.string.back),
                    tint = LocalCustomColors.current.text,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = stringResource(R.string.trash),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LocalCustomColors.current.text
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = LocalCustomColors.current.green,
                    )
                }
            }

            trashLists.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empty_trash),
                        contentDescription = "Empty Trash",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 36.dp)
                    )
                    Text(
                        text = stringResource(R.string.no_deleted_lists),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalCustomColors.current.text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(start = 16.dp, top = 65.dp, end = 16.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    LazyColumn {
                        items(trashLists) { list ->
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
                                // Назва списку
                                Text(
                                    text = list.name,
                                    color = LocalCustomColors.current.text,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(end = 25.dp, top = 10.dp),
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = stringResource(R.string.delete).uppercase(),
                                        color = Color.Red,
                                        modifier = Modifier
                                            .clickable {
                                                viewModel.permanentlyDeleteItem(list.id)
                                                viewModel.loadDeletedLists(userId = userId)
                                            }
                                            .padding(end = 16.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = stringResource(R.string.restore).uppercase(),
                                        color = LocalCustomColors.current.green,
                                        modifier = Modifier
                                            .clickable {
                                                viewModel.removeFromTrash(list.id)
                                                viewModel.loadDeletedLists(userId = userId)
                                            },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}