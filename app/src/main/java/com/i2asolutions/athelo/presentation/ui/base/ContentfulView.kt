package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.community.CommunityData
import com.i2asolutions.athelo.presentation.model.news.NewsData
import com.i2asolutions.athelo.utils.contentful.ContentfulClient

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