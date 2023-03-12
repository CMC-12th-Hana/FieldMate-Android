package com.hana.fieldmate.data.local

import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.ui.theme.CategoryColor
import java.time.LocalDate

val fakeTaskDataSource = listOf(
    TaskEntity(
        id = 0,
        authorId = 0,
        client = "고객1",
        business = "사업1",
        title = "업무1",
        category = "A/S",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 1,
        authorId = 0,
        client = "고객2",
        business = "사업2",
        title = "업무2",
        category = "기술마케팅",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 2,
        authorId = 0,
        client = "고객3",
        business = "사업3",
        title = "업무3",
        category = "사전점검",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 3,
        authorId = 0,
        client = "고객4",
        business = "사업4",
        title = "업무4",
        category = "단순문의",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 4,
        authorId = 0,
        client = "고객5",
        business = "사업5",
        title = "업무5",
        category = "고객민원",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 5,
        authorId = 0,
        client = "고객6",
        business = "사업6",
        title = "업무6",
        category = "고객민원",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    ),
    TaskEntity(
        id = 6,
        authorId = 0,
        client = "고객7",
        business = "사업7",
        title = "업무7",
        category = "고객민원",
        categoryColor = CategoryColor[0],
        description = "ㅁㄴㅇㄹ"
    )
)

val fakeClientDataSource = listOf(
    ClientEntity(
        id = 0,
        name = "디지털리얼코리아",
        phone = "010-1234-5678",
        salesRepresentativeDepartment = "담당부서",
        salesRepresentativeName = "김부장",
        salesRepresentativePhone = "010-1234-5678",
        taskCount = 27,
        businessCount = 5
    ),
    ClientEntity(
        id = 1,
        name = "CMC",
        phone = "010-1234-5678",
        salesRepresentativeDepartment = "담당부서",
        salesRepresentativeName = "김부장",
        salesRepresentativePhone = "010-1234-5678",
        taskCount = 62,
        businessCount = 15
    ),
    ClientEntity(
        id = 2,
        name = "UMC",
        phone = "010-1234-5678",
        salesRepresentativeDepartment = "담당부서",
        salesRepresentativeName = "김부장",
        salesRepresentativePhone = "010-1234-5678",
        taskCount = 15,
        businessCount = 2
    ),
    ClientEntity(
        id = 3,
        name = "하나",
        phone = "010-1234-5678",
        salesRepresentativeDepartment = "담당부서",
        salesRepresentativeName = "김부장",
        salesRepresentativePhone = "010-1234-5678",
        taskCount = 234,
        businessCount = 7
    ),
)

val fakeMemberDataSource = listOf(
    MemberEntity(
        id = 0,
        name = "동현",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phoneNumber = "010-1234-5678",
        staffRank = "사원",
        staffNumber = "1234"
    ),
    MemberEntity(
        id = 1,
        name = "동쳔",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phoneNumber = "010-1234-5678",
        staffRank = "사원",
        staffNumber = "1234"
    ),
    MemberEntity(
        id = 2,
        name = "동챤",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phoneNumber = "010-1234-5678",
        staffRank = "사원",
        staffNumber = "1234"
    ),
    MemberEntity(
        id = 3,
        name = "똥쳔",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phoneNumber = "010-1234-5678",
        staffRank = "사원",
        staffNumber = "1234"
    ),
    MemberEntity(
        id = 4,
        name = "동치연",
        company = "하나",
        profileImg = R.drawable.ic_member_profile,
        phoneNumber = "010-1234-5678",
        staffRank = "사원",
        staffNumber = "1234"
    )
)

val fakeClientSelectionData = listOf(
    "디지털리얼코리아", "Central Makeus Challenge"
)

val fakeBusinessDataSource = listOf(
    BusinessEntity(
        id = 0,
        name = "사업명1",
        startDate = LocalDate.now().minusWeeks(4),
        endDate = LocalDate.now(),
        memberEntities = emptyList(),
        description = "",
        revenue = "10000"
    ),
    BusinessEntity(
        id = 1,
        name = "사업명2",
        startDate = LocalDate.now().minusWeeks(4),
        endDate = LocalDate.now(),
        memberEntities = emptyList(),
        description = "",
        revenue = "10000"
    ),
    BusinessEntity(
        id = 2,
        name = "사업명3",
        startDate = LocalDate.now().minusWeeks(4),
        endDate = LocalDate.now(),
        memberEntities = emptyList(),
        description = "",
        revenue = "10000"
    ),
    BusinessEntity(
        id = 3,
        name = "사업명4",
        startDate = LocalDate.now().minusWeeks(4),
        endDate = LocalDate.now(),
        memberEntities = emptyList(),
        description = "",
        revenue = "10000"
    ),
    BusinessEntity(
        id = 4,
        name = "사업명5",
        startDate = LocalDate.now().minusWeeks(4),
        endDate = LocalDate.now(),
        memberEntities = emptyList(),
        description = "",
        revenue = "10000"
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
