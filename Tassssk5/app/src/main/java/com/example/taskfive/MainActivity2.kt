package com.example.taskfive

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskfive.ui.theme.MyFiveAppTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFiveAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Blue
                ) {
                    Box {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity2, MainActivity::class.java)
//                                intent.putExtra("extra_data", "Some data here")
                                startActivity(intent)
                                finish()
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            },
                            modifier = Modifier
                                .align(Alignment.Center) // Position in the center of the screen
                        ) {
                            Text(text = "Back to Main Activity")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MyFiveAppTheme {
        Greeting2("Android")
    }
}