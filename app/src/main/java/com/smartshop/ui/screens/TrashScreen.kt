package com.smartshop.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartshop.R
import com.smartshop.Screen
import com.smartshop.ui.theme.LocalCustomColors

@Composable
fun TrashScreen(navController: NavController, modifier: Modifier = Modifier) {
    val trashLists = emptyList<String>() // TODO Отримати список видалених

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
                    contentDescription = stringResource(R.string.back),
                    tint = LocalCustomColors.current.text,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(3.dp)) // Відступ між кнопкою та текстом

            Text(
                text = stringResource(R.string.trash),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LocalCustomColors.current.text
            )
        }

        if (trashLists.isEmpty()) {
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
        }
    }
}
