package com.hana.fieldmate.ui.navigation

import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.hana.fieldmate.BuildConfig
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.auth.*
import com.hana.fieldmate.ui.business.*
import com.hana.fieldmate.ui.client.AddEditClientScreen
import com.hana.fieldmate.ui.client.ClientScreen
import com.hana.fieldmate.ui.client.ClientTaskGraphScreen
import com.hana.fieldmate.ui.client.DetailClientScreen
import com.hana.fieldmate.ui.member.AddMemberScreen
import com.hana.fieldmate.ui.member.DetailMemberScreen
import com.hana.fieldmate.ui.member.EditMemberScreen
import com.hana.fieldmate.ui.member.MemberScreen
import com.hana.fieldmate.ui.setting.*
import com.hana.fieldmate.ui.splash.SplashScreen
import com.hana.fieldmate.ui.splash.viewmodel.SplashViewModel
import com.hana.fieldmate.ui.task.AddEditTaskScreen
import com.hana.fieldmate.ui.task.DetailTaskScreen
import com.hana.fieldmate.ui.task.TaskScreen

fun NavGraphBuilder.splashGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.Splash.name,
        route = FieldMateScreen.SplashGraph.name
    ) {
        composable(route = FieldMateScreen.Splash.name) {
            val viewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                fetchUserInfo = viewModel::fetchUserInfo,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.Login.name,
        route = FieldMateScreen.AuthGraph.name
    ) {
        composable(route = FieldMateScreen.Login.name) {
            LoginScreen()
        }

        composable(route = FieldMateScreen.Join.name) {
            JoinScreen()
        }

        composable(route = FieldMateScreen.FindPassword.name) {
            FindPasswordScreen()
        }

        composable(route = FieldMateScreen.OnBoarding.name) {
            OnBoardingScreen(navController = navController)
        }

        composable(route = FieldMateScreen.SelectCompany.name) {
            SelectCompanyScreen()
        }

        composable(route = FieldMateScreen.AddCompany.name) {
            AddCompanyScreen()
        }
    }
}

fun NavGraphBuilder.taskGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.TaskList.name,
        route = FieldMateScreen.TaskGraph.name
    ) {
        composable(route = FieldMateScreen.TaskList.name) {
            TaskScreen(navController = navController)
        }

        composable(
            route = FieldMateScreen.AddTask.name,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Add.name
                }
            )
        ) {
            AddEditTaskScreen()
        }

        composable(
            route = "${FieldMateScreen.EditTask.name}/{taskId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Edit.name
                },
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditTaskScreen()
        }

        composable(
            route = "${FieldMateScreen.DetailTask.name}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailTaskScreen()
        }
    }
}

fun NavGraphBuilder.clientGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.ClientList.name,
        route = FieldMateScreen.ClientGraph.name
    ) {
        composable(route = FieldMateScreen.ClientList.name) {
            ClientScreen(navController = navController)
        }

        composable(route = FieldMateScreen.AddClient.name,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Add.name
                }
            )
        ) {
            AddEditClientScreen()
        }

        composable(route = "${FieldMateScreen.EditClient.name}/{clientId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Edit.name
                },
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditClientScreen()
        }

        composable(
            route = "${FieldMateScreen.DetailClient.name}/{clientId}",
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailClientScreen()
        }

        composable(
            route = "${FieldMateScreen.ClientTaskGraph.name}/{clientId}",
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            ClientTaskGraphScreen()
        }
    }
}


fun NavGraphBuilder.businessGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.BusinessList.name,
        route = FieldMateScreen.BusinessGraph.name
    ) {
        composable(route = FieldMateScreen.BusinessList.name) {
            BusinessScreen(navController = navController)
        }

        composable(route = "${FieldMateScreen.AddBusiness.name}/{clientId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Add.name
                },
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditBusinessScreen()
        }

        composable(
            route = "${FieldMateScreen.EditBusiness.name}/{businessId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Edit.name
                },
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditBusinessScreen()
        }

        composable(
            route = "${FieldMateScreen.DetailBusiness.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailBusinessScreen()
        }

        composable(
            route = "${FieldMateScreen.DetailEtcBusiness.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailEtcBusinessScreen()
        }

        composable(
            route = "${FieldMateScreen.BusinessMember.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            BusinessMemberScreen()
        }

        composable(
            route = "${FieldMateScreen.SelectBusinessMember.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            SelectBusinessMemberScreen()
        }

        composable(
            route = "${FieldMateScreen.BusinessTaskGraph.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            BusinessTaskGraphScreen()
        }

        composable(
            route = "${FieldMateScreen.SummaryTask.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            SummaryTaskScreen()
        }
    }
}

fun NavGraphBuilder.memberGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.MemberList.name,
        route = FieldMateScreen.MemberGraph.name
    ) {
        composable(route = FieldMateScreen.MemberList.name) {
            MemberScreen(navController = navController)
        }

        composable(
            route = FieldMateScreen.AddMember.name,
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Edit.name
                }
            )
        ) {
            AddMemberScreen()
        }

        composable(
            route = "${FieldMateScreen.EditMember.name}/{memberId}",
            arguments = listOf(
                navArgument("mode") {
                    type = NavType.StringType
                    defaultValue = EditMode.Edit.name
                },
                navArgument("memberId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            EditMemberScreen()
        }

        composable(
            route = "${FieldMateScreen.DetailMember.name}/{memberId}",
            arguments = listOf(
                navArgument("memberId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailMemberScreen()
        }
    }
}

fun NavGraphBuilder.settingGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.SettingMenu.name,
        route = FieldMateScreen.SettingGraph.name
    ) {
        composable(route = FieldMateScreen.SettingMenu.name) {
            SettingScreen(navController = navController)
        }

        composable(route = FieldMateScreen.Category.name) {
            CategoryScreen()
        }

        composable(route = FieldMateScreen.ChangeLeader.name) {
            ChangeLeaderScreen()
        }

        composable(route = FieldMateScreen.ChangePassword.name) {
            ChangePasswordScreen()
        }

        composable(route = FieldMateScreen.Withdrawal.name) {
            WithdrawalScreen()
        }

        composable(route = FieldMateScreen.AppInfo.name) {
            AppInfoScreen(navController = navController)
        }

        composable(route = FieldMateScreen.TermsOfUse.name) {
            WebViewScreen(
                navController = navController,
                title = stringResource(id = R.string.terms_of_use),
                contentUrl = "${BuildConfig.BASE_URL}docs/terms-of-service.html"
            )
        }

        composable(route = FieldMateScreen.PrivacyPolicy.name) {
            WebViewScreen(
                navController = navController,
                title = stringResource(id = R.string.privacy_policy),
                contentUrl = "${BuildConfig.BASE_URL}docs/privacy-policy.html"
            )
        }
    }
}