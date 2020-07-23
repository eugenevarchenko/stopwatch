package com.example.ultimacalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private var handler: Handler? = null

    private var milliseconds: Int = 0
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var hours: Int = 0

    private var startTime: Long = 0L
    private var timeBuff: Long = 0L
    private var updateTime: Long = 0L
    private var millisecondsTime: Long = 0L

    private var isRunning: Boolean = false

    private var mainTimeTextView: TextView? = null
    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var resetButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainTimeTextView = findViewById(R.id.mainTimeTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        startTime = SystemClock.uptimeMillis()

        startButton?.setOnClickListener {
//            if (isRunning) {
//                handler?.removeCallbacks(runnable)
//                isRunning = false
//            } else {
                handler?.postDelayed(runnable, 0)
                isRunning = true
        }

        stopButton?.setOnClickListener {
            handler?.removeCallbacks(runnable)
            isRunning = false
        }

        resetButton?.setOnClickListener {
            handler?.removeCallbacks(runnable)
            isRunning = false

            milliseconds = 0
            seconds = 0
            minutes = 0
            hours = 0
            startTime = 0L
            timeBuff = 0L
            updateTime = 0L
            millisecondsTime = 0L
        }

        handler = Handler()
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            millisecondsTime = SystemClock.uptimeMillis() - startTime
            updateTime = timeBuff + millisecondsTime
            seconds = (updateTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            milliseconds = (updateTime % 1000).toInt()

            var secondsString: String = seconds.toString()
            var millisecondsString: String = milliseconds.toString()

//            mainTimeTextView?.text = "$minutes:${seconds-1}.$milliseconds"

            if (millisecondsString.length < 3) {
                millisecondsString = "0$milliseconds"
            } else if (millisecondsString.length < 2) {
                millisecondsString = "00$milliseconds"
            } else {
                millisecondsString = "$milliseconds"
            }

            if (secondsString.length < 2) {
                secondsString = "0$seconds"
            } else {
                secondsString = "$seconds"
            }

            mainTimeTextView?.text = "${minutes.toString()}:$secondsString.$millisecondsString"

//            when {
//                milliseconds.toString().length < 3 ->
//                milliseconds.toString().length < 2 ->
//
//            }

            handler?.postDelayed(this, 0)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}