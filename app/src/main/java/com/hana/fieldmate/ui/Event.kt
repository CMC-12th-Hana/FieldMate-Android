package com.hana.fieldmate.ui

sealed class Event {
    // TODO: popupto, inclusive, navigateup 같은 옵션 세분화 필요
    data class NavigateTo(val destination: String) : Event()
    object NavigateUp : Event()
    data class NavigatePopUpTo(val destination: String, val popUpDestination: String) : Event()
    data class Dialog(val dialog: DialogState, val action: DialogAction) : Event()
}

enum class DialogState {
    AddEdit, Delete, TimeOut, PhotoPick, Select
}

enum class DialogAction {
    Open, Close
}