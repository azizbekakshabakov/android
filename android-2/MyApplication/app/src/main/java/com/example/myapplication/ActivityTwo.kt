package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_two)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentThree = FragmentThree()
        val fragmentFour = FragmentFour()///////////////////////

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.theFragment, fragmentThree)
            commit()
        }

        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.theFragment, fragmentThree)
                addToBackStack(null)
                commit()
            }
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.theFragment, fragmentFour)
                addToBackStack(null)
                commit()
            }
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
//            val resultIntent = Intent()
//            resultIntent.putExtra("result_key", "Your result string here")
//            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}