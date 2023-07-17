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
    const val NavigateUp = "NavigateUp"

    fun navigateUp() = object : NavigateAction {
        override val destination: String
            get() = NavigateUp
    }

    fun backToLoginScreen() = object : NavigateAction {
        override val destination: String
            get() = FieldMateScreen.Login.name
        override val navOptions: NavOptions
            get() = NavOptions.Builder()
                .setPopUpTo(
                    route = destination,
                    inclusive = true,
                )
                .setLaunchSingleTop(true)
                .build()
    }

    object LoginScreen {
        fun toHomeScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.TaskGraph.name
        }

        fun toJoinScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.Join.name
        }

        fun toFindPasswordScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.FindPassword.name
        }

        fun toTermsOfUseScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.TermsOfUse.name
        }

        fun toPrivacyPolicyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.PrivacyPolicy.name
        }
    }

    object JoinScreen {
        fun toSelectCompanyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.SelectCompany.name
        }
    }

    object SelectCompanyScreen {
        fun toAddCompanyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.AddCompany.name
        }
    }

    object AddCompanyScreen {
        fun toOnBoardingScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.OnBoarding.name
            override val navOptions: NavOptions
                get() = NavOptions.Builder()
                    .setPopUpTo(
                        route = FieldMateScreen.Login.name,
                        inclusive = true,
                    )
                    .build()
        }
    }
}