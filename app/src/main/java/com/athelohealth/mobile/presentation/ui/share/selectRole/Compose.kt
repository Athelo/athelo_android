@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.selectRole

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BackButton
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ImageToolbar
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun SelectRoleScreen(viewModel: SelectRoleViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val showBack = remember(viewState.showBackButton) {
        viewState.showBackButton
    }
    BoxScreen(viewModel = viewModel, showProgressProvider = { viewState.isLoading }) {
        Image(
            painter = painterResource(id = R.drawable.frame_1640),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
        Content(handleEvent = viewModel::handleEvent, showBack = showBack)
    }
}

@Composable
fun Content(handleEvent: (SelectRoleEvent) -> Unit, showBack: Boolean) {
    Column(Modifier.fillMaxSize()) {
        ImageToolbar(leftButton = {
            if (showBack) {
                BackButton(modifier = Modifier) {
                    handleEvent(SelectRoleEvent.BackButtonClick)
                }
            }
        })
        Text(
            text = stringResource(id = R.string.Select_role_message),
            style = MaterialTheme.typography.normalText.copy(
                color = purple, textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { handleEvent(SelectRoleEvent.ActAsPatientClick) },
            label = stringResource(
                id = R.string.Act_as_a_patient
            ),
            icon = R.drawable.ic_human
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { handleEvent(SelectRoleEvent.ActAsCaregiverClick) },
            label = stringResource(
                id = R.string.Act_as_a_caregiver
            ),
            icon = R.drawable.caregiver
        )
    }
}

@Composable
private fun Button(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String,
    icon: Int,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(white),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            }) {
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .alpha(0.2f),
                painter = painterResource(id = R.drawable.mask_group),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                )
                androidx.compose.material.Text(
                    text = label,
                    style = MaterialTheme.typography.body1.copy(color = gray),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.ic_arrow_gray),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}