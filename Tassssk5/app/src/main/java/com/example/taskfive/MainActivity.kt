package com.example.taskfive

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.taskfive.ui.theme.MyFiveAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            MyFiveAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
            MyFiveAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Green//MaterialTheme.colorScheme.background
                ) {
                    Box{
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, MainActivity2::class.java)
//                                intent.putExtra("extra_data", "Some data here")
                                startActivity(intent)
                                finish()
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            },
                            modifier = Modifier.align(Alignment.BottomCenter) // Position the button in the center
                        ) {
                            Text(text = "Go to Second Activity")
                        }
                    }

                    Box {
                        AnimatedPreloader(modifier = Modifier.size(200.dp).align(Alignment.Center))




                    }

//                    val buttonSwitchActivity: Button = findViewById(R.id.button_switch_activity)
//                    buttonSwitchActivity.setOnClickListener {
//                        // Create an Intent to start the second activity
//                        val intent = Intent(this, MainActivity2::class.java)
//
//                        // You can pass data using the Intent
//                        intent.putExtra("extra_data", "Some data here")
//
//                        // Start the second activity
//                        startActivity(intent)
//
//                        // Optionally finish the first activity to prevent going back to it
//                        finish()
//                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedPreloader(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.animation_preloader
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
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
    MyFiveAppTheme {
        Greeting("Android")
    }
}