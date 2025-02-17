package com.philblandford.currencystrength.common.ui

import com.philblandford.currencystrength.common.log.log
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplicationDidChangeStatusBarOrientationNotification
import platform.UIKit.UIDeviceOrientationDidChangeNotification
import platform.UIKit.UIScreen
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
private class ConfigurationObserver(private val onChange: () -> Unit) : NSObject() {

    fun start() {
        NSNotificationCenter.defaultCenter.addObserver(
            observer = this,
            selector = NSSelectorFromString("handleOrientationChange"),
            name = UIApplicationDidChangeStatusBarOrientationNotification,
            `object` = null
        )
    }

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun handleOrientationChange() {
        log("handleOrientationChange")
        onChange()

    }
}

class OrientationManagerIos : OrientationManager {
    override val orientationFlow: MutableStateFlow<Orientation> =
        MutableStateFlow(getCurrentOrientation())
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val configurationObserver = ConfigurationObserver {
        coroutineScope.launch {
            orientationFlow.emit(getCurrentOrientation())
        }
    }.apply { start() }

    override fun isPortrait(): Boolean = getCurrentOrientation() == Orientation.PORTRAIT

    @OptIn(ExperimentalForeignApi::class)
    private fun getCurrentOrientation(): Orientation {
        val width = UIScreen.mainScreen.bounds.useContents { size.width }
        val height = UIScreen.mainScreen.bounds.useContents { size.height }
        log("screenSize: ${width} ${height}")
        return if (width < height) {
            Orientation.PORTRAIT
        } else {
            Orientation.LANDSCAPE
        }
    }
}