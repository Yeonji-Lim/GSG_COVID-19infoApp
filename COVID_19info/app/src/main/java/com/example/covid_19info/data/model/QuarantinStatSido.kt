package com.example.covid_19info.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


@Xml(name = "response")
data class QuarantinStatSido(
    @Element
    val header: HeaderQS,
    @Element
    val body: BodyQS
)


@Xml(name = "header")
data class HeaderQS(
    @PropertyElement
    val resultCode: Int,
    @PropertyElement
    val resultMsg: String
)

@Xml(name="body")
data class BodyQS(
    @Element(name = "items")
    val items: ItemsQS,
    @PropertyElement
    val numOfRows: Int,
    @PropertyElement
    val pageNo: Int,
    @PropertyElement
    val totalCount: Int
)
@Xml(name="items")
data class ItemsQS(
    @Element(name = "item")
    val item: List<ItemQS>
)

@Xml(name = "item")
data class ItemQS(
    @PropertyElement val createDt: String,
    @PropertyElement val deathCnt: Int,
    @PropertyElement val defCnt: Int,
    @PropertyElement val gubun: String,
    @PropertyElement val gubunCn: String,
    @PropertyElement val gubunEn: String,
    @PropertyElement val incDec: Int,
    @PropertyElement val isolClearCnt: Int,
    @PropertyElement val localOccCnt: Int,
    @PropertyElement val isolIngCnt: Int,
    @PropertyElement val overFlowCnt: Int,
    @PropertyElement val qurRate: String,
    @PropertyElement val seq: Int,
    @PropertyElement val stdDay: String,
    @PropertyElement val updateDt: String
)