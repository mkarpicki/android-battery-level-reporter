package com.karpicki.batterylevelreporter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// https://stackoverflow.com/questions/6391902/how-do-i-start-my-app-on-startup

class MainActivity : AppCompatActivity() {

    private var isReceiverRegistered = false

    private fun start() {
        if (isReceiverRegistered) {
            return
        }

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_BATTERY_OKAY)

        this.registerReceiver(
            BatteryLevelReceiver(),
            filter
        );

        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }

//        this.registerReceiver(
//            MyWakefulReceiver(),
//            IntentFilter()
//        );

        isReceiverRegistered = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putBoolean(Constants.semaphoreName, isReceiverRegistered)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            savedInstanceState.getBoolean(Constants.semaphoreName)
                .also { isReceiverRegistered = it }
        }

        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        start()
    }

}