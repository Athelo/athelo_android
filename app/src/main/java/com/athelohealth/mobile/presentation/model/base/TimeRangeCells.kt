package com.athelohealth.mobile.presentation.model.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun InfoCell(info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
    ) {
        Image(
            modifier = Modifier.padding(top = 4.dp),
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = info,
            style = MaterialTheme.typography.paragraph.copy(color = gray),
            textAlign = TextAlign.Justify,
        )
    }
}

@Composable
fun PeriodInfoCell(
    periodInfo: SleepDetailScreen.PeriodInfo,
    prevClick: () -> Unit,
    nextClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = periodInfo.date,
                style = MaterialTheme.typography.body1.copy(color = gray)
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = periodInfo.rangeName,
                style = MaterialTheme.typography.headline20.copy(color = black)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_prev),
            colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (periodInfo.canGoBack) 1.0f else 0.5f)),
            contentDescription = "Arrow Previous",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (periodInfo.canGoBack) white else white.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { prevClick() },
        )

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_next),
            colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (periodInfo.canGoForward) 1.0f else 0.5f)),
            contentDescription = "Arrow Next",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (periodInfo.canGoForward) white else white.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { nextClick() },
        )
    }
}
