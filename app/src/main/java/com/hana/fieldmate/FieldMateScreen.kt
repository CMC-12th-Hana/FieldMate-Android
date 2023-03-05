package com.hana.fieldmate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hana.fieldmate.ui.auth.*
import com.hana.fieldmate.ui.business.*
import com.hana.fieldmate.ui.client.*
import com.hana.fieldmate.ui.member.*
import com.hana.fieldmate.ui.report.*
import com.hana.fieldmate.ui.setting.CategoryScreen
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
    AddCompany, // 새 회사 등록 페이지
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
    LeaderMember,   // 리더 구성원 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지

    Setting, // 환경 설정 페이지
    Category,  // 카테고리명 수정 페이지
    LeaderCategory  // 리더 카테고리 페이지
}

@Composable
fun FieldMateApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FieldMateScreen.Login.name,
        modifier = modifier
    ) {
        composable(route = FieldMateScreen.Login.name) {
            val viewModel: LoginViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                uiState = uiState,
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
                    navController.navigate(FieldMateScreen.AddCompany.name)
                }
            )
        }

        composable(route = FieldMateScreen.AddCompany.name) {
            JoinCompanyScreen(
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(FieldMateScreen.Report.name)
                }
            )
        }

        composable(route = FieldMateScreen.Report.name) {
            val viewModel: ReportListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ReportScreen(
                uiState = uiState,
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
            val viewModel: ClientListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ClientScreen(
                uiState = uiState,
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
                navController = navController,
                confirmBtnOnClick = { }
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
                navController = navController,
                confirmBtnOnClick = { }
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

            AddEditMemberScreen(
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
            CategoryScreen(navController = navController)
        }
    }

}
