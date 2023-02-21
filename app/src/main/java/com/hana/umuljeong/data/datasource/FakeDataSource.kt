package com.hana.umuljeong.data.datasource

import com.hana.umuljeong.R
import com.hana.umuljeong.data.model.Business
import com.hana.umuljeong.data.model.Company
import com.hana.umuljeong.data.model.Member
import com.hana.umuljeong.data.model.Report

val fakeReportData = listOf(
    Report(id = 0, customer = "고객1", name = "업무1", category = "A/S", content = "ㅁㄴㅇㄹ"),
    Report(id = 1, customer = "고객2", name = "업무2", category = "기술마케팅", content = "ㅁㄴㅇㄹ"),
    Report(id = 2, customer = "고객3", name = "업무3", category = "사전점검", content = "ㅁㄴㅇㄹ"),
    Report(id = 3, customer = "고객4", name = "업무4", category = "단순문의", content = "ㅁㄴㅇㄹ"),
    Report(id = 4, customer = "고객5", name = "업무5", category = "고객민원", content = "ㅁㄴㅇㄹ"),
    Report(id = 5, customer = "고객6", name = "업무6", category = "고객민원", content = "ㅁㄴㅇㄹ"),
    Report(id = 6, customer = "고객7", name = "업무7", category = "고객민원", content = "ㅁㄴㅇㄹ")
)

val fakeCompanyData = listOf(
    Company(
        id = 0,
        name = "디지털리얼코리아",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 27,
        businessNum = 5
    ),
    Company(
        id = 1,
        name = "CMC",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 62,
        businessNum = 15
    ),
    Company(
        id = 2,
        name = "UMC",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 15,
        businessNum = 2
    ),
    Company(
        id = 3,
        name = "하나",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 234,
        businessNum = 7
    ),
)

val fakeMemberData = listOf(
    Member(
        id = 0,
        name = "동현",
        profileImg = R.drawable.ic_member_profile,
        email = "kimdonghyun90766@gmail.com",
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    Member(
        id = 1,
        name = "동쳔",
        profileImg = R.drawable.ic_member_profile,
        email = "kimdonghyun90766@gmail.com",
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    Member(
        id = 2,
        name = "동챤",
        profileImg = R.drawable.ic_member_profile,
        email = "kimdonghyun90766@gmail.com",
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    Member(
        id = 3,
        name = "똥쳔",
        profileImg = R.drawable.ic_member_profile,
        email = "kimdonghyun90766@gmail.com",
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    Member(
        id = 4,
        name = "동치연",
        profileImg = R.drawable.ic_member_profile,
        email = "kimdonghyun90766@gmail.com",
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    )
)

val fakeCompanySelectionData = listOf(
    "디지털리얼코리아", "Central Makeus Challenge"
)

val fakeBusinessData = listOf(
    Business(
        id = 0,
        name = "사업명1",
        startDate = "23.01.02",
        endDate = "24.01.25",
        members = fakeMemberData,
        profit = "10000"
    ),
    Business(
        id = 1,
        name = "사업명2",
        startDate = "23.01.02",
        endDate = "23.01.25",
        members = fakeMemberData,
        profit = "10000"
    ),
    Business(
        id = 2,
        name = "사업명3",
        startDate = "23.01.02",
        endDate = "23.01.25",
        members = fakeMemberData,
        profit = "10000"
    ),
    Business(
        id = 3,
        name = "사업명4",
        startDate = "23.01.02",
        endDate = "23.01.25",
        members = fakeMemberData,
        profit = "10000"
    ),
    Business(
        id = 4,
        name = "사업명5",
        startDate = "23.01.02",
        endDate = "23.01.25",
        members = fakeMemberData,
        profit = "10000"
    ),
)

val fakeBussinessSelectionData = listOf(
    "디지털리얼코리아 전용 회선", "CMC 12기 하나팀 화이팅", "UI 개발하기가 제일 힘들다"
)

val fakeCategorySelectionData = listOf(
    "고객민원", "A/S", "단순문의", "기술마케팅", "사전점검",
)

val fakeVisitData = listOf(
    Pair("고객민원", 12),
    Pair("A/S", 13),
    Pair("단순문의", 8),
    Pair("기술마케팅", 10),
    Pair("사전점검", 2)
)