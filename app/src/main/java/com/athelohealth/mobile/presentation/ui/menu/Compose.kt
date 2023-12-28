@file:OptIn(ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.menu.MenuItem
import com.athelohealth.mobile.presentation.ui.base.CircleAvatarImage
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun MenuScreen(viewModel: MenuViewModel) {
    Column(Modifier.background(menuBackground)) {
        val state = viewModel.viewState.collectAsState()
        UserSection(
            displayName = state.value.displayName,
            avatar = state.value.image,
            onCloseClick = {
                viewModel.handleEvent(MenuEvent.CloseClick)
            })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(top = 38.dp)
        ) {
            items(state.value.items) {
                MenuItemCell(viewModel, it)
                Divider(color = gray.copy(alpha= 0.2f), thickness = 1.dp)

            }
        }
    }
}

@Composable
private fun MenuItemCell(
    viewModel: MenuViewModel,
    it: MenuItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                viewModel.handleEvent(MenuEvent.ItemClick(it.deeplink))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = it.icon),
            contentDescription = it.title,
            modifier = Modifier
                .size(56.dp)
                .padding(start = 16.dp, end = 16.dp)
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = it.title,
            style = MaterialTheme.typography.button.copy(color = gray),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun UserSection(displayName: String, avatar: String?, onCloseClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(277f / 193f)
            .background(lightPurple)
            .statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.ic_menu_user_bg),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(id = R.drawable.fi_sr_cross_small),
            contentDescription = "",
            modifier = Modifier
                .size(56.dp)
                .padding(16.dp)
                .align(Alignment.TopStart)
                .clickable {
                    onCloseClick()
                },
            contentScale = ContentScale.Fit
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomStart),
            verticalAlignment = CenterVertically,
        ) {
            CircleAvatarImage(
                avatar = avatar,
                displayName = displayName,
            )
            Text(
                text = displayName,
                style = MaterialTheme.typography.headline20.copy(
                    textAlign = TextAlign.Start,
                    color = white
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
