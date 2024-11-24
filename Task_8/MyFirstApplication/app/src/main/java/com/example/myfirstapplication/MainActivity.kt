package com.example.myfirstapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)

        setContent {
            var itemList by remember { mutableStateOf<List<Item>>(emptyList()) }
            var newItemName by remember { mutableStateOf(TextFieldValue("")) }
            var newItemDescription by remember { mutableStateOf(TextFieldValue("")) }

            // Загрузка списка из базы данных
            LaunchedEffect(Unit) {
                itemList = loadItemsFromDatabase()
            }

            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    // Поля для ввода нового элемента
                    BasicTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(8.dp)) {
                                if (newItemName.text.isEmpty()) Text("Enter item name")
                                innerTextField()
                            }
                        }
                    )
                    BasicTextField(
                        value = newItemDescription,
                        onValueChange = { newItemDescription = it },
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        decorationBox = { innerTextField ->
                            Box(Modifier.padding(8.dp)) {
                                if (newItemDescription.text.isEmpty()) Text("Enter description")
                                innerTextField()
                            }
                        }
                    )
                    // Кнопка добавления
                    Button(
                        onClick = {
                            if (newItemName.text.isNotEmpty() && newItemDescription.text.isNotEmpty()) {
                                addItemToDatabase(
                                    Item(
                                        name = newItemName.text,
                                        description = newItemDescription.text
                                    )
                                ) {
                                    itemList = it
                                }
                                newItemName = TextFieldValue("")
                                newItemDescription = TextFieldValue("")
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Add Item")
                    }

                    // Список элементов
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(itemList) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
                                    Text(text = "Description: ${item.description}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Загрузка всех элементов из базы данных
    private suspend fun loadItemsFromDatabase(): List<Item> {
        return withContext(Dispatchers.IO) {
            database.itemDao().getAllItems()
        }
    }

    // Добавление нового элемента в базу данных
    private fun addItemToDatabase(item: Item, onComplete: (List<Item>) -> Unit) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.itemDao().insertItem(item)
            }
            // Обновляем список
            val updatedList = loadItemsFromDatabase()
            onComplete(updatedList)
        }
    }
}


@Composable
fun MainScreen() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Hello, Coroutine with Room!")
        }
    }
}

