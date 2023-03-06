package com.hana.fieldmate.ui

import com.hana.fieldmate.FieldMateScreen

sealed class Event {
    // TODO: popupto, inclusive, navigateup 같은 옵션 세분화 필요
    data class NavigateTo(val destination: FieldMateScreen) : Event()
    data class Dialog(val title: String, val openState: Boolean) : Event()
    data class Alert(val title: String, val openState: Boolean) : Event()
}
