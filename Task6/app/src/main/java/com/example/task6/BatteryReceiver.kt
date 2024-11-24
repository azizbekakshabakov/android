package com.example.task6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BatteryReceiver(private val onBatteryStatusChanged: (Float) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.task6.BATTERY_STATUS") {
            val batteryPercentage = intent.getFloatExtra("batteryPercentage", -1f)
            Log.d("BatteryReceiver", "Received battery percentage: $batteryPercentage")
            onBatteryStatusChanged(batteryPercentage)
        }
    }
}