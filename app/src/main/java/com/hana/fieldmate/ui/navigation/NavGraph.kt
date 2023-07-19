package com.hana.fieldmate.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.hana.fieldmate.App
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
import com.hana.fieldmate.ui.member.viewmodel.MemberListViewModel
import com.hana.fieldmate.ui.member.viewmodel.MemberViewModel
import com.hana.fieldmate.ui.setting.*
import com.hana.fieldmate.ui.setting.viewmodel.CategoryViewModel
import com.hana.fieldmate.ui.setting.viewmodel.ChangeLeaderViewModel
import com.hana.fieldmate.ui.setting.viewmodel.ChangePasswordViewModel
import com.hana.fieldmate.ui.setting.viewmodel.WithdrawalViewModel
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
        route = FieldMateScreen.Member.name
    ) {
        composable(route = FieldMateScreen.MemberList.name) {
            val viewModel: MemberListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MemberScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMembers = viewModel::loadMembers,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.AddMember.name) {
            val viewModel: MemberViewModel = hiltViewModel()

            AddMemberScreen(
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                navController = navController,
                confirmBtnOnClick = viewModel::createMember
            )
        }

        composable(
            route = "${FieldMateScreen.EditMember.name}/{memberId}",
            arguments = listOf(
                navArgument("memberId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: MemberViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            EditMemberScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMember = viewModel::loadMember,
                navController = navController,
                updateMyProfile = viewModel::updateMyProfile,
                updateMemberProfile = viewModel::updateMemberProfile
            )
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
            val viewModel: MemberViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailMemberScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMember = viewModel::loadMember,
                deleteMember = viewModel::deleteMember,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.settingGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.SettingMenu.name,
        route = FieldMateScreen.Setting.name
    ) {
        composable(route = FieldMateScreen.SettingMenu.name) {
            SettingScreen(
                userInfo = App.getInstance().getUserInfo(),
                navController = navController
            )
        }

        composable(route = FieldMateScreen.Category.name) {
            val viewModel: CategoryViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CategoryScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadCategories = viewModel::loadCategories,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                addCategory = viewModel::createTaskCategory,
                updateCategory = viewModel::updateTaskCategory,
                deleteCategory = viewModel::deleteTaskCategory
            )
        }

        composable(route = FieldMateScreen.ChangeLeader.name) {
            val viewModel: ChangeLeaderViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ChangeLeaderScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadCompanyMembers = viewModel::loadMembers,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                updateMemberToLeader = viewModel::updateMemberToLeader
            )
        }

        composable(route = FieldMateScreen.ChangePassword.name) {
            val viewModel: ChangePasswordViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ChangePasswordScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                checkPassword = viewModel::checkPassword,
                checkConfirmPassword = viewModel::checkConfirmPassword,
                checkConfirmEnabled = viewModel::checkConfirmEnabled,
                navController = navController,
                confirmBtnOnClick = viewModel::updateMyPassword
            )
        }

        composable(route = FieldMateScreen.Withdrawal.name) {
            val viewModel: WithdrawalViewModel = hiltViewModel()

            WithdrawalScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                quitMember = viewModel::quitMember,
                navController = navController
            )
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