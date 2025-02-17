package com.philblandford.currencystrength.common.permissions


class PermissionGranterIos() : PermissionGranter {
    override suspend fun grant(
        permission: String,
        onGranted: suspend () -> Unit,
        onDenied: suspend (shouldShowRationale: Boolean) -> Unit
    ) {
        onGranted()
    }
}

//class PermissionGranterAndroid(private val permissionsController: PermissionsController) :
//    PermissionGranter {
//    override suspend fun grant(
//        permission: String,
//        onGranted: suspend () -> Unit,
//        onDenied: suspend (shouldShowRationale: Boolean) -> Unit
//    ) {
//        try {
//            permissionsController.providePermission(Permission.valueOf(permission))
//            onGranted()
//        } catch (e: DeniedException) {
//            onDenied(true)
//        } catch (e: DeniedAlwaysException) {
//            onDenied(false)
//        }
//    }
//}