package com.athelohealth.mobile.presentation.ui.patient.symptomRecommendation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.theme.black
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.headline20
import com.athelohealth.mobile.widgets.HtmlText

@Composable
fun RecommendationSymptomScreen(viewModel: RecommendationSymptomViewModel) {
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
fun Content(handleEvent: (RecommendationSymptomEvent) -> Unit, name: String, description: String) {
    Column {
        ToolbarWithNameBack(
            backClick = { handleEvent(RecommendationSymptomEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Recommendation)
        )
        if (name.isNotBlank())
            Text(
                text = stringResource(R.string.General_recommendation_format, name),
                style = MaterialTheme.typography.headline20.copy(
                    color = black,
                    textAlign = TextAlign.Start
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .fillMaxWidth()
            )
        HtmlText(
            text = description,
            style = MaterialTheme.typography.headline20.copy(
                color = gray,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
fun EmptyContentPrev() {
    Box {
        Content(handleEvent = {}, name = "", description = "")
    }
}

@Preview
@Composable
fun ContentPrev() {
    Box {
        Content(handleEvent = {}, name = "Other", description = LoremIpsum(239).toString())
    }
}