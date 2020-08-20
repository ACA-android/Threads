package com.dm.threads

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_async.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AsyncActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async)


        startBtn.setOnClickListener {
            MyTask(textView, progressBar).execute(R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6)
        }
    }
}

class MyTask(val textView: TextView, val progressBar: ProgressBar)
    : AsyncTask<Int, Int, String>() {

    //UI thread
    override fun onPreExecute() {
        textView.text = "writing images"
    }

    // Worker thread
    override fun doInBackground(vararg params: Int?): String {
        params.forEach {
            try {
                val bitmap = BitmapFactory.decodeResource(textView.resources, it!!)
                val dir = File(textView.context.filesDir, "images")
                dir.mkdirs()
                val file = File(dir, "$it.jpg")

                val fOut = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                fOut.flush()
                fOut.close()
                bitmap.recycle()
            } catch (e: IOException) {
                return "failed"
            }
            publishProgress(100*(params.indexOf(it)+1)/params.size)
        }

        return "success"
    }

    // UI thread
    override fun onProgressUpdate(vararg values: Int?) {
        progressBar.progress = values[0]!!
        textView.text = "${values[0]!!}%"
        
    }

    // UI thread
    override fun onPostExecute(result: String?) {
        textView.text = result
    }
}