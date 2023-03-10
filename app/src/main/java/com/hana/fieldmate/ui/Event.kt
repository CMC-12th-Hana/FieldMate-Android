package com.hana.fieldmate.ui

sealed class Event {
    // TODO: popupto, inclusive, navigateup 같은 옵션 세분화 필요
    data class NavigateTo(val destination: String) : Event()
    object NavigateUp : Event()
    data class NavigatePopUpTo(
        val destination: String,
        val popUpDestination: String,
        val inclusive: Boolean = false
    ) : Event()

    data class Dialog(
        val dialog: DialogState,
        val action: DialogAction,
        val description: String = ""
    ) : Event()
}

enum class DialogState {
    AddEdit, Delete, TimeOut, PhotoPick, Select, Error, Image
}

enum class DialogAction {
    Open, Close
}