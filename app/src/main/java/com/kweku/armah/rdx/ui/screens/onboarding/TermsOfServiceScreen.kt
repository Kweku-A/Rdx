package com.kweku.armah.rdx.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    uiData: OnboardingData,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTermsCheckChange: (Boolean) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.terms_of_service_title)) },
                navigationIcon = {
                    IconButton(onClick = onPreviousClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back_icon_desription
                            )
                        )
                    }
                })
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.terms_of_service_text),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Justify
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.semantics{contentDescription = "checkbox"},
                    checked = uiData.isTermsAgreed,
                    onCheckedChange = { isChecked ->
                        onTermsCheckChange(isChecked)
                    })
                Text(
                    text = stringResource(R.string.agree_to_terms),
                )
            }

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Button(onClick = onPreviousClick, modifier = Modifier.width(150.dp)) {
                    Text(text = stringResource(R.string.previous))
                }

                Button(
                    onClick = onNextClick,
                    enabled = uiData.isTermsAgreed,
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(text = stringResource(R.string.next))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen(
        onPreviousClick = {},
        onNextClick = {},
        onTermsCheckChange = {},
        uiData = OnboardingData()
    )
}