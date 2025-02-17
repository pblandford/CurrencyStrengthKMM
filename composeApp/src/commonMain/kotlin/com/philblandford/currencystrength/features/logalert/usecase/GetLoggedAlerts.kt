package com.philblandford.currencystrength.features.logalert.usecase

import com.philblandford.currencystrength.common.data.RoomDatabaseManager
import com.philblandford.currencystrength.common.data.toAlert
import com.philblandford.currencystrength.common.model.Alert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLoggedAlerts(private val databaseManager: RoomDatabaseManager) {

    operator fun invoke(): Flow<List<Alert>> {
        return databaseManager.getDao().getAllAsFlow().map {
            it.map { it.toAlert() }
        }
    }
}