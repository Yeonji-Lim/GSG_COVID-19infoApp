package com.example.covid_19info

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.covid_19info.data.StatisticAPI
import com.example.covid_19info.data.model.vaccinatedData
import com.example.covid_19info.databinding.ActivityParsingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xmlpull.v1.XmlPullParserException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.xml.parsers.DocumentBuilderFactory

class ParsingActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityParsingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParsingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_parsing)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val API = StatisticAPI
        var elem : Element? = null
        CoroutineScope(Dispatchers.Default).launch {
            CoroutineScope(Dispatchers.IO).async {
                elem = getInfected(API, "1", "18", "20211118", "20211118")
            }.await()
            parsingData(elem)
        }
        //parsingVaccinated(API, "20211118")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_parsing)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun parsingVaccinated(API: StatisticAPI, startCreateDt: String) {
        //val vaccinated = API.get_vaccinated(startCreateDt)
        val jsonstring =
            "{\"currentCount\":18,\"data\":[{\"accumulatedFirstCnt\":41905044,\"accumulatedSecondCnt\":39998292,\"accumulatedThirdCnt\":986583,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":55234,\"id\":4471,\"secondCnt\":100839,\"sido\":\"전국\",\"thirdCnt\":50403,\"totalFirstCnt\":41960278,\"totalSecondCnt\":40099131,\"totalThirdCnt\":1036986},{\"accumulatedFirstCnt\":7816395,\"accumulatedSecondCnt\":7495715,\"accumulatedThirdCnt\":196394,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":9722,\"id\":4472,\"secondCnt\":17188,\"sido\":\"서울특별시\",\"thirdCnt\":10544,\"totalFirstCnt\":7826117,\"totalSecondCnt\":7512903,\"totalThirdCnt\":206938},{\"accumulatedFirstCnt\":2682830,\"accumulatedSecondCnt\":2564617,\"accumulatedThirdCnt\":48096,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":2924,\"id\":4473,\"secondCnt\":6198,\"sido\":\"부산광역시\",\"thirdCnt\":2487,\"totalFirstCnt\":2685754,\"totalSecondCnt\":2570815,\"totalThirdCnt\":50583},{\"accumulatedFirstCnt\":1866812,\"accumulatedSecondCnt\":1778216,\"accumulatedThirdCnt\":32075,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1932,\"id\":4474,\"secondCnt\":4641,\"sido\":\"대구광역시\",\"thirdCnt\":1814,\"totalFirstCnt\":1868744,\"totalSecondCnt\":1782857,\"totalThirdCnt\":33889},{\"accumulatedFirstCnt\":2385231,\"accumulatedSecondCnt\":2274085,\"accumulatedThirdCnt\":54438,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":3691,\"id\":4475,\"secondCnt\":6490,\"sido\":\"인천광역시\",\"thirdCnt\":3386,\"totalFirstCnt\":2388922,\"totalSecondCnt\":2280575,\"totalThirdCnt\":57824},{\"accumulatedFirstCnt\":1166269,\"accumulatedSecondCnt\":1105684,\"accumulatedThirdCnt\":27280,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1788,\"id\":4476,\"secondCnt\":3146,\"sido\":\"광주광역시\",\"thirdCnt\":1375,\"totalFirstCnt\":1168057,\"totalSecondCnt\":1108830,\"totalThirdCnt\":28655},{\"accumulatedFirstCnt\":1153201,\"accumulatedSecondCnt\":1099420,\"accumulatedThirdCnt\":24780,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1174,\"id\":4477,\"secondCnt\":2472,\"sido\":\"대전광역시\",\"thirdCnt\":1097,\"totalFirstCnt\":1154375,\"totalSecondCnt\":1101892,\"totalThirdCnt\":25877},{\"accumulatedFirstCnt\":266819,\"accumulatedSecondCnt\":253738,\"accumulatedThirdCnt\":8333,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":297,\"id\":4478,\"secondCnt\":524,\"sido\":\"세종특별자치시\",\"thirdCnt\":338,\"totalFirstCnt\":267116,\"totalSecondCnt\":254262,\"totalThirdCnt\":8671},{\"accumulatedFirstCnt\":895005,\"accumulatedSecondCnt\":854482,\"accumulatedThirdCnt\":19511,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":778,\"id\":4479,\"secondCnt\":1711,\"sido\":\"울산광역시\",\"thirdCnt\":728,\"totalFirstCnt\":895783,\"totalSecondCnt\":856193,\"totalThirdCnt\":20239},{\"accumulatedFirstCnt\":10961054,\"accumulatedSecondCnt\":10448143,\"accumulatedThirdCnt\":249519,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":18808,\"id\":4480,\"secondCnt\":31209,\"sido\":\"경기도\",\"thirdCnt\":15382,\"totalFirstCnt\":10979862,\"totalSecondCnt\":10479352,\"totalThirdCnt\":264901},{\"accumulatedFirstCnt\":1330073,\"accumulatedSecondCnt\":1266812,\"accumulatedThirdCnt\":34126,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1467,\"id\":4481,\"secondCnt\":3147,\"sido\":\"충청북도\",\"thirdCnt\":1547,\"totalFirstCnt\":1331540,\"totalSecondCnt\":1269959,\"totalThirdCnt\":35673},{\"accumulatedFirstCnt\":1755430,\"accumulatedSecondCnt\":1674633,\"accumulatedThirdCnt\":50645,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":2025,\"id\":4482,\"secondCnt\":3973,\"sido\":\"충청남도\",\"thirdCnt\":1676,\"totalFirstCnt\":1757455,\"totalSecondCnt\":1678606,\"totalThirdCnt\":52321},{\"accumulatedFirstCnt\":1257664,\"accumulatedSecondCnt\":1203983,\"accumulatedThirdCnt\":31538,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1118,\"id\":4483,\"secondCnt\":2409,\"sido\":\"강원도\",\"thirdCnt\":891,\"totalFirstCnt\":1258782,\"totalSecondCnt\":1206392,\"totalThirdCnt\":32429},{\"accumulatedFirstCnt\":1487810,\"accumulatedSecondCnt\":1417780,\"accumulatedThirdCnt\":38324,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":2021,\"id\":4484,\"secondCnt\":3098,\"sido\":\"전라북도\",\"thirdCnt\":1908,\"totalFirstCnt\":1489831,\"totalSecondCnt\":1420878,\"totalThirdCnt\":40232},{\"accumulatedFirstCnt\":1549298,\"accumulatedSecondCnt\":1478710,\"accumulatedThirdCnt\":48584,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1642,\"id\":4485,\"secondCnt\":2602,\"sido\":\"전라남도\",\"thirdCnt\":1918,\"totalFirstCnt\":1550940,\"totalSecondCnt\":1481312,\"totalThirdCnt\":50502},{\"accumulatedFirstCnt\":2125744,\"accumulatedSecondCnt\":2027584,\"accumulatedThirdCnt\":45116,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":1985,\"id\":4486,\"secondCnt\":4661,\"sido\":\"경상북도\",\"thirdCnt\":1800,\"totalFirstCnt\":2127729,\"totalSecondCnt\":2032245,\"totalThirdCnt\":46916},{\"accumulatedFirstCnt\":2666647,\"accumulatedSecondCnt\":2543463,\"accumulatedThirdCnt\":64561,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":3144,\"id\":4487,\"secondCnt\":6213,\"sido\":\"경상남도\",\"thirdCnt\":2934,\"totalFirstCnt\":2669791,\"totalSecondCnt\":2549676,\"totalThirdCnt\":67495},{\"accumulatedFirstCnt\":538762,\"accumulatedSecondCnt\":511227,\"accumulatedThirdCnt\":13263,\"baseDate\":\"2021-11-14 00:00:00\",\"firstCnt\":718,\"id\":4488,\"secondCnt\":1157,\"sido\":\"제주특별자치도\",\"thirdCnt\":578,\"totalFirstCnt\":539480,\"totalSecondCnt\":512384,\"totalThirdCnt\":13841}],\"matchCount\":18,\"page\":1,\"perPage\":18,\"totalCount\":4506}\n"
        val vaccinated = JSONObject(jsonstring)
        val jArray = vaccinated.getJSONArray("data")
        var resultList = emptyList<vaccinatedData>()
        var resultStr: String = ""
        val tv: TextView = findViewById(R.id.textView2)

        for (i in 0 until jArray.length()) {
            val obj = jArray.getJSONObject(i)
            val loc = obj.getString("sido")
            val first = obj.getString("firstCnt")
            val sec = obj.getString("secondCnt")
            val third = obj.getString("thirdCnt")
            resultList += vaccinatedData(loc, first, sec, third)
            resultStr += resultList.get(i).printData()
        }
        tv.append(resultStr)
    }

    private fun get(url: String): String {
        val tv: TextView = findViewById(R.id.textView2)
        var xml = ""
        try {
            val URLXML = URL(url);
            val http : HttpsURLConnection = URLXML.openConnection() as HttpsURLConnection;
            http.setConnectTimeout(5000);
            val sb : StringBuilder = StringBuilder()
            val istream : InputStreamReader = InputStreamReader(http.inputStream)
            val br = BufferedReader(istream)
            while(true)
            {
                val line = br.readLine() ?: break
                sb.append(line);
            }
            xml = sb.toString()
            br.close()
            http.disconnect()
        } catch (e: Exception) {
            tv.text = e.toString()
        }
        return xml
    }

    private fun parsingData(element : Element?) {
        val tv: TextView = findViewById(R.id.textView2)
        var str : String = ""
        val list = element?.getElementsByTagName("item")
        if (list != null) {
            for (i in 0..list.length - 1) {
                var n: Node = list.item(i)
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    str += "============${elem.getElementsByTagName("gubun").item(0).textContent}============\n"
                    str += "확진자 수 : ${elem.getElementsByTagName("defCnt").item(0).textContent}\n"
                    str += "전일 대비 증감 : ${elem.getElementsByTagName("incDec").item(0).textContent}\n"
                    str += "사망자 수 : ${elem.getElementsByTagName("deathCnt").item(0).textContent}\n"
                }
            }
        }
        tv.append(str)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private suspend fun getInfected(API: StatisticAPI, pageNo : String, numOfRows : String, startCreateDt: String, endCreateDt: String): Element? {
        var xml : String = ""
        var element : Element? = null
        CoroutineScope(Dispatchers.IO).async {
            val serviceKey = "bKFxm+Xr14ZcYYufhT8uL7d3uZt47+EsPB/QXMPBp1dOtzu1pDR4f9fhsG2jdKdmglXIavaB6gqvEOiDejKu0A=="
            val url: String = "https://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson?serviceKey=" +
                    "$serviceKey&pageNo=$pageNo&numOfRows=$numOfRows&startCreateDt=$startCreateDt&endCreateDt=$endCreateDt"
            xml = "<response>\n" +
                    "<header>\n" +
                    "<resultCode>00</resultCode>\n" +
                    "<resultMsg>NORMAL SERVICE.</resultMsg>\n" +
                    "</header>\n" +
                    "<body>\n" +
                    "<items>\n" +
                    "<item>\n" +
                    "<createDt>2021-11-14 09:40:13.21</createDt>\n" +
                    "<deathCnt>16</deathCnt>\n" +
                    "<defCnt>6358</defCnt>\n" +
                    "<gubun>검역</gubun>\n" +
                    "<gubunCn>隔離區</gubunCn>\n" +
                    "<gubunEn>Lazaretto</gubunEn>\n" +
                    "<incDec>3</incDec>\n" +
                    "<isolClearCnt>6219</isolClearCnt>\n" +
                    "<isolIngCnt>123</isolIngCnt>\n" +
                    "<localOccCnt>0</localOccCnt>\n" +
                    "<overFlowCnt>3</overFlowCnt>\n" +
                    "<qurRate>-</qurRate>\n" +
                    "<seq>13290</seq>\n" +
                    "<stdDay>2021년 11월 14일 00시</stdDay>\n" +
                    "<updateDt>null</updateDt>\n" +
                    "</item>\n" +
                    "<item>\n" +
                    "<createDt>2021-11-14 09:40:13.21</createDt>\n" +
                    "<deathCnt>7</deathCnt>\n" +
                    "<defCnt>6358</defCnt>\n" +
                    "<gubun>합계</gubun>\n" +
                    "<gubunCn>合计</gubunCn>\n" +
                    "<gubunEn>Total</gubunEn>\n" +
                    "<incDec>2419</incDec>\n" +
                    "<isolClearCnt>360891</isolClearCnt>\n" +
                    "<isolIngCnt>31466</isolIngCnt>\n" +
                    "<localOccCnt>2401</localOccCnt>\n" +
                    "<overFlowCnt>18</overFlowCnt>\n" +
                    "<qurRate>763</qurRate>\n" +
                    "<seq>13272</seq>\n" +
                    "<stdDay>2021년 11월 14일 00시</stdDay>\n" +
                    "<updateDt>null</updateDt>\n" +
                    "</item>\n" +
                    "</items>\n" +
                    "<numOfRows>10</numOfRows>\n" +
                    "<pageNo>1</pageNo>\n" +
                    "<totalCount>19</totalCount>\n" +
                    "</body>\n" +
                    "</response>" //get(url)
            val factory = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.byteInputStream())
            element = factory.documentElement
        }.join()
        return element
    }

}