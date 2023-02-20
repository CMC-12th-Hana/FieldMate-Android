package com.hana.umuljeong

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hana.umuljeong.ui.HomeScreen
import com.hana.umuljeong.ui.auth.*
import com.hana.umuljeong.ui.business.AddBusinessScreen
import com.hana.umuljeong.ui.business.BusinessListViewModel
import com.hana.umuljeong.ui.business.BusinessScreen
import com.hana.umuljeong.ui.business.SelectMemberScreen
import com.hana.umuljeong.ui.company.*
import com.hana.umuljeong.ui.member.*
import com.hana.umuljeong.ui.report.*
import com.hana.umuljeong.ui.setting.CategoryScreen
import com.hana.umuljeong.ui.setting.SettingScreen

enum class UmuljeongScreen {
    Login,  // 로그인 페이지
    Register,   // 회원가입 페이지
    SelectMyCompany,   // 새 회사 or 등록된 회사 합류 결정 페이지
    AddMyCompany, // 새 회사 등록 페이지
    FindPassword,    // 비밀번호 찾기 페이지
    VerifyEmail,  // 이메일을 통한 인증번호 페이지

    Alarm,  // 알림 페이지

    Home,   // 캘린더와 업무 작성 가능한 페이지
    AddReport,  // 사업보고서 추가 페이지
    DetailReport, // 사업보고서 상세 페이지
    EditReport, // 사업보고서 수정 페이지

    PickImage,  // 사진 선택

    Company,  // 기업 관리 페이지
    AddCompany,    // 기업 추가 페이지
    EditCompany,   // 기업 수정 페이지
    DetailCompany, // 기업 상세정보 페이지

    Business,    // 사업 관리 페이지
    AddBusiness,    // 사업 추가 페이지
    SelectMember,   // 참여 구성원 선택 페이지
    EditBusiness,   // 사업 수정 페이지
    BusinessDetail,  // 사업 상세정보 페이지
    BusinessSummary, // 영업 사원 한눈에 보기 페이지

    Member,    // 구성원 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지
    EmployeeManagement, // 사원 관리 페이지

    Setting, // 환경 설정 페이지
    Category  // 카테고리명 수정 페이지
}

@Composable
fun UmuljeongApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UmuljeongScreen.Login.name,
        modifier = modifier
    ) {
        composable(route = UmuljeongScreen.Login.name) {
            LoginScreen(
                loginBtnOnClick = {
                    navController.navigate(UmuljeongScreen.Home.name)
                },
                findPwBtnOnClick = {

                },
                registerBtnOnClick = {
                    navController.navigate(UmuljeongScreen.Register.name)
                }
            )
        }

        composable(route = UmuljeongScreen.Register.name) {
            val viewModel: RegisterViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            RegisterScreen(
                uiState = uiState,
                navController = navController,
                checkName = viewModel::checkName,
                checkEmail = viewModel::checkEmail,
                checkPhone = viewModel::checkPhone,
                checkCertNumber = viewModel::checkCertNumber,
                setTimer = viewModel::setTimer,
                checkPassword = viewModel::checkPassword,
                checkConfirmPassword = viewModel::checkConfirmPassword,
                checkRegisterEnabled = viewModel::checkRegisterEnabled,
                registerBtnOnClick = {
                    navController.navigate(UmuljeongScreen.SelectMyCompany.name)
                }
            )
        }

        composable(route = UmuljeongScreen.SelectMyCompany.name) {
            SelectMyCompanyScreen(
                joinCompanyBtnOnClick = { },
                addCompanyBtnOnClick = {
                    navController.navigate(UmuljeongScreen.AddMyCompany.name)
                }
            )
        }

        composable(route = UmuljeongScreen.AddMyCompany.name) {
            AddMyCompanyScreen(
                navController = navController,
                confirmBtnOnClick = {
                    navController.navigate(UmuljeongScreen.Home.name)
                }
            )
        }

        composable(route = UmuljeongScreen.Home.name) {
            val viewModel: ReportListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                navController = navController,
                addBtnOnClick = {
                    navController.navigate(UmuljeongScreen.AddReport.name)
                }
            )
        }

        composable(route = UmuljeongScreen.AddReport.name) {
            val viewModel: ReportViewModel = viewModel()

            AddReportScreen(
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                addPhotoBtnOnClick = viewModel::selectImages,
                addBtnOnClick = { }
            )
        }

        composable(
            route = "${UmuljeongScreen.DetailReport.name}/{reportId}",
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

        composable(
            route = "${UmuljeongScreen.EditReport.name}/{reportId}",
            arguments = listOf(
                navArgument("reportId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: ReportViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            EditReportScreen(
                uiState = uiState,
                selectedImageList = viewModel.selectedImageList,
                navController = navController,
                addPhotoBtnOnClick = viewModel::selectImages,
                confirmBtnOnClick = { }
            )
        }

        composable(route = UmuljeongScreen.Company.name) {
            val viewModel: CompanyListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CompanyScreen(
                uiState = uiState,
                navController = navController,
                addBtnOnClick = { navController.navigate(UmuljeongScreen.AddCompany.name) }
            )
        }

        composable(route = UmuljeongScreen.AddCompany.name) {
            AddCompanyScreen(
                navController = navController,
                confirmBtnOnClick = { }
            )
        }

        composable(route = "${UmuljeongScreen.EditCompany.name}/{companyId}",
            arguments = listOf(
                navArgument("companyId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: CompanyViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            EditCompanyScreen(
                uiState = uiState,
                navController = navController,
                confirmBtnOnClick = { }
            )
        }


        composable(
            route = "${UmuljeongScreen.DetailCompany.name}/{companyId}",
            arguments = listOf(
                navArgument("companyId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: CompanyViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailCompanyScreen(
                uiState = uiState,
                navController = navController,
                addBtnOnClick = { navController.navigate(UmuljeongScreen.AddBusiness.name) }
            )
        }

        composable(route = UmuljeongScreen.Business.name) {
            val viewModel: BusinessListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BusinessScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(route = UmuljeongScreen.AddBusiness.name) {
            AddBusinessScreen(
                navController = navController,
                addMemberBtnOnClick = { navController.navigate(UmuljeongScreen.SelectMember.name) },
                addBtnOnClick = { }
            )
        }

        composable(route = UmuljeongScreen.SelectMember.name) {
            SelectMemberScreen(
                navController = navController,
                onSelected = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = UmuljeongScreen.Member.name) {
            val viewModel: MemberListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MemberScreen(
                uiState = uiState,
                navController = navController
            )
        }

        composable(
            route = "${UmuljeongScreen.DetailMember.name}/{memberId}",
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

        composable(
            route = "${UmuljeongScreen.EditMember.name}/{memberId}",
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

        composable(route = UmuljeongScreen.Setting.name) {
            SettingScreen(
                navController = navController,
                categoryBtnOnClick = {
                    navController.navigate(UmuljeongScreen.Category.name)
                }
            )
        }

        composable(route = UmuljeongScreen.Category.name) {
            CategoryScreen(navController = navController)
        }
    }

}
