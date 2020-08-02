package com.example.ultimacalc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.TextView
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout

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

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var timeTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var darkModeSwitch: Switch

    private val channelId: String = "com.example.ultimacalc"
    private val notifyId: Int = 228

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private lateinit var notificationLayout: RemoteViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.rootLayout)
        timeTextView = findViewById(R.id.timeTextView)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)

        timeTextView.text = "0:00.000"

        startButton.setOnClickListener {
//            if (isRunning) {
//                handler?.removeCallbacks(runnable)      TODO FIX: kastyli pachini syka ebanaya
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
//                isRunning = true                       TODO FIX: kastyli pachini syka ebanaya
//                wasRunningBeforeStop = false
            }

            startButton.isEnabled = false
            stopButton.isEnabled = true
        }

        stopButton.setOnClickListener {
            handler?.removeCallbacks(runnable)
//            isRunning = false                      TODO FIX: kastyli pachini syka ebanaya
//            wasRunningBeforeStop = true

            startButton.isEnabled = true
            stopButton.isEnabled = false
        }

        resetButton.setOnClickListener {
            milliseconds = 0
            seconds = 0
            minutes = 0
            hours = 0
            startTime = 0
            timeBuff = 0
            updatedTime = 0
            millisecondsTime = 0

            timeTextView.text = "0:00.000"

            handler?.removeCallbacks(runnable)
//            isRunning = false
//            wasRunningBeforeStop = false          TODO FIX: kastyli pachini syka ebanaya

            startButton.isEnabled = true
            stopButton.isEnabled = true
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rootLayout.setBackgroundColor(Color.BLACK)
                darkModeSwitch.setTextColor(Color.WHITE)
                timeTextView.setTextColor(Color.WHITE)
                startButton.setBackgroundColor(Color.CYAN)
                stopButton.setBackgroundColor(Color.CYAN)
                resetButton.setBackgroundColor(Color.CYAN)
            } else {
                rootLayout.setBackgroundColor(Color.parseColor("#C0F1FF"))
                darkModeSwitch.setTextColor(Color.BLACK)
                timeTextView.setTextColor(Color.BLACK)
                startButton.setBackgroundColor(Color.parseColor("#FF1AB7FF"))
                stopButton.setBackgroundColor(Color.parseColor("#FF1AB7FF"))
                resetButton.setBackgroundColor(Color.parseColor("#FF1AB7FF"))
            }
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationLayout = RemoteViews(packageName, R.layout.notification)

        notificationLayout.setTextViewText(R.id.notification_time, timeTextView.text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, "хуйв очё", NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)

            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContent(notificationLayout)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))   // TODO FIX: kastyli pachini syka ebanaya
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                .setContent(notificationLayout)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))   // TODO FIX: kastyli pachini syka ebanaya
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(notifyId, builder.build())
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
                millisecondsString.length < 2 -> millisecondsString = "00$milliseconds"    // TODO FIX: tekst inogda ska4et kak ebanat
                else -> millisecondsString = "$milliseconds"
            }

            when {
                secondsString.length < 2 -> secondsString = "0$seconds"
                else -> secondsString = "$seconds"
            }

            timeTextView.text = "${minutes.toString()}:$secondsString.$millisecondsString"

            handler?.postDelayed(this, 0)

            notificationLayout.setTextViewText(R.id.notification_time, timeTextView.text)
            notificationManager.notify(notifyId, builder.build())            // TODO FIX: kastyli pachini syka ebanaya
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