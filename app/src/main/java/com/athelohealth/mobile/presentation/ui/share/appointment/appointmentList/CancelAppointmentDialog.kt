@file:OptIn(ExperimentalComposeUiApi::class)

package com.athelohealth.mobile.presentation.ui.share.appointment.appointmentList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.red
import com.athelohealth.mobile.presentation.ui.theme.typography
import com.athelohealth.mobile.presentation.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CancelAppointmentDialog(
    title: String,
    message: String,
    negativeBtnText: String,
    negativeBtnTextColor: Color,
    positiveBtnText: String,
    positiveBtnTextColor: Color,
    positiveBtnBgColor: Color,
    onDialogDismiss: () -> Unit,
    onNegativeBtnClicked: () -> Unit,
    onPositiveBtnClicked: () -> Unit
) {

    Dialog(
        onDismissRequest = { onDialogDismiss() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = white
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(0.90f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.athelo_logo_with_text),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    contentScale = ContentScale.FillHeight
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 8.dp),
                    text = title,
                    style = typography.headlineSmall.copy(color = darkPurple),
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    text = message,
                    style = typography.labelMedium.copy(color = darkPurple),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        onClick = {
                            onNegativeBtnClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .width(90.dp)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(24.dp)),
                        border = BorderStroke(width = 1.dp, color = darkPurple),
                        shape = CircleShape
                    ) {
                        Text(
                            text = negativeBtnText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = negativeBtnTextColor,
                        )
                    }

                    Divider(modifier = Modifier.weight(1f), color = Color.White)

                    Button(
                        onClick = {
                            onPositiveBtnClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = positiveBtnBgColor
                        ),
                        modifier = Modifier
                            .width(90.dp)
                            .wrapContentHeight()
                            .clip(shape = RoundedCornerShape(24.dp)),
                        shape = CircleShape
                    ) {
                        Text(
                            text = positiveBtnText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = positiveBtnTextColor,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreView() {
    CancelAppointmentDialog(
        title = "Cancel appointment",
        message = "Are you sure you want to cancel appointment?",
        negativeBtnText = "No",
        negativeBtnTextColor = darkPurple,
        positiveBtnText = "Yes",
        positiveBtnTextColor = white,
        positiveBtnBgColor = red,
        onDialogDismiss = {},
        onNegativeBtnClicked = {},
        onPositiveBtnClicked = {}
    )
}