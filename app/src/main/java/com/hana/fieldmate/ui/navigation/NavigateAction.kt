package com.hana.fieldmate.ui.navigation

import android.os.Parcelable
import androidx.navigation.NavOptions

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

    MemberGraph, // 구성원 그래프
    MemberList,    // 구성원 페이지
    AddMember,  // 구성원 추가 페이지
    DetailMember,   // 구성원 상세보기
    EditMember,    // 프로필 수정 페이지

    SettingGraph,    // 설정 그래프
    SettingMenu, // 환경 설정 페이지
    Category,  // 카테고리명 수정 페이지
    ChangeLeader,    // 리더 수정
    ChangePassword,  // 비밀번호 재설정 페이지
    Withdrawal,  // 회원 탈퇴 페이지
    AppInfo, // 앱 정보 페이지
    TermsOfUse,   //  이용 약관 페이지
    PrivacyPolicy   // 개인 정보 처리 방침 페이지
}


interface NavigateAction {
    val destination: String
    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}

object NavigateActions {
    const val NavigateUp = "NavigateUp"

    fun navigateUp() = object : NavigateAction {
        override val destination: String
            get() = NavigateUp
    }

    fun backToLoginScreen() = object : NavigateAction {
        override val destination: String
            get() = FieldMateScreen.Login.name
        override val navOptions: NavOptions
            get() = NavOptions.Builder()
                .setPopUpTo(
                    route = destination,
                    inclusive = true,
                )
                .setLaunchSingleTop(true)
                .build()
    }

    object LoginScreen {
        fun toHomeScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.TaskGraph.name
        }

        fun toJoinScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.Join.name
        }

        fun toFindPasswordScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.FindPassword.name
        }

        fun toTermsOfUseScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.TermsOfUse.name
        }

        fun toPrivacyPolicyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.PrivacyPolicy.name
        }
    }

    object JoinScreen {
        fun toSelectCompanyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.SelectCompany.name
        }
    }

    object SelectCompanyScreen {
        fun toAddCompanyScreen() = object : NavigateAction {
            override val destination: String
                get() = FieldMateScreen.AddCompany.name
        }
    }

    object AddCompanyScreen {
        fun toOnBoardingScreen() = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.OnBoarding}"
            override val navOptions: NavOptions
                get() = NavOptions.Builder()
                    .setPopUpTo(
                        route = "${FieldMateScreen.Login}",
                        inclusive = true,
                    )
                    .build()
        }
    }

    object BusinessMemberScreen {
        fun toSelectBusinessMemberScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.SelectBusinessMember}/${businessId}"
        }

        fun toDetailMemberScreen(memberId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailMember}/${memberId}"
        }
    }

    object DetailBusinessScreen {
        fun toEditBusinessScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.EditBusiness}/${businessId}"
        }

        fun toBusinessMemberScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.BusinessMember}/${businessId}"
        }

        fun toBusinessTaskGraphScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.BusinessTaskGraph}/${businessId}"
        }

        fun toSummaryTaskScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.SummaryTask}/${businessId}"
        }
    }

    object DetailEtcBusinessScreen {
        fun toBusinessTaskGraphScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.BusinessTaskGraph}/${businessId}"
        }

        fun toBusinessSummaryTaskScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.SummaryTask}/${businessId}"
        }
    }

    object SummaryTaskScreen {
        fun toDetailTaskScreen(taskId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailTask}/${taskId}"

        }
    }

    object ClientScreen {
        fun toAddClientScreen() = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.AddClient}"

        }
    }

    object DetailClientScreen {
        fun toEditClientScreen(clientId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.EditClient}/${clientId}"
        }

        fun toClientTaskGraphScreen(clientId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.ClientTaskGraph}/${clientId}"

        }

        fun toAddBusinessScreen(clientId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.AddBusiness}/${clientId}"
        }

        fun toDetailEtcBusinessScreen(etcBusinessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailBusiness}/${etcBusinessId}"
        }

        fun toDetailBusinessScreen(businessId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailBusiness}/${businessId}"
        }
    }

    object MemberScreen {
        fun toDetailMemberScreen(memberId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailMember}/${memberId}"

        }
    }

    object DetailMemberScreen {
        fun toEditMemberScreen(memberId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.EditMember}/${memberId}"
        }
    }

    object TaskScreen {
        fun toAddTaskScreen() = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.AddTask}"
        }

        fun toDetailTaskScreen(taskId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.DetailTask}/${taskId}"
        }

        fun toSettingScreen() = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.SettingGraph}"
        }
    }

    object DetailTaskScreen {
        fun toEditTaskScreen(taskId: Long) = object : NavigateAction {
            override val destination: String
                get() = "${FieldMateScreen.EditTask}/${taskId}"
        }
    }
}