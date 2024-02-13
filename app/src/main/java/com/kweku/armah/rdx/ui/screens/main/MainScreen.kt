package com.kweku.armah.rdx.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kweku.armah.rdx.R
import com.kweku.armah.rdx.domain.model.UserInfo
import com.kweku.armah.rdx.domain.wrapper.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: UiState<UserInfo>,
    navigateToOnboarding: () -> Unit,
    onLogout: () -> Unit
) {
    when (uiState.isLoading) {
        true -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }

        false -> {
            if (uiState.uiData.firstName.isNotEmpty()) {

                var showLogoutDialog by rememberSaveable {
                    mutableStateOf(false)
                }

                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.dashboard)) },
                        actions = {
                            IconButton(onClick = {
                                showLogoutDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.PowerSettingsNew,
                                    contentDescription = stringResource(R.string.logout)
                                )
                            }
                        },
                    )
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (showLogoutDialog) {
                            AppDialog(
                                header = stringResource(R.string.logging_out),
                                dialogMessage = stringResource(R.string.are_you_sure_you_want_to_log_out),
                                onNegativeClick = {
                                    showLogoutDialog = false
                                },
                                onPositiveClick = {
                                    showLogoutDialog = false
                                    onLogout()
                                },
                                negativeButtonText = stringResource(R.string.cancel),
                                positiveButtonText =stringResource(R.string.logout),
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .drawBehind {
                                        drawCircle(
                                            color = color,
                                            radius = this.size.maxDimension
                                        )
                                    },
                                text = uiState.uiData.firstName.first().toString()
                                    .uppercase(),
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 20.sp
                                )
                            )

                            Text(
                                stringResource(
                                    R.string.welcome_user,
                                    uiState.uiData.firstName,
                                    uiState.uiData.lastName
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = TextUnit(value = 20f, type = TextUnitType.Sp)
                                ),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        var showDetails by rememberSaveable {
                            mutableStateOf(false)
                        }

                        OutlinedButton(onClick = { showDetails = !showDetails }) {
                            Text(text = "Click to additional info")
                        }

                        AnimatedVisibility(visible = showDetails) {
                            OutlinedCard(modifier = Modifier.width(350.dp)) {
                                val textStyle = TextStyle(
                                    fontSize = TextUnit(
                                        value = 18f,
                                        type = TextUnitType.Sp
                                    ),
                                    textAlign = TextAlign.Start
                                )

                                val rowModifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                    )
                                    .padding(8.dp)
                                Row(
                                    modifier = rowModifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Text(
                                        text = "Telephone:",
                                        style = textStyle,
                                    )
                                    Text(
                                        text = uiState.uiData.phoneNumber,
                                        modifier = Modifier.padding(start = 6.dp),
                                        style = textStyle,
                                    )
                                }

                                Row(
                                    modifier = rowModifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Text(
                                        text = "Email:",
                                        style = textStyle,
                                    )
                                    Text(
                                        text = uiState.uiData.email,
                                        modifier = Modifier.padding(start = 6.dp),
                                        style = textStyle,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                navigateToOnboarding()
            }
        }
    }
}

@Composable
fun AppDialog(
    header: String,
    dialogMessage: String = "",
    negativeButtonText: String = "",
    onNegativeClick: () -> Unit = {},
    positiveButtonText: String = "Ok",
    onPositiveClick: () -> Unit = {},
) {
    Dialog(onDismissRequest = { }, properties = DialogProperties()) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ) {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    text = header,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    text = dialogMessage,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(),
                    horizontalArrangement = Arrangement.End,
                ) {

                    TextButton(onClick = onNegativeClick, modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = negativeButtonText,
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 1f),
                        )
                    }

                    TextButton(onClick = onPositiveClick, modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = positiveButtonText,
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 1f),
                        )
                    }
                }
            }

        }
    }
}