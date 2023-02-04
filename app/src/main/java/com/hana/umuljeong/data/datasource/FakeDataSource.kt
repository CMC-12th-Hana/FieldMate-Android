package com.hana.umuljeong.data.datasource

import com.hana.umuljeong.data.model.Report

val fakeReportData = listOf(
    Report(id = 0, author = "황진하", name = "업무1", category = "A/S", content = "ㅁㄴㅇㄹ"),
    Report(id = 1, author = "황진하", name = "업무2", category = "기술마케팅", content = "ㅁㄴㅇㄹ"),
    Report(id = 2, author = "황진하", name = "업무3", category = "사전점검", content = "ㅁㄴㅇㄹ"),
    Report(id = 3, author = "황진하", name = "업무4", category = "단순문의", content = "ㅁㄴㅇㄹ"),
    Report(id = 4, author = "황진하", name = "업무5", category = "고객민원", content = "ㅁㄴㅇㄹ"),
)

val fakeBussinessData = listOf(
    "디지툴리얼코리아 전용 회선", "CMC 12기 하나팀 화이팅", "UI 개발하기가 제일 힘들다"
)

val fakeCategoryData = listOf(
    "단순문의", "고객민원", "A/S", "기술마케팅", "사전점검"
)