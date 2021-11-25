package com.example.covid_19info.data.model

import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class VaccinatedNation(
    @Element(name = "body")
    val body: BodyV
)

@Xml(name = "body")
data class BodyV(
    @PropertyElement val dataTime: String,
    @Element(name = "items")
    var items: ItemsV
)

@Xml(name = "items")
data class ItemsV(
    @Element(name = "item")
    val item: List<ItemV>
)

@Xml(name = "item")
data class ItemV(
    @PropertyElement val tpcd: String,
    @PropertyElement val firstCnt: Int,
    @PropertyElement val secondCnt: Int,
    @PropertyElement val thirdCnt: Int
)