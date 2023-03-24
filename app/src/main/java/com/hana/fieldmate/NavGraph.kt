package com.hana.fieldmate

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
import com.hana.fieldmate.ui.auth.*
import com.hana.fieldmate.ui.auth.viewmodel.CompanyViewModel
import com.hana.fieldmate.ui.auth.viewmodel.JoinViewModel
import com.hana.fieldmate.ui.auth.viewmodel.LoginViewModel
import com.hana.fieldmate.ui.business.*
import com.hana.fieldmate.ui.business.viewmodel.BusinessListViewModel
import com.hana.fieldmate.ui.business.viewmodel.BusinessTaskViewModel
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.client.AddEditClientScreen
import com.hana.fieldmate.ui.client.ClientScreen
import com.hana.fieldmate.ui.client.ClientTaskGraphScreen
import com.hana.fieldmate.ui.client.DetailClientScreen
import com.hana.fieldmate.ui.client.viewmodel.ClientListViewModel
import com.hana.fieldmate.ui.client.viewmodel.ClientViewModel
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
import com.hana.fieldmate.ui.task.viewmodel.TaskListViewModel
import com.hana.fieldmate.ui.task.viewmodel.TaskViewModel

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
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                navController = navController,
                loginBtnOnClick = viewModel::login
            )
        }

        composable(route = FieldMateScreen.Join.name) {
            val viewModel: JoinViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            JoinScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                navController = navController,
                checkName = viewModel::checkName,
                checkPhone = viewModel::checkPhone,
                sendMessage = viewModel::sendMessage,
                verifyMessage = viewModel::verifyMessage,
                checkTimer = viewModel::checkTimer,
                checkPassword = viewModel::checkPassword,
                checkConfirmPassword = viewModel::checkConfirmPassword,
                checkRegisterEnabled = viewModel::checkRegisterEnabled,
                joinBtnOnClick = viewModel::join
            )
        }

        composable(route = FieldMateScreen.FindPassword.name) {
            val viewModel: JoinViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            FindPasswordScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                checkPhone = viewModel::checkPhone,
                sendMessage = viewModel::sendMessage,
                verifyMessage = viewModel::verifyMessage,
                checkTimer = viewModel::checkTimer,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.OnBoarding.name) {
            OnBoardingScreen(navController = navController)
        }

        composable(route = FieldMateScreen.SelectCompany.name) {
            val viewModel: CompanyViewModel = hiltViewModel()

            SelectCompanyScreen(
                fetchUserInfo = viewModel::fetchUserInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                navController = navController,
                joinCompany = viewModel::joinCompany
            )
        }

        composable(route = FieldMateScreen.AddCompany.name) {
            val viewModel: CompanyViewModel = hiltViewModel()

            AddCompanyScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                confirmBtnOnClick = viewModel::createCompany
            )
        }
    }
}

fun NavGraphBuilder.taskGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.TaskList.name,
        route = FieldMateScreen.TaskGraph.name
    ) {
        composable(route = FieldMateScreen.TaskList.name) {
            val viewModel: TaskListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TaskScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTasks = viewModel::loadTasks,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                addBtnOnClick = {
                    navController.navigate(FieldMateScreen.AddTask.name)
                }
            )
        }

        composable(route = FieldMateScreen.AddTask.name) {
            val viewModel: TaskViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditTaskScreen(
                mode = EditMode.Add,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTask = viewModel::loadTask,
                loadClients = viewModel::loadClients,
                loadBusinesses = viewModel::loadBusinesses,
                loadCategories = viewModel::loadCategories,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                selectedImageList = viewModel.selectedImageList,
                selectImages = viewModel::selectImages,
                unselectImage = viewModel::unselectImage,
                addBtnOnClick = viewModel::createTask,
                updateBtnOnClick = viewModel::updateTask
            )
        }

        composable(
            route = "${FieldMateScreen.EditTask.name}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: TaskViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditTaskScreen(
                mode = EditMode.Edit,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTask = viewModel::loadTask,
                loadClients = viewModel::loadClients,
                loadBusinesses = viewModel::loadBusinesses,
                loadCategories = viewModel::loadCategories,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController,
                selectedImageList = viewModel.selectedImageList,
                selectImages = viewModel::addImages,
                unselectImage = viewModel::deleteImage,
                addBtnOnClick = viewModel::createTask,
                updateBtnOnClick = viewModel::updateTask
            )
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
            val viewModel: TaskViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailTaskScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTask = viewModel::loadTask,
                deleteTask = viewModel::deleteTask,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.clientGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.ClientList.name,
        route = FieldMateScreen.ClientGraph.name
    ) {
        composable(route = FieldMateScreen.ClientList.name) {
            val viewModel: ClientListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ClientScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadClients = viewModel::loadClients,
                navController = navController,
                addBtnOnClick = { navController.navigate(FieldMateScreen.AddClient.name) }
            )
        }

        composable(route = FieldMateScreen.AddClient.name) {
            val viewModel: ClientViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditClientScreen(
                mode = EditMode.Add,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadClient = viewModel::loadClient,
                navController = navController,
                addBtnOnClick = viewModel::createClient,
                updateBtnOnClick = viewModel::updateClient
            )
        }

        composable(route = "${FieldMateScreen.EditClient.name}/{clientId}",
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: ClientViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditClientScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadClient = viewModel::loadClient,
                navController = navController,
                addBtnOnClick = viewModel::createClient,
                updateBtnOnClick = viewModel::updateClient
            )
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
            val viewModel: ClientViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailClientScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadClient = viewModel::loadClient,
                loadBusinesses = viewModel::loadBusinesses,
                deleteClient = viewModel::deleteClient,
                navController = navController
            )
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
            val viewModel: ClientViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ClientTaskGraphScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTaskGraph = viewModel::loadTaskGraph,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.businessGraph(navController: NavController) {
    navigation(
        startDestination = FieldMateScreen.BusinessList.name,
        route = FieldMateScreen.BusinessGraph.name
    ) {
        composable(route = FieldMateScreen.BusinessList.name) {
            val viewModel: BusinessListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                uiState = uiState,
                loadBusinesses = viewModel::loadBusinesses,
                userInfo = App.getInstance().getUserInfo(),
                navController = navController
            )
        }

        composable(route = "${FieldMateScreen.AddBusiness.name}/{clientId}",
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditBusinessScreen(
                mode = EditMode.Add,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadCompanyMembers = viewModel::loadCompanyMembers,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                removeMember = viewModel::removeMember,
                navController = navController,
                confirmBtnOnClick = viewModel::createBusiness
            )
        }

        composable(
            route = "${FieldMateScreen.EditBusiness.name}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditBusinessScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadCompanyMembers = viewModel::loadCompanyMembers,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                removeMember = viewModel::removeMember,
                navController = navController,
                confirmBtnOnClick = viewModel::updateBusiness
            )
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
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailBusinessScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                deleteBusiness = viewModel::deleteBusiness,
                navController = navController,
            )
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
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailEtcBusinessScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                navController = navController,
            )
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
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessMemberScreen(
                uiState = uiState,
                selectedMemberList = viewModel.selectedMemberList,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                navController = navController
            )
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
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SelectBusinessMemberScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadCompanyMembers = viewModel::loadCompanyMembers,
                selectedMemberList = viewModel.selectedMemberList,
                updateMembers = viewModel::updateBusinessMembers,
                selectMember = viewModel::selectMember,
                unselectMember = viewModel::removeMember,
                navController = navController
            )
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
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessTaskGraphScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTaskGraph = viewModel::loadTaskGraph,
                navController = navController
            )
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
            val viewModel: BusinessTaskViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SummaryTaskScreen(
                uiState = uiState,
                userInfo = App.getInstance().getUserInfo(),
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadTaskListByDate = viewModel::loadTaskListByDate,
                loadTaskDateList = viewModel::loadTaskDateList,
                loadCategories = viewModel::loadCategories,
                navController = navController
            )
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