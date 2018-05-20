package com.haseebshah.root.pushlogger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    lateinit var refreshButton: Button
    lateinit var resultArea: TextView
    lateinit var requestQueue : RequestQueue
    lateinit var listView: ListView
    var channelList: ArrayList<String> = ArrayList()
    val baseURL: String = "http://puushlogger.herokuapp.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.lv)
        refreshButton = findViewById(R.id.refresh_btn)
        requestQueue = Volley.newRequestQueue(this)
    }

    private fun getChannelsList() {
        val url = baseURL + "channels"
        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                "",
                Response.Listener { response: JSONArray ->
                    updateListView(response)
                    //resultArea.text = response.toString()
                    //resultArea.text = response.getJSONObject(1).getString("Channel Name")
                },
                Response.ErrorListener { error ->
                    resultArea.text = error.message
                }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun refreshBtnHandler(v: View) {
        getChannelsList()
    }

    private fun updateListView(channelJSONArray: JSONArray) {
        channelList.clear()
        for (i in 0 until channelJSONArray.length()){
            channelList.add(channelJSONArray.getJSONObject(i).getString("Channel Name"))
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, channelList)
        listView.adapter = adapter
    }
}
