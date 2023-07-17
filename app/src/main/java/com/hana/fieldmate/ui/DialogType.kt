package com.hana.fieldmate.ui

import com.hana.fieldmate.data.ErrorType

sealed class DialogType {
    object AddEdit : DialogType()
    object Confirm : DialogType()
    object Delete : DialogType()
    object TimeOut : DialogType()
    object PhotoPick : DialogType()
    object Select : DialogType()
    data class Error(val errorType: ErrorType) : DialogType()
    object Image : DialogType()
}
