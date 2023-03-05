package com.hana.fieldmate.data.local

import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.domain.model.ReportEntity

val fakeReportDataSource = listOf(
    ReportEntity(
        id = 0,
        client = "고객1",
        business = "사업1",
        memberId = 0L,
        title = "업무1",
        category = "A/S",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 1,
        client = "고객2",
        business = "사업2",
        memberId = 1L,
        title = "업무2",
        category = "기술마케팅",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 2,
        client = "고객3",
        business = "사업3",
        memberId = 2L,
        title = "업무3",
        category = "사전점검",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 3,
        client = "고객4",
        business = "사업4",
        memberId = 3L,
        title = "업무4",
        category = "단순문의",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 4,
        client = "고객5",
        business = "사업5",
        memberId = 4L,
        title = "업무5",
        category = "고객민원",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 5,
        client = "고객6",
        business = "사업6",
        memberId = 2L,
        title = "업무6",
        category = "고객민원",
        content = "ㅁㄴㅇㄹ"
    ),
    ReportEntity(
        id = 6,
        client = "고객7",
        business = "사업7",
        memberId = 3L,
        title = "업무7",
        category = "고객민원",
        content = "ㅁㄴㅇㄹ"
    )
)

val fakeClientDataSource = listOf(
    ClientEntity(
        id = 0,
        name = "디지털리얼코리아",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 27,
        businessNum = 5
    ),
    ClientEntity(
        id = 1,
        name = "CMC",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 62,
        businessNum = 15
    ),
    ClientEntity(
        id = 2,
        name = "UMC",
        phone = "010-1234-5678",
        department = "담당부서",
        managerNm = "김부장",
        managerPhone = "010-1234-5678",
        visitNum = 15,
        businessNum = 2
    ),
    ClientEntity(
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

val fakeMemberDataSource = listOf(
    MemberEntity(
        id = 0,
        name = "동현",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    MemberEntity(
        id = 1,
        name = "동쳔",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    MemberEntity(
        id = 2,
        name = "동챤",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    MemberEntity(
        id = 3,
        name = "똥쳔",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    ),
    MemberEntity(
        id = 4,
        name = "동치연",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phone = "010-1234-5678",
        grade = "사원",
        memberNum = "1234"
    )
)

val fakeClientSelectionData = listOf(
    "디지털리얼코리아", "Central Makeus Challenge"
)

val fakeBusinessDataSource = listOf(
    BusinessEntity(
        id = 0,
        name = "사업명1",
        startDate = "23.01.02",
        endDate = "24.01.25",
        memberEntities = fakeMemberDataSource,
        content = "",
        profit = "10000"
    ),
    BusinessEntity(
        id = 1,
        name = "사업명2",
        startDate = "23.01.02",
        endDate = "23.01.25",
        memberEntities = fakeMemberDataSource,
        content = "",
        profit = "10000"
    ),
    BusinessEntity(
        id = 2,
        name = "사업명3",
        startDate = "23.01.02",
        endDate = "23.01.25",
        memberEntities = fakeMemberDataSource,
        content = "",
        profit = "10000"
    ),
    BusinessEntity(
        id = 3,
        name = "사업명4",
        startDate = "23.01.02",
        endDate = "23.01.25",
        memberEntities = fakeMemberDataSource,
        content = "",
        profit = "10000"
    ),
    BusinessEntity(
        id = 4,
        name = "사업명5",
        startDate = "23.01.02",
        endDate = "23.01.25",
        memberEntities = fakeMemberDataSource,
        content = "",
        profit = "10000"
    ),
)

val fakeBusinessSelectionData = listOf(
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
