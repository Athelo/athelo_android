@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.myProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.phoneMask
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.profile.ProfileItems
import com.athelohealth.mobile.presentation.ui.base.*
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun MyProfileScreen(viewModel: MyProfileViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Toolbar(
                showBack = true,
                screenName = stringResource(id = R.string.My_Profile),
                onBackClick = {
                    viewModel.handleEvent(MyProfileEvent.GoBackClick)
                })
            LazyColumn {
                item {
                    UserCell(
                        onEditClick = { viewModel.handleEvent(MyProfileEvent.EditMyProfileClick) },
                        viewState.value.user
                    )
                }
                items(viewState.value.buttons, key = { it.hashCode() }) { item ->
                    when (item) {
                        is ProfileItems.Header -> HeaderLabel(
                            text = stringResource(id = item.text),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        is ProfileItems.Button -> Button(
                            item,
                            modifier = Modifier
                                .align(Alignment.Start),
                        ) {
                            viewModel.handleEvent(MyProfileEvent.ButtonClick(item))
                        }
                        is ProfileItems.DeleteButton -> DeleteButton(
                            item,
                            modifier = Modifier
                                .align(Alignment.Start),
                        ) {
                            viewModel.handleEvent(MyProfileEvent.DeleteButtonClick)
                        }
                    }
                }
            }
        }
        if (viewState.value.showLogoutOutPopup) {
            ConfirmPopup(
                title = stringResource(id = R.string.Log_out),
                description = stringResource(id = R.string.Log_Out_message),
                onConfirmClick = {
                    viewModel.handleEvent(MyProfileEvent.LogoutUserConfirmed)
                },
                onCancelClick = {
                    viewModel.handleEvent(MyProfileEvent.PopupCancelButtonClick)
                })
        }
        if (viewState.value.showDeletePopup) {
            DeletePopup(
                onConfirmClick = { viewModel.handleEvent(MyProfileEvent.DeleteUserConfirmed) },
                onCancelClick = {
                    viewModel.handleEvent(MyProfileEvent.PopupCancelButtonClick)
                },
                title = stringResource(id = R.string.Delete_an_account),
                description = stringResource(id = R.string.Delete_an_account_message)
            )
        }
    }
}

@Composable
fun UserCell(onEditClick: () -> Unit, user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp, top = 8.dp),
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(size = 30.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            RoundedCornerAvatarImage(
                avatar = user.photo?.image100100,
                displayName = user.formattedDisplayName
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = user.displayName ?: "",
                    style = MaterialTheme.typography.subHeading.copy(color = gray),
                    modifier = Modifier.fillMaxWidth()
                )
//                if (!user.birthday.isNullOrBlank())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 4.dp, top = 4.dp)
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendar icon"
                    )
                    Text(
                        text = if (user.formattedBirthdate.isNullOrBlank()) stringResource(id = R.string.No_Information) else user.formattedBirthdate
                            ?: stringResource(id = R.string.No_Information),
                        style = MaterialTheme.typography.body1.copy(color = if (user.phone.isNullOrBlank()) lightGray else gray),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
//                if (!user.phone.isNullOrBlank())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 4.dp, top = 4.dp)
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.ic_phone),
                        contentDescription = "Calendar icon"
                    )
                    Text(
                        text = if (user.phone.isNullOrBlank()) stringResource(id = R.string.No_Information) else user.phone.phoneMask(),
                        style = MaterialTheme.typography.body1.copy(color = if (user.phone.isNullOrBlank()) lightGray else gray),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onEditClick()
                    })
        }
    }
}

@Composable
fun Button(
    item: ProfileItems.Button,
    modifier: Modifier,
    labelColor: Color = gray,
    onItemClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(bottom = 5.dp, top = 1.dp)
            .padding(horizontal = 1.dp)
            .clickable {
                onItemClick()
            }
            .fillMaxWidth()
            .height(72.dp),
        colors = CardDefaults.cardColors(white),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = item.icon),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
            )
            Text(
                text = stringResource(id = item.label),
                style = MaterialTheme.typography.button.copy(color = labelColor),
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

@Composable
fun DeleteButton(
    item: ProfileItems.DeleteButton,
    modifier: Modifier,
    labelColor: Color = red,
    onItemClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(bottom = 5.dp, top = 1.dp)
            .padding(horizontal = 1.dp)
            .clickable {
                onItemClick()
            }
            .fillMaxWidth()
            .height(72.dp),
        colors = CardDefaults.cardColors(white),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = item.icon),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
            )
            Text(
                text = stringResource(id = item.label),
                style = MaterialTheme.typography.button.copy(color = labelColor),
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

@Composable
fun HeaderLabel(text: String, modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = text,
        style = MaterialTheme.typography.headline20.copy(color = gray, textAlign = TextAlign.Start),
    )
}
