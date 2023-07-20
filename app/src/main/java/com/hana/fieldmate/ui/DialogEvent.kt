package com.hana.fieldmate.ui

import com.hana.fieldmate.data.ErrorType

sealed class DialogEvent {
    object AddEdit : DialogEvent()
    object Confirm : DialogEvent()
    object Delete : DialogEvent()
    object TimeOut : DialogEvent()
    object PhotoPick : DialogEvent()
    object Select : DialogEvent()
    data class Error(val errorType: ErrorType) : DialogEvent()
    object Image : DialogEvent()
}
