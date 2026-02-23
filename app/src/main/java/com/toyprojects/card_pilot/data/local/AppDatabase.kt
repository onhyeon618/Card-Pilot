package com.toyprojects.card_pilot.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.toyprojects.card_pilot.data.local.converter.DateTimeConverters
import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity

@TypeConverters(DateTimeConverters::class)
@Database(
    entities = [CardInfoEntity::class, BenefitEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun benefitDao(): BenefitDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private const val DATABASE_NAME = "card_pilot_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // TODO: 앱 출시 전 옵션 제거 및 데이터 마이그레이션 구현
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
