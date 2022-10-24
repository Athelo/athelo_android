package com.i2asolutions.athelo.presentation.model.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.i2asolutions.athelo.presentation.ui.theme.lightGray
import com.i2asolutions.athelo.presentation.ui.theme.lightOlivaceous
import com.i2asolutions.athelo.presentation.ui.theme.subtitle

@Composable
fun ChartCloudContent(modifier: Modifier = Modifier, data: Pair<String, String>) {
    Row(modifier = modifier.padding(horizontal = 4.dp)) {
        Text(
            modifier = Modifier.align(CenterVertically),
            text = data.first,
            style = MaterialTheme.typography.subtitle.copy(
                color = lightOlivaceous,
                fontSize = 12.sp,
            )
        )
        Text(
            modifier = Modifier.align(CenterVertically),
            text = " - ",
            style = MaterialTheme.typography.subtitle.copy(
                color = lightGray,
                fontSize = 10.sp
            )
        )
        Text(
            modifier = Modifier.align(CenterVertically),
            text = data.second,
            style = MaterialTheme.typography.subtitle.copy(
                color = lightGray,
                fontSize = 10.sp
            )
        )
    }
}