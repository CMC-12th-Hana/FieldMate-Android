package com.hana.fieldmate.ui.navigation

import android.os.Parcelable
import androidx.navigation.NavOptions
import com.hana.fieldmate.FieldMateScreen

interface NavigateAction {
    val destination: String
    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}

object NavigateActions {
    object LoginScreen {
        fun loginScreenToHomeScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.TaskGraph.name
        }
    }
}