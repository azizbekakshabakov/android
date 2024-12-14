package com.example.someproject.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
//import androidx.compose.foundation.text.Text
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.someproject.model.BalanceResponse
import com.example.someproject.model.Car
import com.example.someproject.model.Rent
import com.example.someproject.model.RentGetResponse
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentsActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var token: String
    private lateinit var sharedPreferences: SharedPreferences

    private var rents: List<Rent> = emptyList()
    private var cars: List<Car> = emptyList()
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentsScreen()
        }

        // Retrieve the JWT token from shared preferences
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("jwt_token", null) ?: ""

        // Initialize API service
        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)

        // Fetch rents data
        fetchRents()
    }

    private fun fetchRents() {
        apiService.getRents().enqueue(object : Callback<RentGetResponse> {
            override fun onResponse(call: Call<RentGetResponse>, response: Response<RentGetResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { rentGetResponse ->
                        rents = rentGetResponse.rents
                        cars = rentGetResponse.cars
                        setContent {
                            RentsScreen(rents, cars, balance)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RentGetResponse>, t: Throwable) {
                Toast.makeText(this@RentsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        apiService.getBalance()
            .enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                    if (response.isSuccessful) {
                        balance = response.body()?.balance ?: 0.0
                        setContent {
                            RentsScreen(rents, cars, balance)
                        }
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    Log.e("CarsActivity", "Error fetching balance: ${t.message}")
                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun RentsScreen(rents: List<Rent> = emptyList(), cars: List<Car> = emptyList(), balance: Double = 0.0) {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Cars Activity - Balance: $$balance")
                    }
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 65.dp, bottom = 56.dp)
                ) {
                    items(rents) { rent ->
                        val car = cars.find { it._id == rent.carId } // Match car to rent

                        // Display car name and rent information
                        RentItem(rent = rent, car = car)
                    }
                }
            },
            bottomBar = {
                NavigationBar (
                    modifier = Modifier.height(56.dp)////////////////////////////////////////////////
                ) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Cars") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RentsActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RentsActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Баланс") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RentsActivity, BalanceActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Logout") },
                            selected = false,
                            onClick = {
                                val editor = sharedPreferences.edit()
                                editor.remove("jwt_token")
                                editor.apply()
                                jwtTokenExists.value = false

                                startActivity(Intent(this@RentsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RentsActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RentsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun RentItem(rent: Rent, car: Car?) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Car name
            Text(
                text = "Car: ${car?.name ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Rent deletion button
            Button(
                onClick = { deleteRent(rent.carId) },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(text = "Delete Rent")
            }

            // Separator
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.LightGray)
            )
        }
    }

    private fun deleteRent(carId: String) {
        apiService.removeRent(carId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RentsActivity, "Rent deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchRents() // Refresh the list of rents
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Failed to delete rent"
                    Toast.makeText(this@RentsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
