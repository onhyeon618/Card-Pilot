package com.toyprojects.card_pilot.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toyprojects.card_pilot.data.dao.BenefitDao
import com.toyprojects.card_pilot.data.dao.CardDao
import com.toyprojects.card_pilot.data.dao.TransactionDao
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.entity.CardEntity
import com.toyprojects.card_pilot.data.entity.TransactionEntity

@Database(
    entities = [CardEntity::class, BenefitEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CardPilotDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun benefitDao(): BenefitDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: CardPilotDatabase? = null

        fun getDatabase(context: Context): CardPilotDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardPilotDatabase::class.java,
                    "card_pilot_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
