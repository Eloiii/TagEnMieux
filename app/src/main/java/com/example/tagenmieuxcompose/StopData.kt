package com.example.tagenmieuxcompose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

class StopData(stopName: String, routeName: String, queue: RequestQueue) : ViewModel() {
    val name = mutableStateOf(String)
    val realTime = mutableStateOf(Boolean)
    val arrivalTime = mutableStateOf(LocalDateTime)
    val scheduledArrival = mutableStateOf(LocalDateTime)

    init {
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
            this.name.setValue(name)
            this.realTime.add(realTime)
            this.arrivalTime.add(arrivalTime)
            this.scheduledArrival.add(scheduledArrival)

        }))
    }


}