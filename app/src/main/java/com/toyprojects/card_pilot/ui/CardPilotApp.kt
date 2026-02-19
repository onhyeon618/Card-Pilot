package com.toyprojects.card_pilot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toyprojects.card_pilot.ui.screens.AddCardScreen
import com.toyprojects.card_pilot.ui.screens.AddTransactionScreen
import com.toyprojects.card_pilot.ui.screens.BenefitDetailScreen
import com.toyprojects.card_pilot.ui.screens.BenefitEditScreen
import com.toyprojects.card_pilot.ui.screens.CardListScreen
import com.toyprojects.card_pilot.ui.screens.HomeScreen
import com.toyprojects.card_pilot.ui.screens.SettingsScreen
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object BenefitDetail : Screen()

    @Serializable
    data object BenefitEdit : Screen()

    @Serializable
    data object CardList : Screen()

    @Serializable
    data object AddCard : Screen()

    @Serializable
    data object AddTransaction : Screen()

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
                    onAddBenefitClick = {
                        navController.navigate(Screen.AddCard)
                    },
                    onBenefitClick = { benefit ->
                        navController.navigate(Screen.BenefitDetail)
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
                        navController.navigate(Screen.AddCard)
                    }
                )
            }
            composable<Screen.CardList> {
                CardListScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onCardClick = {
                        navController.navigate(Screen.AddCard)
                    },
                    onAddCard = {
                        navController.navigate(Screen.AddCard)
                    }
                )
            }
            composable<Screen.AddCard> {
                AddCardScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        navController.popBackStack()
                    },
                    onEditBenefit = { index, name, amount ->
                        navController.navigate(Screen.BenefitEdit)
                    }
                )
            }
            composable<Screen.AddTransaction> {
                AddTransactionScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSave = {
                        navController.popBackStack()
                    }
                )
            }
            composable<Screen.BenefitDetail> {
                BenefitDetailScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddTransactionClick = {
                        navController.navigate(Screen.AddTransaction)
                    }
                )
            }
            composable<Screen.BenefitEdit> { backStackEntry ->
                BenefitEditScreen(
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
