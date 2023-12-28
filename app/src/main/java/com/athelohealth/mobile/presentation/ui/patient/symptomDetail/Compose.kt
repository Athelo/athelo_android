package com.athelohealth.mobile.presentation.ui.patient.symptomDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.SecondaryWithImageButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.paragraph
import com.athelohealth.mobile.widgets.HtmlText

@Composable
fun MySymptomDetailsScreen(viewModel: SymptomDetailsViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            name = state.name,
            description = state.description
        )
    }
}

@Composable
private fun BoxScope.Content(
    handleEvent: (SymptomDetailsEvent) -> Unit,
    name: String,
    description: String
) {
    Column(Modifier.navigationBarsPadding()) {
        ToolbarWithNameBack(
            backClick = { handleEvent(SymptomDetailsEvent.BackButtonClick) },
            screenName = name
        )
        HtmlText(
            text = description,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                ),
            style = MaterialTheme.typography.paragraph.copy(gray, textAlign = TextAlign.Justify),
        )
    }
    SecondaryWithImageButton(
        imageRes = R.drawable.fi_sr_time_fast,
        textRes = R.string.Symptom_Chronology,
        onClick = { handleEvent(SymptomDetailsEvent.ChronologyClick) },
        background = background,
        border = darkPurple,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp)
            .navigationBarsPadding()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    )
}

@Preview
@Composable
private fun ContentPreview() {
    Box {
        Content(handleEvent = {}, "Test Symptom", LoremIpsum(29).toString())
    }
}