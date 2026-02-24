package com.toyprojects.card_pilot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toyprojects.card_pilot.ui.feature.benefit.EditBenefitScreen
import com.toyprojects.card_pilot.ui.feature.card.CardListScreen
import com.toyprojects.card_pilot.ui.feature.card.EditCardScreen
import com.toyprojects.card_pilot.ui.feature.home.BenefitUsageScreen
import com.toyprojects.card_pilot.ui.feature.home.HomeScreen
import com.toyprojects.card_pilot.ui.feature.settings.SettingsScreen
import com.toyprojects.card_pilot.ui.feature.transaction.EditTransactionScreen
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object BenefitUsage : Screen()

    @Serializable
    data object EditBenefit : Screen()

    @Serializable
    data object CardList : Screen()

    @Serializable
    data object EditCard : Screen()

    @Serializable
    data object EditTransaction : Screen()

    @Serializable
    data object Settings : Screen()
}

@Composable
fun CardPilotApp() {
    CardPilotTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.Home) {
            composable<Screen.Home> {
                HomeScreen(
                    onSettingsClick = {
                        navController.navigate(Screen.Settings)
                    },
                    onBenefitClick = {
                        navController.navigate(Screen.BenefitUsage)
                    }
                )
            }

            composable<Screen.Settings> {
                SettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onCardListClick = {
                        navController.navigate(Screen.CardList)
                    },
                    onAddCardClick = {
                        navController.navigate(Screen.EditCard)
                    }
                )
            }

            composable<Screen.CardList> {
                CardListScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddCard = {
                        navController.navigate(Screen.EditCard)
                    }
                )
            }

            composable<Screen.EditCard> {
                EditCardScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        navController.popBackStack()
                    },
                    onEditBenefit = { _, _, _ ->
                        navController.navigate(Screen.EditBenefit)
                    }
                )
            }

            composable<Screen.EditTransaction> {
                EditTransactionScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Screen.BenefitUsage> {
                BenefitUsageScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddTransactionClick = {
                        navController.navigate(Screen.EditTransaction)
                    }
                )
            }

            composable<Screen.EditBenefit> {
                EditBenefitScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = { _, _, _, _, _ ->
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
