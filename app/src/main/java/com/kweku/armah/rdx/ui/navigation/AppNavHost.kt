package com.kweku.armah.rdx.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.kweku.armah.rdx.ui.screens.main.MainScreen
import com.kweku.armah.rdx.ui.screens.destinations.MainScreenDestinations
import com.kweku.armah.rdx.ui.screens.main.viewmodel.MainViewModel
import com.kweku.armah.rdx.ui.screens.onboarding.ConfirmPinScreen
import com.kweku.armah.rdx.ui.screens.onboarding.CredentialsScreen
import com.kweku.armah.rdx.ui.screens.onboarding.NewPinScreen
import com.kweku.armah.rdx.ui.screens.onboarding.PersonalInfoScreen
import com.kweku.armah.rdx.ui.screens.onboarding.TermsOfServiceScreen
import com.kweku.armah.rdx.ui.screens.onboarding.WelcomeScreen
import com.kweku.armah.rdx.ui.screens.onboarding.destinations.OnboardingScreenDestinations
import com.kweku.armah.rdx.ui.screens.onboarding.viewmodel.OnboardingViewModel


@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navigateToDestinationAndClearStack: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = MainScreenDestinations.Main.toString(),
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }
    ) {

        composable(route = MainScreenDestinations.Main.toString()) {
            val mainViewModel: MainViewModel = hiltViewModel()
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            MainScreen(uiState = uiState, onLogout = {
                mainViewModel.onLogout()
                navigateToDestinationAndClearStack(MainScreenDestinations.Onboarding.toString())
            }, navigateToOnboarding = {
                navigateToDestinationAndClearStack(MainScreenDestinations.Onboarding.toString())
            })

            LaunchedEffect(Unit) {
                mainViewModel.getUserLogin()
            }
        }

        navigation(
            startDestination = OnboardingScreenDestinations.Welcome.toString(),
            route = MainScreenDestinations.Onboarding.toString()
        ) {

            composable(route = OnboardingScreenDestinations.Welcome.toString()) {
                WelcomeScreen(onNextClick = {
                    navController.navigate(OnboardingScreenDestinations.TermsOfService.toString())
                })
            }

            composable(route = OnboardingScreenDestinations.TermsOfService.toString()) {
                val onboardingViewModel =
                    it.sharedViewModel<OnboardingViewModel>(navController = navController)

                val state = onboardingViewModel.uiState.collectAsStateWithLifecycle()

                TermsOfServiceScreen(
                    onPreviousClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(OnboardingScreenDestinations.Credentials.toString())
                    },
                    onTermsCheckChange = { isChecked ->
                        onboardingViewModel.onTermsAgreed(isChecked)
                    },
                    uiData = state.value
                )
            }

            composable(route = OnboardingScreenDestinations.Credentials.toString()) {
                val onboardingViewModel =
                    it.sharedViewModel<OnboardingViewModel>(navController = navController)

                val state = onboardingViewModel.uiState.collectAsStateWithLifecycle()

                CredentialsScreen(
                    onEmailChange = { email ->
                        onboardingViewModel.onEmailNameChange(email = email)
                    },
                    onPasswordChange = { password ->
                        onboardingViewModel.onPasswordChange(password = password)

                    },
                    onPreviousClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(OnboardingScreenDestinations.PersonalInfo.toString())
                    },
                    uiState = state.value
                )
            }

            composable(route = OnboardingScreenDestinations.PersonalInfo.toString()) {
                val onboardingViewModel =
                    it.sharedViewModel<OnboardingViewModel>(navController = navController)
                val state = onboardingViewModel.uiState.collectAsStateWithLifecycle()

                PersonalInfoScreen(
                    onFirstNameChange = { firstName ->
                        onboardingViewModel.onFirstNameChange(firstName = firstName)
                    },
                    onLastNameChange = { lastName ->
                        onboardingViewModel.onLastNameChange(lastName = lastName)
                    },
                    onPhoneNumberChange = { phoneNumber ->
                        onboardingViewModel.onPhoneNumberChange(phoneNumber = phoneNumber)

                    },
                    onPreviousClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(OnboardingScreenDestinations.NewPin.toString())
                    },
                    uiState = state.value
                )
            }

            composable(route = OnboardingScreenDestinations.NewPin.toString()) {
                val onboardingViewModel =
                    it.sharedViewModel<OnboardingViewModel>(navController = navController)

                val state = onboardingViewModel.uiState.collectAsStateWithLifecycle()

                NewPinScreen(
                    onPinChange = { pin ->
                        onboardingViewModel.onPinChange(pin = pin)
                    },
                    onPreviousClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(OnboardingScreenDestinations.ConfirmPin.toString())
                    },
                    uiState = state.value
                )
            }

            composable(route = OnboardingScreenDestinations.ConfirmPin.toString()) {
                val onboardingViewModel =
                    it.sharedViewModel<OnboardingViewModel>(navController = navController)

                val state = onboardingViewModel.uiState.collectAsStateWithLifecycle()

                if (state.value.onFinishNavigationRoute.isNotEmpty()) {
                    navigateToDestinationAndClearStack(state.value.onFinishNavigationRoute)
                }

                ConfirmPinScreen(
                    onPinChange = { pin ->
                        onboardingViewModel.onConfirmPin(pin = pin)
                    },
                    onPreviousClick = {
                        navController.popBackStack()
                    },
                    onFinishClick = {
                        onboardingViewModel.onFinishClicked()
                    },
                    uiState = state.value
                )
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}