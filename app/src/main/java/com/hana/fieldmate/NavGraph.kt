package com.hana.fieldmate

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.hana.fieldmate.ui.AuthViewModel
import com.hana.fieldmate.ui.auth.*
import com.hana.fieldmate.ui.business.*
import com.hana.fieldmate.ui.client.*
import com.hana.fieldmate.ui.member.*
import com.hana.fieldmate.ui.setting.CategoryScreen
import com.hana.fieldmate.ui.setting.CategoryViewModel
import com.hana.fieldmate.ui.setting.SettingScreen
import com.hana.fieldmate.ui.task.*

// TODO 후에 사용가능하게 된다면 네비게이션 그래프 분할하자

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.Login.name,
        route = FieldMateScreen.AuthGraph.name
    ) {
        composable(route = FieldMateScreen.Login.name) {
            val viewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                navController = navController,
                loginBtnOnClick = viewModel::login,
                findPwBtnOnClick = {
                    navController.navigate(FieldMateScreen.FindPassword.name)
                },
                registerBtnOnClick = {
                    navController.navigate(FieldMateScreen.Join.name)
                }
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
            val viewModel: JoinViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            FindPasswordScreen(
                uiState = uiState,
                checkPhone = viewModel::checkPhone,
                checkCertNumber = viewModel::checkCertNumber,
                setTimer = viewModel::setTimer,
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(FieldMateScreen.ResetPassword.name)
                }
            )
        }

        composable(route = FieldMateScreen.ResetPassword.name) {
            val viewModel: JoinViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ResetPasswordScreen(
                uiState = uiState,
                checkPassword = viewModel::checkPassword,
                checkConfirmPassword = viewModel::checkConfirmPassword,
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(FieldMateScreen.TaskList.name)
                }
            )
        }

        composable(route = FieldMateScreen.SelectCompany.name) {
            SelectCompanyScreen(
                joinCompanyBtnOnClick = { },
                addCompanyBtnOnClick = {
                    navController.navigate(FieldMateScreen.JoinCompany.name)
                }
            )
        }

        composable(route = FieldMateScreen.JoinCompany.name) {
            val viewModel: CompanyViewModel = hiltViewModel()
            val userInfo by authViewModel.userInfo.collectAsState()

            authViewModel.loadMyProfile()

            JoinCompanyScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                userInfo = userInfo,
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(FieldMateScreen.TaskList.name)
                }
            )
        }
    }
}

fun NavGraphBuilder.taskGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.TaskList.name,
        route = FieldMateScreen.TaskGraph.name
    ) {
        composable(route = FieldMateScreen.TaskList.name) {
            val viewModel: TaskListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userInfo by authViewModel.userInfo.collectAsState()

            authViewModel.loadMyProfile()

            TaskScreen(
                uiState = uiState,
                userInfo = userInfo,
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
                uiState = uiState,
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                selectImages = viewModel::selectImages,
                removeImage = viewModel::removeImage,
                confirmBtnOnClick = viewModel::createTask
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
                uiState = uiState,
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                selectImages = viewModel::selectImages,
                removeImage = viewModel::removeImage,
                confirmBtnOnClick = viewModel::createTask
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
                navController = navController,
                uiState = uiState
            )
        }
    }
}

fun NavGraphBuilder.clientGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.ClientList.name,
        route = FieldMateScreen.ClientGraph.name
    ) {
        composable(route = FieldMateScreen.ClientList.name) {
            val viewModel: ClientListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userInfo by authViewModel.userInfo.collectAsState()

            authViewModel.loadMyProfile()

            ClientScreen(
                uiState = uiState,
                userInfo = userInfo,
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
            val userInfo by authViewModel.userInfo.collectAsState()

            AddEditClientScreen(
                mode = EditMode.Add,
                uiState = uiState,
                userInfo = userInfo,
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
            val userInfo by authViewModel.userInfo.collectAsState()

            AddEditClientScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                userInfo = userInfo,
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
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadClient = viewModel::loadClient,
                navController = navController,
                addBtnOnClick = { navController.navigate(FieldMateScreen.AddBusiness.name) }
            )
        }
    }
}

fun NavGraphBuilder.businessGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.BusinessList.name,
        route = FieldMateScreen.BusinessGraph.name
    ) {
        composable(route = FieldMateScreen.BusinessList.name) {
            val viewModel: BusinessListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.AddBusiness.name) {
            val viewModel: BusinessViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userInfo by authViewModel.userInfo.collectAsState()

            AddEditBusinessScreen(
                mode = EditMode.Add,
                uiState = uiState,
                userInfo = userInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadMembers = viewModel::loadMembers,
                navController = navController,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                removeMember = viewModel::removeMember,
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
            val userInfo by authViewModel.userInfo.collectAsState()

            AddEditBusinessScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                userInfo = userInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadMembers = viewModel::loadMembers,
                navController = navController,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                removeMember = viewModel::removeMember,
                confirmBtnOnClick = viewModel::updateBusiness
            )
        }

        businessDetailGraph(navController, authViewModel)

        composable(route = FieldMateScreen.VisitGraph.name) {
            GraphScreen(navController = navController)
        }

        composable(route = FieldMateScreen.SummaryTask.name) {
            SummaryTaskScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.businessDetailGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = "${FieldMateScreen.DetailBusiness.name}/{businessId}",
        route = "${FieldMateScreen.BusinessDetailGraph.name}/{businessId}",
        arguments = listOf(
            navArgument("businessId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
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
            val userInfo by authViewModel.userInfo.collectAsState()

            DetailBusinessScreen(
                uiState = uiState,
                userInfo = userInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadBusiness = viewModel::loadBusiness,
                loadMembers = viewModel::loadMembers,
                navController = navController,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                removeMember = viewModel::removeMember,
                updateMembersBtnOnClick = viewModel::updateBusinessMembers
            )
        }

        composable(route = FieldMateScreen.BusinessMember.name) {
            BusinessMemberScreen(navController = navController)
        }

        composable(route = FieldMateScreen.SelectMember.name) {

        }
    }
}

fun NavGraphBuilder.memberGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.MemberList.name,
        route = FieldMateScreen.Member.name
    ) {
        composable(route = FieldMateScreen.MemberList.name) {
            val viewModel: MemberListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userInfo by authViewModel.userInfo.collectAsState()

            MemberScreen(
                uiState = uiState,
                userInfo = userInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMembers = viewModel::loadMembers,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.AddMember.name) {
            val viewModel: MemberViewModel = hiltViewModel()
            val userInfo by authViewModel.userInfo.collectAsState()

            AddMemberScreen(
                userInfo = userInfo,
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
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMember = viewModel::loadMember,
                navController = navController,
                confirmBtnOnClick = viewModel::updateProfile
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
            val userInfo by authViewModel.userInfo.collectAsState()

            DetailMemberScreen(
                uiState = uiState,
                userInfo = userInfo,
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadMember = viewModel::loadMember,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.settingGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = FieldMateScreen.SettingMenu.name,
        route = FieldMateScreen.Setting.name
    ) {
        composable(route = FieldMateScreen.SettingMenu.name) {
            SettingScreen(
                navController = navController,
                categoryBtnOnClick = {
                    navController.navigate(FieldMateScreen.Category.name)
                },
                resetPasswordBtnOnClick = {
                    navController.navigate(FieldMateScreen.ResetPassword.name)
                }
            )
        }

        composable(route = FieldMateScreen.Category.name) {
            val viewModel: CategoryViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val userInfo by authViewModel.userInfo.collectAsState()

            CategoryScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                loadCategories = viewModel::loadCategories,
                uiState = uiState,
                userInfo = userInfo,
                navController = navController,
                addCategory = viewModel::createTaskCategory,
                updateCategory = viewModel::updateTaskCategory,
                deleteCategory = viewModel::deleteTaskCategory
            )
        }
    }
}