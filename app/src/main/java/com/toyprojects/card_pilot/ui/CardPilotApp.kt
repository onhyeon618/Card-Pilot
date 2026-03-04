package com.toyprojects.card_pilot.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.ui.feature.benefit.EditBenefitRoute
import com.toyprojects.card_pilot.ui.feature.card.CardListRoute
import com.toyprojects.card_pilot.ui.feature.card.EditCardRoute
import com.toyprojects.card_pilot.ui.feature.home.BenefitUsageRoute
import com.toyprojects.card_pilot.ui.feature.home.HomeRoute
import com.toyprojects.card_pilot.ui.feature.settings.SettingsScreen
import com.toyprojects.card_pilot.ui.feature.transaction.EditTransactionRoute
import com.toyprojects.card_pilot.ui.navigation.BenefitPropertyType
import com.toyprojects.card_pilot.ui.navigation.BenefitResult
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed class Screen {
    companion object {
        const val RESULT_KEY_BENEFIT = "benefit_result"
    }

    @Serializable
    data object Home : Screen()

    @Serializable
    data class BenefitUsage(val cardId: Long, val benefitId: Long) : Screen()

    @Serializable
    data object CardList : Screen()

    @Serializable
    data class EditCard(val cardId: Long? = null) : Screen()

    @Serializable
    data class EditBenefit(
        val benefitProperty: BenefitProperty? = null,
        val index: Int = -1,
    ) : Screen()

    @Serializable
    data class EditTransaction(
        val transactionId: Long? = null,
        val initialCardId: Long,
        val initialBenefitId: Long
    ) : Screen()

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
                    onBenefitClick = { cardId, benefitId ->
                        navController.navigate(Screen.BenefitUsage(cardId = cardId, benefitId = benefitId))
                    },
                    onAddCardClick = {
                        navController.navigate(Screen.EditCard())
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.Settings)
                    }
                )
            }

            composable<Screen.BenefitUsage> {
                BenefitUsageRoute(
                    onAddTransactionClick = { cardId, benefitId ->
                        navController.navigate(
                            Screen.EditTransaction(
                                initialCardId = cardId,
                                initialBenefitId = benefitId
                            )
                        )
                    },
                    onEditTransactionClick = { transactionId, cardId, benefitId ->
                        navController.navigate(
                            Screen.EditTransaction(
                                transactionId = transactionId,
                                initialCardId = cardId,
                                initialBenefitId = benefitId
                            )
                        )
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

            composable<Screen.EditCard> { navBackStackEntry ->
                val benefitResult by navBackStackEntry.savedStateHandle
                    .getStateFlow<BenefitResult?>(Screen.RESULT_KEY_BENEFIT, null)
                    .collectAsStateWithLifecycle()

                EditCardRoute(
                    benefitResult = benefitResult,
                    onAddBenefit = {
                        navController.navigate(Screen.EditBenefit())
                    },
                    onEditBenefit = { benefitProperty, index ->
                        navController.navigate(
                            Screen.EditBenefit(benefitProperty = benefitProperty, index = index)
                        )
                    },
                    onBenefitResultConsumed = {
                        navBackStackEntry.savedStateHandle.remove<BenefitResult>(Screen.RESULT_KEY_BENEFIT)
                    },
                    onSave = {
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<Screen.EditBenefit>(
                typeMap = mapOf(typeOf<BenefitProperty?>() to BenefitPropertyType)
            ) {
                EditBenefitRoute(
                    onSave = { benefit, index ->
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            Screen.RESULT_KEY_BENEFIT,
                            BenefitResult(benefit, index)
                        )
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
