package com.philblandford.currencystrength.common.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philblandford.currencystrength.common.error.ErrorHandler
import com.philblandford.currencystrength.common.log.log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class ScreenState


open class BasicActions {
    data object Success : BasicActions()
    data class Error(val message: String) : BasicActions()
}

abstract class BaseViewModel<T : ScreenState> : ViewModel(), KoinComponent {
    internal abstract val state: MutableStateFlow<T>
    val actionFlow = MutableSharedFlow<BasicActions>()
    private val errorHandler: ErrorHandler by inject()

    protected fun updateState(func: T.() -> T) {
        viewModelScope.launch {
            val newValue = state.value.func()
            state.value = newValue
            state.emit(newValue)
        }
    }

    protected fun handleError(exception: Throwable, displayErrorToUser: Boolean = true) {
        errorHandler.handleError(exception, displayErrorToUser)
    }

    protected fun <T> tryResult(displayErrorToUser: Boolean = true, func: suspend () -> Result<T>) {
        val provisionalException = Exception("TryResult")
        viewModelScope.launch {
            try {
                func().onFailure {
                    log("Error from result: $it", provisionalException)
                    handleError(it, displayErrorToUser)
                }
            } catch (e: Exception) {
                log("Error from thrown exception: $e", e)
                handleError(Exception(e), displayErrorToUser)
            }
        }
    }

    protected fun complete() {
        viewModelScope.launch {
            actionFlow.emit(BasicActions.Success)
        }
    }
}