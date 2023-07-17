package com.hana.fieldmate

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.ui.navigation.*
import kotlinx.coroutines.flow.Flow

enum class EditMode {
    Add, Edit
}

enum class FieldMateScreen {
    SplashGraph,    // 스플래시 그래프
    Splash, // 스플래시 화면
    OnBoarding, // 온보딩 화면

    AuthGraph,   // 로그인 그래프
    Login,  // 로그인 페이지
    Join,   // 회원가입 페이지
    SelectCompany,   // 새 회사 or 등록된 회사 합류 결정 페이지
    AddCompany, // 새 회사 등록 페이지
    FindPassword,    // 비밀번호 찾기 페이지

    Alarm,  // 알림 페이지

    TaskGraph,   // 업무 그래프
    TaskList,   // 업무 페이지
    AddTask,  // 사업보고서 추가 페이지
    EditTask, // 사업보고서 수정 페이지
    DetailTask, // 사업보고서 상세 페이지

    ClientGraph, // 고객 그래프
    ClientTaskGraph,   // 고객 업무 현황 그래프
    ClientList,  // 고객 관리 페이지
    AddClient,    // 고객 추가 페이지
    EditClient,   // 고객 수정 페이지
    DetailClient, // 고객 상세정보 페이지

    BusinessGraph,   // 사업 그래프
    BusinessTaskGraph,   // 사업 업무 현황 그래프
    BusinessList,    // 사업 관리 페이지
    AddBusiness,    // 사업 추가 페이지

    EditBusiness,   // 사업 수정 페이지
    DetailBusiness,  // 사업 상세정보 페이지
    DetailEtcBusiness,  // 사업 기타 상세정보 페이지
    BusinessMember,    // 참여 구성원 페이지
    SelectBusinessMember,   // 참여 구성원 수정 페이지

    SummaryTask, // 업무 한눈에 보기 페이지

    Member, // 구성원 그래프
    MemberList,    // 구성원 페이지
    AddMember,  // 구성원 추가 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지

    Setting,    // 설정 그래프
    SettingMenu, // 환경 설정 페이지
    Category,  // 카테고리명 수정 페이지
    ChangeLeader,    // 리더 수정
    ChangePassword,  // 비밀번호 재설정 페이지
    Withdrawal,  // 회원 탈퇴 페이지
    AppInfo, // 앱 정보 페이지
    TermsOfUse,   //  이용 약관 페이지
    PrivacyPolicy   // 개인 정보 처리 방침 페이지
}

@Composable
fun FieldMateApp(navigator: ComposeCustomNavigator) {
    val navController = rememberNavController()

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navController.navigate(it.destination, it.navOptions)
        }
    }

    NavHost(
        navController = navController,
        startDestination = FieldMateScreen.SplashGraph.name
    ) {
        splashGraph(navController)
        authGraph(navController)
        taskGraph(navController)
        clientGraph(navController)
        businessGraph(navController)
        memberGraph(navController)
        settingGraph(navController)
    }
}

@Composable
fun <T> Flow<T>.asLifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    initialState: T
): State<T> {
    return lifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        flow = this,
        initialState = initialState
    )
}

@Composable
fun <T> lifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    initialState: T
): State<T> {
    val lifecycleAwareStateFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    return lifecycleAwareStateFlow.collectAsState(initialState)
}