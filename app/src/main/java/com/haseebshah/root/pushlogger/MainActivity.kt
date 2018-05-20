package com.haseebshah.root.pushlogger

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var refreshSwipe: SwipeRefreshLayout
    lateinit var infoText: TextView
    lateinit var requestQueue : RequestQueue
    lateinit var listView: ListView
    var channelList: ArrayList<String> = ArrayList()
    val baseURL: String = "http://puushlogger.herokuapp.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.lv)
        infoText = findViewById(R.id.tv)
        refreshSwipe = findViewById(R.id.refresh_swipe)
        refreshSwipe.setOnRefreshListener(this)

        requestQueue = Volley.newRequestQueue(this)
    }

    override fun onRefresh() {
        getChannelsList()
    }

    private fun getChannelsList() {
        val url = baseURL + "channels"
        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                "",
                Response.Listener { response: JSONArray ->
                    updateListView(response)
                    refreshSwipe.isRefreshing = false
                    infoText.visibility = View.INVISIBLE
                },
                Response.ErrorListener { error ->
                    refreshSwipe.isRefreshing = false
                    infoText.text = error.message + "\n Swipe down to try again"
                    infoText.visibility = View.VISIBLE
                }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun refreshBtnHandler() {
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
