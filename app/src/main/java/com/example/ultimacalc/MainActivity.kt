package com.example.ultimacalc

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val handler: Handler? = Handler()

    private var milliseconds: Int = 0
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var hours: Int = 0

    private var startTime: Long = 0
    private var timeBuff: Long = 0
    private var updatedTime: Long = 0
    private var millisecondsTime: Long = 0

    private var isRunning: Boolean = false
    private var wasRunningBeforeStop: Boolean = false

    private var rootLayout: ConstraintLayout? = null
    private var mainTimeTextView: TextView? = null
    private var startButton: Button? = null
    private var stopButton: Button? = null
    private var resetButton: Button? = null
    private var darkModeSwitch: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.rootLayout)
        mainTimeTextView = findViewById(R.id.mainTimeTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)

        mainTimeTextView?.text = "0:00.000"

        startButton?.setOnClickListener {
//            if (isRunning) {
//                handler?.removeCallbacks(runnable)
//                isRunning = false
//            } else {

            startTime = SystemClock.uptimeMillis()

            if (wasRunningBeforeStop) {
                handler?.postDelayed(runnable, 0)
                isRunning = true
                wasRunningBeforeStop = false
            } else {
                handler?.removeCallbacks(runnable)
                handler?.postDelayed(runnable, 0)
                isRunning = true
                wasRunningBeforeStop = false
            }

            startButton!!.isEnabled = false
            stopButton!!.isEnabled = true
        }

        stopButton?.setOnClickListener {
            handler?.removeCallbacks(runnable)
            isRunning = false
            wasRunningBeforeStop = true

            startButton!!.isEnabled = true
            stopButton!!.isEnabled = false
        }

        resetButton?.setOnClickListener {
            milliseconds = 0
            seconds = 0
            minutes = 0
            hours = 0
            startTime = 0
            timeBuff = 0
            updatedTime = 0
            millisecondsTime = 0

            mainTimeTextView?.text = "0:00.000"

            handler?.removeCallbacks(runnable)
            isRunning = false
            wasRunningBeforeStop = false

            startButton!!.isEnabled = true
            stopButton!!.isEnabled = true
        }

        darkModeSwitch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rootLayout!!.setBackgroundColor(Color.BLACK)
                darkModeSwitch!!.setTextColor(Color.WHITE)
                mainTimeTextView!!.setTextColor(Color.WHITE)
                startButton!!.setBackgroundColor(Color.CYAN)
                stopButton!!.setBackgroundColor(Color.CYAN)
                resetButton!!.setBackgroundColor(Color.CYAN)
            } else {
                rootLayout!!.setBackgroundResource(R.drawable.background)
                darkModeSwitch!!.setTextColor(Color.BLACK)
                mainTimeTextView!!.setTextColor(Color.BLACK)
                startButton!!.setBackgroundColor(Color.parseColor("#FF69D5"))
                stopButton!!.setBackgroundColor(Color.parseColor("#FF69D5"))
                resetButton!!.setBackgroundColor(Color.parseColor("#FF69D5"))
            }
        }
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            millisecondsTime = SystemClock.uptimeMillis() - startTime
            updatedTime = timeBuff + millisecondsTime
            seconds = (updatedTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            milliseconds = (updatedTime % 1000).toInt()

            var secondsString: String = seconds.toString()
            var millisecondsString: String = milliseconds.toString()

            when {
                millisecondsString.length < 3 -> millisecondsString = "0$milliseconds"
                millisecondsString.length < 2 -> millisecondsString = "00$milliseconds"
                else -> millisecondsString = "$milliseconds"
            }

            when {
                secondsString.length < 2 -> secondsString = "0$seconds"
                else -> secondsString = "$seconds"
            }

            mainTimeTextView?.text = "${minutes.toString()}:$secondsString.$millisecondsString"

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