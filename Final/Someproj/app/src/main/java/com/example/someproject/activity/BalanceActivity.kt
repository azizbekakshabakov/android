package com.example.someproject.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.someproject.model.BalanceRequest
import com.example.someproject.model.BalanceResponse
import com.example.someproject.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BalanceScreen()
        }
    }

    @Composable
    fun BalanceScreen() {
        val balanceInput = remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add Balance")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = balanceInput.value,
                onValueChange = { balanceInput.value = it },
                label = { Text("Enter Amount") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val amount = balanceInput.value.toDoubleOrNull()
                if (amount != null) {
                    sendBalanceUpdate(amount)
                } else {
                    Toast.makeText(this@BalanceActivity, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Send")
            }
        }
    }

    private fun sendBalanceUpdate(amount: Double) {
        val apiService = ApiClient.getRetrofitInstance(this).create(com.example.someproject.service.ApiService::class.java)
        val request = BalanceRequest(amount)

        apiService.addBalance(request).enqueue(object : Callback<BalanceResponse> {
            override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@BalanceActivity, "Balance added successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BalanceActivity, "Failed to add balance", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                Toast.makeText(this@BalanceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
