package com.example.task6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.task6.ui.theme.Task6Theme

class MainActivity : ComponentActivity() {

    private lateinit var batteryReceiver: BatteryReceiver
    private var batteryPercentage by mutableStateOf(0f)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        batteryReceiver = BatteryReceiver { newBatteryPercentage ->
            batteryPercentage = newBatteryPercentage
        }

        // Регистрация BroadcastReceiver для уровня батареи
        val filter = IntentFilter("com.example.task6.BATTERY_STATUS")
        LocalBroadcastManager.getInstance(this).registerReceiver(batteryReceiver, filter)
        Log.d("MainActivity", "Ресивер создн")

        // Запуск сервеса для отслеживания батареи
        startService(Intent(this, BatteryService::class.java))

        setContent {
            Task6Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Уровень заряда: ${batteryPercentage.toInt()}%", modifier = Modifier.padding(10.dp), fontSize = 36.sp)
                    Button(onClick = {
                        Toast.makeText(this@MainActivity, "Заряд: $batteryPercentage%", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Кнопки батарея", fontSize = 36.sp)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Task6Theme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Уровень заряда: 80%")
        }
    }
}
