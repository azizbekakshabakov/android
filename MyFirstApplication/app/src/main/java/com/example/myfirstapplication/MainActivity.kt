package com.example.myfirstapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstapplication.data.UserData
import com.example.myfirstapplication.ui.theme.MyFirstApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstApplicationTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFA07A)) // Background color for the Box
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.statusBarsPadding().padding(all = 8.dp)
                    ) {
                        items(UserData.userData()) { row ->
                            TextName(name = row["name"].toString())
                            TextDescription(name = row["description"].toString())
                            TextDate(name = row["date"].toString())

                            Row(
                                modifier = Modifier.fillMaxWidth(), // Fill the full width
                                horizontalArrangement = Arrangement.End // Aligns children to the end (right)
                            ) {
                                GreetingImage(row["image"] as Int)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingImage(imageName: Int, modifier: Modifier = Modifier) {
//        val imageId = resources.getIdentifier(imageName, "drawable", packageName)
    val painter = painterResource(id = imageName)
    Image(painter = painter, contentDescription = null)
}

@Composable
fun TextName(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFDC143C)
        ),
        modifier = modifier
    )
}

@Composable
fun TextDescription(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        style = TextStyle(
            fontStyle = FontStyle.Italic,
            fontSize = 26.sp,
            fontFamily = FontFamily.Cursive,
            color = Color(0xFF9932CC)
        ),
        modifier = modifier
    )
}

@Composable
fun TextDate(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        style = TextStyle(
            fontStyle = FontStyle.Normal,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF6A5ACD)
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyFirstApplicationTheme {
//        Greeting("Android")
    }
}