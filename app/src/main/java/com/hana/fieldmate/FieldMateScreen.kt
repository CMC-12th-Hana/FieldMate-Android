package com.hana.fieldmate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hana.fieldmate.ui.AuthViewModel
import com.hana.fieldmate.ui.auth.*
import com.hana.fieldmate.ui.business.*
import com.hana.fieldmate.ui.client.*
import com.hana.fieldmate.ui.member.*
import com.hana.fieldmate.ui.report.*
import com.hana.fieldmate.ui.setting.CategoryScreen
import com.hana.fieldmate.ui.setting.CategoryViewModel
import com.hana.fieldmate.ui.setting.SettingScreen

enum class EditMode {
    Add, Edit
}

enum class UserMode {
    Employee, Leader
}

enum class FieldMateScreen {
    Login,  // 로그인 페이지
    Join,   // 회원가입 페이지
    SelectCompany,   // 새 회사 or 등록된 회사 합류 결정 페이지
    JoinCompany, // 새 회사 등록 페이지
    FindPassword,    // 비밀번호 찾기 페이지
    ResetPassword,  // 비밀번호 재설정 페이지

    Alarm,  // 알림 페이지

    Report,   // 업무 페이지
    AddReport,  // 사업보고서 추가 페이지
    EditReport, // 사업보고서 수정 페이지
    DetailReport, // 사업보고서 상세 페이지

    Client,  // 고객 관리 페이지
    AddClient,    // 고객 추가 페이지
    EditClient,   // 고객 수정 페이지
    LeaderEditClient,    // 리더 고객 수정 페이지
    DetailClient, // 고객 상세정보 페이지

    Business,    // 사업 관리 페이지
    AddBusiness,    // 사업 추가 페이지
    EditBusiness,   // 사업 수정 페이지
    LeaderEditBusiness, // 리더 사업 수정 페이지
    DetailBusiness,  // 사업 상세정보 페이지
    BusinessMember,    // 참여 구성원 페이지
    SummaryReport, // 업무 한눈에 보기 페이지
    VisitGraph, // 방문 건수 그래프 페이지

    Member,    // 구성원 페이지
    AddMember,  // 구성원 추가 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지

    Setting, // 환경 설정 페이지
    Category  // 카테고리명 수정 페이지
}

@Composable
fun FieldMateApp() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val userInfo by authViewModel.userInfo.collectAsState()

    //val isLoggedIn = userInfo.isLoggedIn
    val isLoggedIn = false
    val initialRoute = if (isLoggedIn) FieldMateScreen.Report.name else FieldMateScreen.Login.name

    NavHost(
        navController = navController,
        startDestination = initialRoute,
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
                checkCertNumber = viewModel::checkCertNumber,
                setTimer = viewModel::setTimer,
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
                    navController.navigate(FieldMateScreen.Report.name)
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

            authViewModel.loadMyProfile()

            JoinCompanyScreen(
                eventsFlow = viewModel.eventsFlow,
                sendEvent = viewModel::sendEvent,
                userInfo = userInfo,
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(FieldMateScreen.Report.name)
                }
            )
        }

        // 로그인 시 앱의 시작점
        composable(route = FieldMateScreen.Report.name) {
            val viewModel: ReportListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            authViewModel.loadMyProfile()

            ReportScreen(
                uiState = uiState,
                userInfo = userInfo,
                navController = navController,
                addBtnOnClick = {
                    navController.navigate(FieldMateScreen.AddReport.name)
                }
            )
        }

        composable(route = FieldMateScreen.AddReport.name) {
            val viewModel: ReportViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditReportScreen(
                mode = EditMode.Add,
                uiState = uiState,
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                selectImages = viewModel::selectImages,
                removeImage = viewModel::removeImage,
                confirmBtnOnClick = { }
            )
        }

        composable(
            route = "${FieldMateScreen.EditReport.name}/{reportId}",
            arguments = listOf(
                navArgument("reportId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: ReportViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditReportScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                selectImages = viewModel::selectImages,
                removeImage = viewModel::removeImage,
                confirmBtnOnClick = { }
            )
        }

        composable(
            route = "${FieldMateScreen.DetailReport.name}/{reportId}",
            arguments = listOf(
                navArgument("reportId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: ReportViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailReportScreen(
                navController = navController,
                uiState = uiState
            )
        }

        composable(route = FieldMateScreen.Client.name) {
            val viewModel: ClientListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

        composable(route = FieldMateScreen.Business.name) {
            val viewModel: BusinessListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.AddBusiness.name) {
            val viewModel: BusinessViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditBusinessScreen(
                mode = EditMode.Add,
                uiState = uiState,
                navController = navController,
                addMemberBtnOnClick = viewModel::selectedMembers,
                confirmBtnOnClick = { }
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
            val viewModel: BusinessViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddEditBusinessScreen(
                mode = EditMode.Edit,
                uiState = uiState,
                navController = navController,
                addMemberBtnOnClick = viewModel::selectedMembers,
                confirmBtnOnClick = { }
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
            val viewModel: BusinessViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailBusinessScreen(
                uiState = uiState,
                navController = navController,
                addMemberBtnOnClick = viewModel::selectedMembers
            )
        }

        composable(route = FieldMateScreen.BusinessMember.name) {
            BusinessMemberScreen(navController = navController)
        }

        composable(route = FieldMateScreen.VisitGraph.name) {
            GraphScreen(navController = navController)
        }

        composable(route = FieldMateScreen.SummaryReport.name) {
            SummaryReportScreen(navController = navController)
        }

        composable(route = FieldMateScreen.Member.name) {
            val viewModel: MemberListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MemberScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.AddMember.name) {
            AddMemberScreen(
                navController = navController,
                confirmBtnOnClick = { }
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
            val viewModel: MemberViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            EditMemberScreen(
                uiState = uiState,
                navController = navController,
                confirmBtnOnClick = { }
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
            val viewModel: MemberViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailMemberScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(route = FieldMateScreen.Setting.name) {
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
