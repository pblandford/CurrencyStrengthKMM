package com.philblandford.currencystrength.common.permissions

interface PermissionGranter {
    suspend fun grant(permission: String, onGranted: suspend () -> Unit,
                      onDenied: suspend (shouldShowRationale: Boolean) -> Unit)
}

