package com.hana.fieldmate

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.ui.AuthViewModel

enum class EditMode {
    Add, Edit
}

enum class FieldMateScreen {
    AuthGraph,   // 로그인 그래프
    Login,  // 로그인 페이지
    Join,   // 회원가입 페이지
    SelectCompany,   // 새 회사 or 등록된 회사 합류 결정 페이지
    JoinCompany, // 새 회사 등록 페이지
    FindPassword,    // 비밀번호 찾기 페이지
    ResetPassword,  // 비밀번호 재설정 페이지

    Alarm,  // 알림 페이지

    TaskGraph,   // 업무 그래프
    TaskList,   // 업무 페이지
    AddTask,  // 사업보고서 추가 페이지
    EditTask, // 사업보고서 수정 페이지
    DetailTask, // 사업보고서 상세 페이지

    ClientGraph, // 고객 그래프
    ClientList,  // 고객 관리 페이지
    AddClient,    // 고객 추가 페이지
    EditClient,   // 고객 수정 페이지
    DetailClient, // 고객 상세정보 페이지

    BusinessGraph,   // 사업 그래프
    BusinessList,    // 사업 관리 페이지
    AddBusiness,    // 사업 추가 페이지

    EditBusiness,   // 사업 수정 페이지
    DetailBusiness,  // 사업 상세정보 페이지
    BusinessMember,    // 참여 구성원 페이지
    SelectMember,   // 참여 구성원 수정 페이지

    SummaryTask, // 업무 한눈에 보기 페이지

    Member, // 구성원 그래프
    MemberList,    // 구성원 페이지
    AddMember,  // 구성원 추가 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지

    Setting,    // 설정 그래프
    SettingMenu, // 환경 설정 페이지
    Category  // 카테고리명 수정 페이지
}

@Composable
fun FieldMateApp(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    //val isLoggedIn = userInfo.isLoggedIn
    val isLoggedIn = false
    val initialRoute =
        if (isLoggedIn) FieldMateScreen.TaskGraph.name else FieldMateScreen.AuthGraph.name

    NavHost(
        navController = navController,
        startDestination = initialRoute
    ) {
        loginGraph(navController, authViewModel)
        taskGraph(navController, authViewModel)
        clientGraph(navController, authViewModel)
        businessGraph(navController, authViewModel)
        memberGraph(navController, authViewModel)
        settingGraph(navController, authViewModel)
    }
}
