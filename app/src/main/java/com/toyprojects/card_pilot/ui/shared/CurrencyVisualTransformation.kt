package com.toyprojects.card_pilot.ui.shared

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = try {
            val parsed = originalText.toLong()
            NumberFormat.getInstance(Locale.KOREA).format(parsed)
        } catch (_: NumberFormatException) {
            originalText
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                val safeOffset = offset.coerceAtMost(originalText.length)

                var originalIndex = 0
                for (i in formattedText.indices) {
                    if (originalIndex == safeOffset) return i
                    if (formattedText[i].isDigit()) {
                        originalIndex++
                    }
                }
                return formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalCount = 0
                val safeOffset = offset.coerceAtMost(formattedText.length)
                for (i in 0 until safeOffset) {
                    if (formattedText[i].isDigit()) {
                        originalCount++
                    }
                }
                return originalCount
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
