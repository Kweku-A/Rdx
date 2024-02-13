package com.kweku.armah.rdx.ui.screens.onboarding

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    uiState: OnboardingData,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Personal Info") },
                navigationIcon = {
                    IconButton(onClick = onPreviousClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back_icon_desription
                            )
                        )
                    }
                },
            )
        }
    ) {
        val localFocusManager = LocalFocusManager.current
        val lastNameFocusRequester = FocusRequester()
        val phoneNumberFocusRequester = FocusRequester()
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = uiState.firstName,
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .padding(8.dp),
                onValueChange = onFirstNameChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.first_name)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onNext = { lastNameFocusRequester.requestFocus() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.first_name),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
            )

            OutlinedTextField(
                value = uiState.lastName,
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .padding(8.dp)
                    .focusRequester(lastNameFocusRequester),
                onValueChange = onLastNameChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.last_name)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onNext = { phoneNumberFocusRequester.requestFocus() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.last_name),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
            )

            OutlinedTextField(
                value = uiState.phoneNumber,
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .padding(8.dp)
                    .focusRequester(phoneNumberFocusRequester),
                onValueChange = onPhoneNumberChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.telephone)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = stringResource(R.string.telephone),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = !uiState.isPhoneNumberValid
            )

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

                val isEnabled =
                    (uiState.firstName.isNotEmpty() && uiState.lastName.isNotEmpty() && uiState.phoneNumber.isNotEmpty() && uiState.isPhoneNumberValid)

                Button(
                    onClick = onNextClick,
                    enabled = isEnabled,
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
fun PersonalInfoScreenPreview() {
    PersonalInfoScreen(
        onPreviousClick = {},
        onNextClick = {},
        onFirstNameChange = {},
        onLastNameChange = {},
        onPhoneNumberChange = {},
        uiState = OnboardingData()
    )
}