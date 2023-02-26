@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tagenmieuxcompose

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tagenmieuxcompose.ui.theme.TagEnMieuxComposeTheme
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
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

    private fun getData() {
        val queue = Volley.newRequestQueue(this)
        var res: String? = null
        val mStringRequest = StringRequest(Request.Method.GET, "https://data.mobilites-m.fr/api/routers/default/index/clusters/SEM:GENMAISONCO/stoptimes",
            { response -> Log.d("DEBUG", response) }
        ) { error -> Log.i(ContentValues.TAG, "Error :$error") }

        queue.add(stringRequest)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TagEnMieuxComposeTheme {
        StopCard(title = "bonjour", description = "test")
    }
}