package com.example.tagenmieuxcompose

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest

class HTTPReq {
    companion object {
        fun getRequest(url: String, callback: VolleyCallback): JsonArrayRequest {
            val stringRequest = object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    callback.onSuccess(response)
                },
                Response.ErrorListener { error ->
                    Log.e("ERROR", error.toString())
                }
            ) {
                override fun getHeaders(): Map<String, String> {

                    val headers = HashMap<String, String>()
                    headers["origin"] = "eloichr.xyz"

                    return headers
                }
            }
            return stringRequest
        }
    }
}