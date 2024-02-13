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
import androidx.compose.material.icons.filled.Pin
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.OnboardingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPinScreen(
    uiData: OnboardingData,
    onPreviousClick: () -> Unit,
    onFinishClick: () -> Unit,
    onPinChange: (String) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.new_pin)) },
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
    }) {
        val localFocusManager = LocalFocusManager.current
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
                value = uiData.confirmPin,
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .padding(8.dp),
                onValueChange = onPinChange,
                maxLines = 1,
                label = { Text(text = stringResource(R.string.confirm_pin)) },
                shape = RoundedCornerShape(4.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Pin,
                        contentDescription = stringResource(R.string.confirm_pin),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
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

                Button(
                    onClick = onFinishClick,
                    enabled = (uiData.confirmPin.isNotEmpty()),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(text = stringResource(R.string.finish))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmPinScreenPreview() {
    ConfirmPinScreen(
        onPreviousClick = {},
        onFinishClick = {},
        onPinChange = {},
        uiData = OnboardingData()
    )
}