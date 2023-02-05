package com.hana.umuljeong

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hana.umuljeong.ui.*
import com.hana.umuljeong.ui.component.*

enum class UmuljeongScreen() {
    Login,  // 로그인 페이지
    Register,   // 회원가입 페이지
    SelectCompany,   // 새 회사 or 등록된 회사 합류 결정 페이지
    AddCompany, // 새 회사 등록 페이지
    FindPassword,    // 비밀번호 찾기 페이지
    VerifyEmail,  // 이메일을 통한 인증번호 페이지

    Alarm,  // 알림 페이지

    Home,   // 캘린더와 업무 작성 가능한 페이지
    AddReport,  // 사업보고서 추가 페이지
    DetailReport, // 사업보고서 상세 페이지
    EditReport, // 사업보고서 수정 페이지

    Customer,  // 고객 관리 페이지
    CustomerList,   // 고객 리스트 페이지
    AddCustomer,    // 고객 추가 페이지
    EditCustomer,   // 고객 수정 페이지
    CustomerDetail, // 고객 상세정보 페이지

    BusinessList,   // 사업 리스트 페이지
    AddBusiness,    // 사업 추가 페이지
    EditBusiness,   // 사업 수정 페이지
    BusinessDetail,  // 사업 상세정보 페이지
    BusinessSummary, // 영업 사원 한눈에 보기 페이지

    Bookmark,    // 북마크 페이지

    Profile,    // 프로필 페이지
    ProfileEdit,    // 프로필 수정 페이지
    EmployeeManagement // 사원 관리 페이지
}

@Composable
fun UmuljeongApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: UmuljeongScreen.Login.name

    var showBottomBar by rememberSaveable { (mutableStateOf(false)) }
    var showTopBar by rememberSaveable { mutableStateOf(false) }
    var topBarStatus by rememberSaveable { mutableStateOf(AppBarStatus.AppBarWithBackBtn) }
    var title by rememberSaveable { mutableStateOf(R.string.home) }

    when (currentScreen) {
        UmuljeongScreen.Login.name -> {
            showBottomBar = false
            showTopBar = false
        }
        UmuljeongScreen.Register.name -> {
            showBottomBar = false
            showTopBar = true
            topBarStatus = AppBarStatus.AppBarWithBackBtn
            title = R.string.register
        }
        UmuljeongScreen.Home.name -> {
            showBottomBar = true
            showTopBar = false
        }
        UmuljeongScreen.SelectCompany.name -> {
            showBottomBar = false
            showTopBar = false
        }
        UmuljeongScreen.AddCompany.name -> {
            showBottomBar = false
            showTopBar = true
            topBarStatus = AppBarStatus.AppBarWithBackBtn
            title = R.string.add_company
        }
        UmuljeongScreen.AddReport.name -> {
            showBottomBar = false
            showTopBar = true
            topBarStatus = AppBarStatus.AppBarWithBackBtn
            title = R.string.add_report
        }
    }

    if (currentScreen.startsWith("${UmuljeongScreen.DetailReport.name}/")) {
        showBottomBar = false
        showTopBar = true
        topBarStatus = AppBarStatus.AppBarWithEditBtn
        title = R.string.detail_report
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                when (topBarStatus) {
                    AppBarStatus.AppBarWithBackBtn -> {
                        UAppBarWithBackBtn(
                            title = title,
                            backBtnOnClick = { navController.navigateUp() }
                        )
                    }
                    AppBarStatus.AppBarWithEditBtn -> {
                        UAppBarWithEditBtn(
                            title = title,
                            backBtnOnClick = { navController.navigateUp() },
                            editBtnOnClick = { }
                        )
                    }
                    AppBarStatus.AppBarWithExitBtn -> {
                        UAppBarWithExitBtn(
                            title = title,
                            exitBtnOnClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                UBottomBar(
                    navController = navController
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = UmuljeongScreen.Login.name,
            modifier = modifier.padding(innerPadding)
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
                RegisterScreen(
                    registerBtnOnClick = {
                        navController.navigate(UmuljeongScreen.SelectCompany.name)
                    }
                )
            }

            composable(route = UmuljeongScreen.SelectCompany.name) {
                SelectCompanyScreen(
                    joinCompanyBtnOnClick = { },
                    addCompanyBtnOnClick = {
                        navController.navigate(UmuljeongScreen.AddCompany.name)
                    }
                )
            }

            composable(route = UmuljeongScreen.AddCompany.name) {
                AddCompanyScreen(
                    confirmBtnOnClick = {
                        navController.navigate(UmuljeongScreen.Home.name)
                    }
                )
            }

            composable(route = UmuljeongScreen.Home.name) {
                HomeScreen(
                    navController = navController,
                    addBtnOnClick = {
                        navController.navigate(UmuljeongScreen.AddReport.name)
                    }
                )
            }

            composable(route = UmuljeongScreen.AddReport.name) {
                AddReportScreen(
                    addPhotoBtnOnClick = { },
                    addBtnOnClick = { }
                )
            }

            composable(
                route = "${UmuljeongScreen.DetailReport.name}/{reportId}",
                arguments = listOf(
                    navArgument("reportId") {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) { backStackEntry ->
                DetailReportScreen(reportId = backStackEntry.arguments!!.getLong("reportId"))
            }


            composable(route = UmuljeongScreen.Customer.name) {

            }

            composable(route = UmuljeongScreen.Bookmark.name) {

            }

            composable(route = UmuljeongScreen.Profile.name) {

            }
        }
    }

}