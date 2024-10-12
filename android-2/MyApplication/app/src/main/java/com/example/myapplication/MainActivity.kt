package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultText1: TextView

    private var pauseCount = 0
    private lateinit var currentTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultText1 = findViewById(R.id.resultText1)
        currentTime = findViewById(R.id.resultText2)
//        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                val resultString = data?.getStringExtra("result_key")
//                resultText1.text = resultString
//            }
//        }

        val fragmentFirst = FragmentFirst()
        val fragmentTwo = FragmentTwo()

//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.theFragment, fragmentFirst)
//            commit()
//        }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            val intent = Intent(this, ActivityOne::class.java)
            startActivity(intent);
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
                val intent = Intent(this, ActivityTwo::class.java)
                startActivity(intent);
        }
    }

    override fun onStart() {
        super.onStart()

        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        currentTime.text = "Метод onStart():\n Время:${dateFormat.format(Date())}"
    }

    override fun onResume() {
        super.onResume()

        resultText1.text = "Метод onPause(), onResume():\n Количество раз возвращено из паузы: ${pauseCount}"
    }

    override fun onPause() {
        super.onPause()

        pauseCount += 1
    }
}