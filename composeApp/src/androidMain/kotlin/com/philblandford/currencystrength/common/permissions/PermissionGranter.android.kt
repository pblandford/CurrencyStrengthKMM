package com.philblandford.currencystrength.common.permissions

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController


class PermissionGranterAndroid(private val permissionsController: PermissionsController) :
    PermissionGranter {
    override suspend fun grant(
        permission: String,
        onGranted: suspend () -> Unit,
        onDenied: suspend (shouldShowRationale: Boolean) -> Unit
    ) {
        try {
        //    permissionsController.providePermission(Permission.valueOf(permission))
            onGranted()
        } catch (e: DeniedException) {
            onDenied(true)
        } catch (e: DeniedAlwaysException) {
            onDenied(false)
        }
    }
}