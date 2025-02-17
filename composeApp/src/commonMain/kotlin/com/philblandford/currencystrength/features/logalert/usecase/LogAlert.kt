package com.philblandford.currencystrength.features.logalert.usecase

import com.philblandford.currencystrength.common.data.RoomDatabaseManager
import com.philblandford.currencystrength.common.data.toEntity
import com.philblandford.currencystrength.common.model.Alert

class LogAlert(private val database: RoomDatabaseManager) {
    suspend operator fun invoke(alert: Alert): Result<Unit> {
        return kotlin.runCatching {
            database.getDao().insert(alert.toEntity())
        }
    }
}