package com.toyprojects.card_pilot.mock

import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.model.Transaction
import java.time.LocalDateTime

object MockData {
    val mockCards = mutableListOf(
        CardSimpleInfo(id = 1L, name = "신한 모베러카드", image = "file:///android_asset/card_mock_1.png", displayOrder = 1),
        CardSimpleInfo(id = 2L, name = "삼성 탭탭오", image = "file:///android_asset/card_mock_2.png", displayOrder = 2)
    )

    private val benefitsCard1 = listOf(
        Benefit(
            id = 1L,
            name = "스타벅스 50% 할인",
            explanation = "스타벅스 월 최대 1만원 한도내",
            capAmount = 10000L,
            usedAmount = 4500L,
            displayOrder = 1
        ),
        Benefit(
            id = 2L,
            name = "대중교통 10% 적립",
            explanation = "대중교통 월 5천점까지 적립",
            capAmount = 5000L,
            usedAmount = 1250L,
            displayOrder = 2
        )
    )

    private val benefitsCard2 = listOf(
        Benefit(
            id = 3L,
            name = "편의점 10% 할인",
            explanation = "CU, GS25",
            capAmount = 5000L,
            usedAmount = 5000L,
            displayOrder = 1
        )
    )

    val mockCardDetails = mutableMapOf(
        1L to CardInfo(
            id = 1L,
            name = "신한 모베러카드",
            image = "file:///android_asset/card_mock_1.png",
            usageAmount = 150000L,
            benefits = benefitsCard1,
            displayOrder = 1
        ),
        2L to CardInfo(
            id = 2L,
            name = "삼성 탭탭오",
            image = "file:///android_asset/card_mock_2.png",
            usageAmount = 300000L,
            benefits = benefitsCard2,
            displayOrder = 2
        )
    )

    val mockTransactions = mutableMapOf(
        1L to mutableListOf(
            Transaction(id = 1L, merchant = "스타벅스 강남점", dateTime = LocalDateTime.now().minusDays(2), amount = 4500L),
            Transaction(id = 2L, merchant = "스타벅스 역삼점", dateTime = LocalDateTime.now().minusDays(5), amount = 4500L)
        ),
        2L to mutableListOf(
            Transaction(id = 3L, merchant = "지하철", dateTime = LocalDateTime.now().minusDays(1), amount = 1250L)
        ),
        3L to mutableListOf(
            Transaction(id = 4L, merchant = "GS25", dateTime = LocalDateTime.now().minusDays(3), amount = 5000L)
        )
    )
}
