package com.haseebshah.root.pushlogger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class LogActivity : AppCompatActivity() {

    lateinit var url: String
    lateinit var tv: TextView
    private lateinit var requestQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        url = intent.extras.getString(CHANNEL_URL)
        title = intent.extras.getString(CHANNEL_NAME)

        tv = findViewById(R.id.tv)
        tv.movementMethod = ScrollingMovementMethod()
        requestQueue = Volley.newRequestQueue(this)
        getLogs()
    }

    companion object {
        const val CHANNEL_NAME = "title"
        const val CHANNEL_URL = "url"

        fun newChannelIntent(context: Context, channel_name: String): Intent {
            val channelIntent = Intent(context, LogActivity::class.java)
            channelIntent.putExtra(CHANNEL_NAME, channel_name)
            channelIntent.putExtra(CHANNEL_URL, "http://puushlogger.herokuapp.com/logs/$channel_name")
            return channelIntent
        }
    }

    private fun updateTextView(logJSONArray: JSONArray) {
        var logs = ""
        for (i in 0 until logJSONArray.length()){
            logs += logJSONArray.getJSONObject(i).getString("Timestamp") + ":  "
            logs += logJSONArray.getJSONObject(i).getString("Message") + "\n"
        }
        tv.text = logs
    }

    private fun getLogs() {
        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                "",
                Response.Listener { response: JSONArray ->
                    updateTextView(response)
                },
                Response.ErrorListener { error ->
                    tv.text = error.message
                }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun refreshBtnHandler(v: View){
        getLogs()
    }
}
