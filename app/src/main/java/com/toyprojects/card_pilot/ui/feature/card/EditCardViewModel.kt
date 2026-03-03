package com.toyprojects.card_pilot.ui.feature.card

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.ui.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

sealed interface EditCardEvent {
    data class ShowSnackbar(val message: String) : EditCardEvent
}

data class CardFormData(
    val cardName: String = "",
    val cardImage: String = "",
    val benefits: List<BenefitProperty> = emptyList()
)

data class EditCardUiState(
    val formData: CardFormData = CardFormData(),
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isModified: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
) {
    val isFormValid: Boolean
        get() = formData.cardName.isNotBlank() && formData.benefits.isNotEmpty()
}

class EditCardViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cardRepository: CardRepository,
    private val benefitRepository: BenefitRepository,
) : ViewModel() {

    private val _cardId: Long? = savedStateHandle.toRoute<Screen.EditCard>().cardId

    private var initialSnapshot: CardFormData = CardFormData()

    private val _uiState = MutableStateFlow(EditCardUiState(isEdit = _cardId != null))
    val uiState: StateFlow<EditCardUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<EditCardEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init {
        if (_cardId != null) {
            loadCardData(_cardId)
        }
    }

    private fun loadCardData(cardId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val card = cardRepository.getCardById(cardId)

            if (card == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val benefits = benefitRepository.getBenefitPropertiesOfCardSync(cardId)

            val initialFormData = CardFormData(
                cardName = card.name,
                cardImage = card.image,
                benefits = benefits
            )
            initialSnapshot = initialFormData

            _uiState.update { state ->
                state.copy(
                    formData = initialFormData,
                    isLoading = false
                )
            }
        }
    }

    private fun updateFormData(transform: (CardFormData) -> CardFormData) {
        _uiState.update { currentState ->
            val nextFormData = transform(currentState.formData)
            currentState.copy(
                formData = nextFormData,
                isModified = nextFormData != initialSnapshot
            )
        }
    }

    fun updateCardName(name: String) {
        updateFormData { it.copy(cardName = name) }
    }

    fun updateCardImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingImagePath = _uiState.value.formData.cardImage

                // 선택한 이미지를 내부 저장소에 복사
                val fileName = "card_bg_${System.currentTimeMillis()}.jpg"
                val newFile = File(context.filesDir, fileName)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    newFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val newPath = newFile.absolutePath

                withContext(Dispatchers.Main) {
                    updateFormData { it.copy(cardImage = newPath) }
                }

                // 선택하지 않은 임시 이미지는 제거 (사용자가 이미지를 여러 번 변경한 경우 대비)
                if (existingImagePath.isNotEmpty() && existingImagePath != initialSnapshot.cardImage) {
                    val existingFile = File(existingImagePath)
                    if (existingFile.exists() && existingFile.absolutePath.startsWith(context.filesDir.absolutePath)) {
                        existingFile.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _eventChannel.send(EditCardEvent.ShowSnackbar("이미지 저장에 실패했습니다."))
            }
        }
    }

    fun updateBenefit(updatedBenefit: BenefitProperty, benefitIndex: Int) {
        updateFormData { currentFormData ->
            val existingBenefits = currentFormData.benefits.toMutableList()

            if (benefitIndex != -1 && benefitIndex in existingBenefits.indices) {
                existingBenefits[benefitIndex] = updatedBenefit
            } else {
                existingBenefits.add(updatedBenefit)
            }

            currentFormData.copy(benefits = existingBenefits)
        }
    }

    fun removeBenefit(benefitIndex: Int) {
        updateFormData { currentFormData ->
            val benefits = currentFormData.benefits.toMutableList()
            if (benefitIndex in benefits.indices) {
                benefits.removeAt(benefitIndex)
            }
            currentFormData.copy(benefits = benefits)
        }
    }

    fun moveBenefit(fromIndex: Int, toIndex: Int) {
        updateFormData { currentFormData ->
            val existingBenefits = currentFormData.benefits.toMutableList()
            if (fromIndex in existingBenefits.indices && toIndex in existingBenefits.indices) {
                val item = existingBenefits.removeAt(fromIndex)
                existingBenefits.add(toIndex, item)
                currentFormData.copy(benefits = existingBenefits)
            } else {
                currentFormData
            }
        }
    }

    fun saveCard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val currentState = _uiState.value

            // 혜택 이름은 Unique 해야 함
            val benefitNames = currentState.formData.benefits.map { it.name }
            if (benefitNames.size != benefitNames.distinct().size) {
                _uiState.update { it.copy(isSaving = false) }
                _eventChannel.send(EditCardEvent.ShowSnackbar("혜택 이름은 중복될 수 없습니다."))
                return@launch
            }

            val displayOrder = if (_cardId != null) {
                cardRepository.getCardById(_cardId)?.displayOrder ?: 0
            } else {
                cardRepository.getMaxDisplayOrder() + 1
            }

            val cardInfo = CardInfo(
                id = _cardId ?: 0L,
                name = currentState.formData.cardName,
                image = currentState.formData.cardImage,
                displayOrder = displayOrder
            )

            val benefits = currentState.formData.benefits.mapIndexed { index, property ->
                BenefitProperty(
                    id = property.id,
                    name = property.name,
                    explanation = property.explanation,
                    capAmount = property.capAmount,
                    dailyLimit = property.dailyLimit,
                    oneTimeLimit = property.oneTimeLimit,
                    rate = property.rate,
                    displayOrder = index + 1
                )
            }

            Log.d("hyeon", "Saving card: $cardInfo with benefits: $benefits")

            if (_cardId != null) {
                cardRepository.updateCard(cardInfo, benefits)
            } else {
                cardRepository.insertCard(cardInfo, benefits)
            }

            // 카드 이미지를 변경한 경우 기존 이미지 제거
            if (initialSnapshot.cardImage.isNotEmpty() && initialSnapshot.cardImage != currentState.formData.cardImage) {
                val oldFile = File(initialSnapshot.cardImage)
                if (oldFile.exists()) {
                    oldFile.delete()
                }
            }

            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 뷰모델이 저장되지 않고 삭제된 경우, 새로 선택한 임시 이미지 삭제
        if (!_uiState.value.saveSuccess) {
            val currentImage = _uiState.value.formData.cardImage
            if (currentImage.isNotEmpty() && currentImage != initialSnapshot.cardImage) {
                val file = File(currentImage)
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }
}
