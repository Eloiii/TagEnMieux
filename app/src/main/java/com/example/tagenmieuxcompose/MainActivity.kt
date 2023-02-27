@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tagenmieuxcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.tagenmieuxcompose.ui.theme.TagEnMieuxComposeTheme
import org.json.JSONObject
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private lateinit var queue: RequestQueue
    private lateinit var stopData: StopData

    override fun onCreate(savedInstanceState: Bundle?) {
        queue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        getDataFromApi("SEM:GENMAISONCO", "SEM:14:0:14_R_36")
        setContent {
            TagEnMieuxComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { /*TODO*/ },
                                containerColor = MaterialTheme.colorScheme.secondary
                            ) {
                                Icon(Icons.Filled.Add, "desc", tint = MaterialTheme.colorScheme.onSecondary)
                            }
                        },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = "Tag en mieux",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis

                                    )
                                },
                            )
                        }
                    ) { values ->
                        LazyColumn(contentPadding = values, modifier = Modifier) {
                            items(10) {
                                StopCard(title = "un super long ", description = "test", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }


    private fun getDataFromApi(stopName: String, routeName: String) {
        queue.add(HTTPReq.getRequest("https://data.mobilites-m.fr/api/routers/default/index/clusters/${stopName}/stoptimes", VolleyCallback {
            data ->
            var route: JSONObject? = null
            if (data != null) {
                (0 until data.length()).forEach {
                    val stop = data.getJSONObject(it)
                    val pattern = stop.getJSONObject("pattern")
                    val id = pattern.getString("id")
                    if(id == routeName)
                        route = stop
                }
            }

            val nextArrival = route?.getJSONArray("times")?.get(0) as JSONObject
            val name = route?.getJSONObject("pattern")?.getString("id")?.split(":")?.get(1)?.trim() + " // " + nextArrival.getString("stopName").split(",")[1].trim() + " direction " + route?.getJSONObject("pattern")?.getString("shortDesc")
            val realTime = nextArrival.getBoolean("realtime")

            val realTimeArrivalSeconds = nextArrival.getInt("realtimeArrival")
            val h_realtime: Int = realTimeArrivalSeconds / 3600
            val m_realtime: Int = realTimeArrivalSeconds % 3600 / 60
            val s_realtime: Int = realTimeArrivalSeconds % 60

            val scheduledArrivalSeconds = nextArrival.getInt("scheduledArrival")
            val h_scheduled: Int = scheduledArrivalSeconds / 3600
            val m_scheduled: Int = scheduledArrivalSeconds % 3600 / 60
            val s_scheduled: Int = scheduledArrivalSeconds % 60




            val arrivalTime = if (realTime) LocalDate.now().atTime(h_realtime, m_realtime,s_realtime) else LocalDate.now().atTime(h_scheduled, m_scheduled, s_scheduled)
            val scheduledArrival = LocalDate.now().atTime(h_scheduled, m_scheduled, s_scheduled)
            stopData = StopData(name, realTime, arrivalTime, scheduledArrival)
            Log.d("DEBUG", stopData.name)
        }))
    }

}

//[
//{
//    "pattern": {
//    "id": "SEM:D:0:D_R_1",
//    "desc": "Les Taillees",
//    "dir": 1,
//    "shortDesc": "TAILLEES-UNIVER",
//    "lastStop": "SEM:3100",
//    "lastStopName": "Saint-Martin-d'Hères, Les Taillées - Universités"
//},
//    "times": [
//    {
//        "stopId": "SEM:3092",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 42060,
//        "scheduledDeparture": 42060,
//        "realtimeArrival": 42060,
//        "realtimeDeparture": 42060,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27043825",
//        "headsign": "Saint-Martin-d'Hères, Les Taillées - Universités",
//        "occupancy": "Faible",
//        "occupancyId": 1
//    },
//    {
//        "stopId": "SEM:3092",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 42960,
//        "scheduledDeparture": 42960,
//        "realtimeArrival": 42960,
//        "realtimeDeparture": 42960,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27043826",
//        "headsign": "Saint-Martin-d'Hères, Les Taillées - Universités",
//        "occupancy": "Faible",
//        "occupancyId": 1
//    },
//    {
//        "stopId": "SEM:3092",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 43800,
//        "scheduledDeparture": 43800,
//        "realtimeArrival": 43818,
//        "realtimeDeparture": 43818,
//        "arrivalDelay": 0,
//        "departureDelay": 18,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27043827",
//        "headsign": "Saint-Martin-d'Hères, Les Taillées - Universités",
//        "occupancy": "Faible",
//        "occupancyId": 1
//    }
//    ]
//},
//{
//    "pattern": {
//    "id": "SEM:14:1:14_A_37",
//    "desc": "Gieres Gare U.",
//    "dir": 2,
//    "shortDesc": "GIERES GARE",
//    "lastStop": "SEM:3297",
//    "lastStopName": "Gières, Gières Gare - Universités"
//},
//    "times": [
//    {
//        "stopId": "SEM:0780",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 42245,
//        "scheduledDeparture": 42245,
//        "realtimeArrival": 42264,
//        "realtimeDeparture": 42264,
//        "arrivalDelay": 0,
//        "departureDelay": 19,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482741",
//        "headsign": "Gières, Gières Gare - Universités"
//    },
//    {
//        "stopId": "SEM:0780",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 43145,
//        "scheduledDeparture": 43145,
//        "realtimeArrival": 43145,
//        "realtimeDeparture": 43145,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": false,
//        "realtimeState": "SCHEDULED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482747",
//        "headsign": "Gières, Gières Gare - Universités"
//    },
//    {
//        "stopId": "SEM:0780",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 44045,
//        "scheduledDeparture": 44045,
//        "realtimeArrival": 44046,
//        "realtimeDeparture": 44046,
//        "arrivalDelay": 0,
//        "departureDelay": 1,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482740",
//        "headsign": "Gières, Gières Gare - Universités"
//    }
//    ]
//},
//{
//    "pattern": {
//    "id": "SEM:14:0:14_R_36",
//    "desc": "Verdun Prefect.",
//    "dir": 1,
//    "shortDesc": "VERDUN PREFECTU",
//    "lastStop": "SEM:4027",
//    "lastStopName": "Grenoble, Verdun - Préfecture"
//},
//    "times": [
//    {
//        "stopId": "SEM:0781",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 42270,
//        "scheduledDeparture": 42270,
//        "realtimeArrival": 42270,
//        "realtimeDeparture": 42270,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": false,
//        "realtimeState": "SCHEDULED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482714",
//        "headsign": "Grenoble, Verdun - Préfecture"
//    },
//    {
//        "stopId": "SEM:0781",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 43170,
//        "scheduledDeparture": 43170,
//        "realtimeArrival": 43170,
//        "realtimeDeparture": 43170,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482715",
//        "headsign": "Grenoble, Verdun - Préfecture"
//    },
//    {
//        "stopId": "SEM:0781",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 44250,
//        "scheduledDeparture": 44250,
//        "realtimeArrival": 44262,
//        "realtimeDeparture": 44262,
//        "arrivalDelay": 0,
//        "departureDelay": 12,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27482716",
//        "headsign": "Grenoble, Verdun - Préfecture"
//    }
//    ]
//},
//{
//    "pattern": {
//    "id": "SEM:D:1:D_A_4",
//    "desc": "Etienne Grappe",
//    "dir": 2,
//    "shortDesc": "ETIENNE GRAPPE",
//    "lastStop": "SEM:3107",
//    "lastStopName": "Saint-Martin-d'Hères, Etienne Grappe"
//},
//    "times": [
//    {
//        "stopId": "SEM:3093",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 42780,
//        "scheduledDeparture": 42780,
//        "realtimeArrival": 42772,
//        "realtimeDeparture": 42772,
//        "arrivalDelay": 0,
//        "departureDelay": -8,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27043873",
//        "headsign": "Saint-Martin-d'Hères, Etienne Grappe",
//        "occupancy": "Faible",
//        "occupancyId": 1
//    },
//    {
//        "stopId": "SEM:3093",
//        "stopName": "Saint-Martin-d'Hères, Maison Communale",
//        "scheduledArrival": 43680,
//        "scheduledDeparture": 43680,
//        "realtimeArrival": 43680,
//        "realtimeDeparture": 43680,
//        "arrivalDelay": 0,
//        "departureDelay": 0,
//        "timepoint": true,
//        "realtime": true,
//        "realtimeState": "UPDATED",
//        "serviceDay": 1677452400,
//        "tripId": "SEM:27043874",
//        "headsign": "Saint-Martin-d'Hères, Etienne Grappe",
//        "occupancy": "Faible",
//        "occupancyId": 1
//    }
//    ]
//}
//]