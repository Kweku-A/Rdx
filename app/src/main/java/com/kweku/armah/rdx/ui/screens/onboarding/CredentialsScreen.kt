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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialsScreen(
    uiState: OnboardingData,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.credentials)) },
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
        val passwordFocusRequester = FocusRequester()
        val showPassword = remember { mutableStateOf(false) }
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
                value = uiState.email,
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .padding(8.dp),
                onValueChange = onEmailChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.email)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(R.string.email),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = !uiState.isEmailValid
            )


            OutlinedTextField(
                value = uiState.password,
                modifier = Modifier
                    .widthIn(min = 500.dp)
                    .padding(8.dp)
                    .focusRequester(passwordFocusRequester),
                onValueChange = onPasswordChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.password)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = stringResource(R.string.password),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                visualTransformation =
                if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword.value) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(onClick = {
                        showPassword.value = !showPassword.value
                    }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.password_visibility),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
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
                    (uiState.password.isNotEmpty() && uiState.email.isNotEmpty() && uiState.isEmailValid)

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
fun CredentialsScreenPreview() {
    CredentialsScreen(
        onPreviousClick = {},
        onNextClick = {},
        onPasswordChange = {},
        onEmailChange = {},
        uiState = OnboardingData()
    )
}