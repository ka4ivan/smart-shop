package com.smartshop.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartshop.R

@Composable
fun ListsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Lists Screen")
        }

        ExtendedFloatingActionButton(
            onClick = { /* TODO Додати логіку для створення нового списку */ },
            containerColor = colorResource(R.color.blue),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp, 18.dp, 18.dp, 64.dp)
        ) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.plus),
                contentDescription = stringResource(R.string.new_list),
                tint = colorResource(R.color.full_white),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.new_list).uppercase())
                },
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = colorResource(R.color.full_white),
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
