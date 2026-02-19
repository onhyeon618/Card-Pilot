package com.toyprojects.card_pilot.data

import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.Transaction
import java.time.LocalDate
import java.time.LocalTime

object MockData {
    val cardInfos = listOf(
        CardInfo(
            name = "현대카드 The Red",
            benefits = listOf(
                Benefit(
                    name = "바우처 (여행/호텔)",
                    usedAmount = 150000,
                    totalAmount = 200000,
                    explanation = "항공권 및 호텔 예약 시 사용 가능",
                    transactions = listOf(
                        Transaction("대한항공", LocalDate.of(2026, 2, 14), LocalTime.of(14, 30), 50000),
                        Transaction("호텔 신라", LocalDate.of(2026, 2, 12), LocalTime.of(9, 0), 100000)
                    )
                ),
                Benefit(
                    name = "PP카드 라운지",
                    usedAmount = 2,
                    totalAmount = 10,
                    transactions = listOf()
                ),
                Benefit(
                    name = "메탈 플레이트 발급",
                    usedAmount = 1,
                    totalAmount = 1,
                    explanation = "발급 수수료 면제",
                    transactions = listOf(
                        Transaction("카드 발급", LocalDate.of(2026, 1, 10), LocalTime.of(10, 0), 100000)
                    )
                )
            )
        ),
        CardInfo(
            name = "삼성카드 taptap O",
            benefits = listOf(
                Benefit(
                    name = "스타벅스 50% 할인",
                    usedAmount = 5000,
                    totalAmount = 10000,
                    transactions = listOf()
                ),
                Benefit(
                    name = "대중교통 10% 할인",
                    usedAmount = 2000,
                    totalAmount = 5000,
                    transactions = listOf()
                )
            )
        ),
        CardInfo(
            name = "신한카드 Mr.Life",
            benefits = listOf(
                Benefit(
                    name = "편의점 10% 적립",
                    usedAmount = 3000,
                    totalAmount = 10000,
                    transactions = listOf()
                )
            )
        )
    )
}
