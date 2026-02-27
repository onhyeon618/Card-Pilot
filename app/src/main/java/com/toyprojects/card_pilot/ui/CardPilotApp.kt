package com.toyprojects.card_pilot.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toyprojects.card_pilot.ui.feature.benefit.EditBenefitRoute
import com.toyprojects.card_pilot.ui.feature.card.CardListRoute
import com.toyprojects.card_pilot.ui.feature.card.EditCardRoute
import com.toyprojects.card_pilot.ui.feature.home.BenefitUsageRoute
import com.toyprojects.card_pilot.ui.feature.home.HomeRoute
import com.toyprojects.card_pilot.ui.feature.settings.SettingsScreen
import com.toyprojects.card_pilot.ui.feature.transaction.EditTransactionRoute
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data class BenefitUsage(val benefitId: Long) : Screen()

    @Serializable
    data object CardList : Screen()

    @Serializable
    data class EditCard(val cardId: Long? = null) : Screen()

    @Serializable
    data class EditBenefit(val benefitId: Long? = null) : Screen()

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
                HomeRoute(
                    onBenefitClick = { benefitId ->
                        navController.navigate(Screen.BenefitUsage(benefitId = benefitId))
                    },
                    onAddCardClick = {
                        navController.navigate(Screen.EditCard)
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.Settings)
                    }
                )
            }

            composable<Screen.BenefitUsage> {
                BenefitUsageRoute(
                    onAddTransactionClick = {
                        navController.navigate(Screen.EditTransaction)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Screen.CardList> {
                CardListRoute(
                    onCardClick = { cardId ->
                        navController.navigate(Screen.EditCard(cardId = cardId))
                    },
                    onAddCard = {
                        navController.navigate(Screen.EditCard())
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Screen.EditCard> {
                EditCardRoute(
                    onAddBenefit = {
                        navController.navigate(Screen.EditBenefit())
                    },
                    onEditBenefit = { benefitId ->
                        navController.navigate(Screen.EditBenefit(benefitId = benefitId))
                    },
                    onSave = {
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<Screen.EditBenefit> {
                EditBenefitRoute(
                    onSave = {
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Screen.EditTransaction> {
                EditTransactionRoute(
                    onSave = {
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
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
                        navController.navigate(Screen.EditCard())
                    }
                )
            }
        }
    }
}
