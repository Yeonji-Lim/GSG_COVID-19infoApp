package com.example.covid_19info.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.sql.Time
import java.util.*

//response>
//<header>
//<resultCode>00</resultCode>
//<resultMsg>NORMAL SERVICE.</resultMsg>
//</header>
//<body>
//<items>
//<item>
//<accDefRate>3.239660237</accDefRate>
//<accExamCnt>268212</accExamCnt>
//<accExamCompCnt>251940</accExamCompCnt>
//<careCnt>7280</careCnt>
//<clearCnt>807</clearCnt>
//<createDt>2020-03-15 00:00:00.000</createDt>
//<deathCnt>75</deathCnt>
//<decideCnt>8162</decideCnt>
//<examCnt>16272</examCnt>
//<resutlNegCnt>243778</resutlNegCnt>
//<seq>56</seq>
//<stateDt>20200315</stateDt>
//<stateTime>00:00</stateTime>
//<updateDt>2021-10-07 10:30:51.51</updateDt>
//</item>
//<item>
@Xml(name = "response")
data class QuarantinStat(
    @Element
    val header: HeaderQ,
    @Element
    val body: BodyQ
)

@Xml(name = "header")
data class HeaderQ(
    @PropertyElement
    val resultCode: Int,
    @PropertyElement
    val resultMsg: String
)

@Xml(name = "body")
data class  BodyQ(
    @Element(name = "items")
    val items: ItemsQ,
    @PropertyElement var numOfRows: Int,
    @PropertyElement var pageNo: Int,
    @PropertyElement var totalCount: Int
)
@Xml(name = "items")
data class ItemsQ(
    @Element(name = "item")
    val item: List<ItemQ>?
)

@Xml(name = "item")
data class ItemQ(
    @PropertyElement(name = "accDefRate") var accDefRate: String?,
    @PropertyElement(name = "accExamCnt") var accExamCnt: Int,
    @PropertyElement(name = "accExamCompCnt") var accExamCompCnt: Int,
    @PropertyElement(name = "careCnt") var careCnt: Int,
    @PropertyElement(name = "clearCnt") var clearCnt: Int,
    @PropertyElement(name = "createDt") var createDt: String,
    @PropertyElement(name = "deathCnt") var deathCnt: Int,
    @PropertyElement(name = "decideCnt") var decideCnt: Int,
    @PropertyElement(name = "examCnt") var examCnt: Int,
    @PropertyElement(name = "resutlNegCnt") var resutlNegCnt: Int,
    @PropertyElement(name = "seq") var seq: Int,
    @PropertyElement(name = "stateDt") var stateDt: String?,
    @PropertyElement(name = "stateTime") var stateTime: String?,
    @PropertyElement(name = "updateDt") var updateDt: String?,
)
//<item>
//<accDefRate>3.239660237</accDefRate>
//<accExamCnt>268212</accExamCnt>
//<accExamCompCnt>251940</accExamCompCnt>
//<careCnt>7280</careCnt>
//<clearCnt>807</clearCnt>
//<createDt>2020-03-15 00:00:00.000</createDt>
//<deathCnt>75</deathCnt>
//<decideCnt>8162</decideCnt>
//<examCnt>16272</examCnt>
//<resutlNegCnt>243778</resutlNegCnt>
//<seq>56</seq>
//<stateDt>20200315</stateDt>
//<stateTime>00:00</stateTime>
//<updateDt>2021-10-07 10:30:51.51</updateDt>
//</item>