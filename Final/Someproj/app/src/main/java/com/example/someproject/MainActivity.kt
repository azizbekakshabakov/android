package com.example.someproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.someproject.activity.CarsActivity
import com.example.someproject.activity.LoginActivity
import com.example.someproject.activity.RegisterActivity
import com.example.someproject.activity.RentsActivity
import com.example.someproject.ui.theme.SomeProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Main Activity!")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }) {
                Text("Go to Register Activity")
            }

            Button(onClick = {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }) {
                Text("Go to Login Activity")
            }

            Button(onClick = {
                val intent = Intent(this@MainActivity, CarsActivity::class.java)
                startActivity(intent)
            }) {
                Text("Go to Cars Activity")
            }

            Button(onClick = {
                val intent = Intent(this@MainActivity, RentsActivity::class.java)
                startActivity(intent)
            }) {
                Text("Go to Rents Activity")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SomeProjectTheme {
        Greeting("Android")
    }
}