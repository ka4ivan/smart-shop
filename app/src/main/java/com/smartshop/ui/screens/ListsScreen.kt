package com.smartshop.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartshop.R

@Composable
fun ListsScreen(modifier: Modifier = Modifier) {
    val lists = emptyList<String>() // TODO Отримати список

    Box(modifier = modifier.fillMaxSize()) {
        if (lists.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.empty_list),
                    contentDescription = "Empty List",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 36.dp)
                )
                Text(
                    text = stringResource(R.string.lets_plan_your_shopping),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Text(
                    text = stringResource(R.string.click_on_the_plus_to_create_your_first_list),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.text_secondary),
                    modifier = Modifier.padding(bottom = 64.dp)
                )
            }
        }

        ExtendedFloatingActionButton(
            onClick = { /* TODO Додати логіку для створення нового списку */ },
            containerColor = colorResource(R.color.btn_add_background),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp, 18.dp, 18.dp, 64.dp)
        ) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.plus),
                contentDescription = stringResource(R.string.new_list),
                tint = colorResource(R.color.btn_add_text),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.new_list).uppercase())
                },
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = colorResource(R.color.btn_add_text),
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
