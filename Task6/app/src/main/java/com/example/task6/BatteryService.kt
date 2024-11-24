package com.example.task6

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BatteryService : Service() {

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Получаем уровень заряда батареи
            val level = intent.getIntExtra("level", -1)
            val scale = intent.getIntExtra("scale", -1)

            val batteryPercentage = (level / scale.toFloat()) * 100


            Log.d("BatteryService", "Battery level: $batteryPercentage%")
            val broadcastIntent = Intent("com.example.task6.BATTERY_STATUS")
            broadcastIntent.putExtra("batteryPercentage", batteryPercentage)

            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)

        }
    }

    override fun onCreate() {
        super.onCreate()
        // BroadcastReceiver для просмотра изменений уровня батареи
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // удолить
        unregisterReceiver(batteryReceiver)
    }
}
