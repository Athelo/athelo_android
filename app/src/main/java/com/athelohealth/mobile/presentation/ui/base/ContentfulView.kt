package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.displayAsString
import com.athelohealth.mobile.presentation.model.community.CommunityData
import com.athelohealth.mobile.presentation.model.news.NewsData
import com.athelohealth.mobile.presentation.ui.theme.black
import com.athelohealth.mobile.presentation.ui.theme.body1
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.headline20
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_SHORT
import com.athelohealth.mobile.utils.contentful.ContentfulClient

@Composable
fun ShowContentfulPage(data: CommunityData) {
    Column {
        // Adds view to Compose
        if (data.body != null) {
            AsyncImage(
                model = data.image,
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.fillMaxSize()
            )
            AndroidView(
                modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                factory = { context ->
                    // Creates view
                    ContentfulClient.getDataView(context, data.body)
                }
            )
        }
    }
}


@Composable
fun ShowContentfulNews(data: NewsData) {
    Column {
        // Adds view to Compose
        if (data.body.content.size > 0) {
            AsyncImage(
                model = data.image,
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.headline20.copy(
                        color = black,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline()
                )
                Text(
                    text = data.date.displayAsString(DATE_FORMAT_SHORT),
                    style = MaterialTheme.typography.body1.copy(
                        color = darkPurple,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.padding(start = 16.dp).alignByBaseline()
                )
            }
            AndroidView(
                modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                factory = { context ->
                    // Creates view
                    ContentfulClient.getDataView(context, data.body)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                model = data.bottomLogo,
                contentDescription = stringResource(id = R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Spacer(modifier = Modifier.height(76.dp))
        }
    }
}