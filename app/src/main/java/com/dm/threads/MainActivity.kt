package com.dm.threads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener { startProcess() }

        stopBtn.setOnClickListener { stopProcess() }

    }


    private var going = false
    private fun startProcess() {
        going = true

        Thread {
            var i = 0
            while (going) {
                Thread.sleep(1000)
                mainHandler.post {
                    textView.text = i++.toString()
                    Log.i("UIthread", "call 1")
                }
                runOnUiThread {
                    textView.text = i++.toString()
                    Log.i("UIthread", "call 2")
                }
                textView.post {
                    textView.text = i++.toString()
                    Log.i("UIthread", "call 3")
                }
            }
        }.start()
    }

    private fun stopProcess() {
        going = false
    }
}