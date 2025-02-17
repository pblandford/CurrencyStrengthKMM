package com.philblandford.currencystrength.common.data

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.philblandford.currencystrength.common.data.RoomDatabaseManager.AppDatabase
import com.philblandford.currencystrength.common.model.Alert
import com.philblandford.currencystrength.common.model.Currency
import com.philblandford.currencystrength.common.model.CurrencyPair
import com.philblandford.currencystrength.common.model.Period
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

class RoomDatabaseManager(
    builder: RoomDatabase.Builder<AppDatabase>
) {

    private val database: AppDatabase = builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    fun getDao(): AlertDao = database.getDao()

    @Database(entities = [AlertEntity::class], version = 1)
    @ConstructedBy(AppDatabaseConstructor::class)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun getDao(): AlertDao
    }

    @Entity
    data class AlertEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val period: Period,
        val sample: Int,
        val threshold: Float,
        val firstCurrency: Currency?,
        val secondCurrency: Currency?,
        val time: LocalDateTime
    )

    @Dao
    interface AlertDao {
        @Insert
        suspend fun insert(item: AlertEntity)

        @Query("SELECT count(*) FROM AlertEntity")
        suspend fun count(): Int

        @Query("SELECT * FROM AlertEntity")
        fun getAllAsFlow(): Flow<List<AlertEntity>>
    }
}

fun Alert.toEntity(): RoomDatabaseManager.AlertEntity {
    return RoomDatabaseManager.AlertEntity(
        period = period,
        sample = sample,
        threshold = threshold,
        firstCurrency = lastPair?.base,
        secondCurrency = lastPair?.counter,
        time = lastAlert ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
}

fun RoomDatabaseManager.AlertEntity.toAlert():Alert {
    return Alert(
        period = period,
        sample = sample,
        threshold = threshold,
        lastPair = CurrencyPair(firstCurrency, secondCurrency),
        lastAlert = time,
    )
}