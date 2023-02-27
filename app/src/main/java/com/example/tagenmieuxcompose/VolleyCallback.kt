package com.example.tagenmieuxcompose

import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

fun interface VolleyCallback {
    fun onSuccess(result: JSONArray?)
}