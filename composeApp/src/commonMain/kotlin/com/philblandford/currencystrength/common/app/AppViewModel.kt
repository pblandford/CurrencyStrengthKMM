package com.philblandford.currencystrength.common.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philblandford.currencystrength.common.log.log
import com.philblandford.currencystrength.common.notifications.NotificationManager
import com.philblandford.currencystrength.common.permissions.PermissionGranter
import com.philblandford.currencystrength.common.ui.BaseViewModel
import com.philblandford.currencystrength.common.ui.ScreenState
import com.philblandford.currencystrength.features.checkin.CheckIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class AppViewModel(
    private val permissionGranter: PermissionGranter,
    private val checkIn: CheckIn
) : BaseViewModel<ScreenState>() {
    override val state = MutableStateFlow(ScreenState())

    internal fun init() {
        log("AppViewModel init")
        viewModelScope.launch {
            permissionGranter.grant("REMOTE_NOTIFICATION", {
                log("Permission granted - doing checkIN")
                doCheckIn()
            }, {
                log("Permission denied")
            })
        }
    }

    private fun doCheckIn() {
        tryResult {
            checkIn().onSuccess {
                log("CheckIn success")
            }.onFailure {
                log("CheckIn failed: $it")
            }
        }
    }
}