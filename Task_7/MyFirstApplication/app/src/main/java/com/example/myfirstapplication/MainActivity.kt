package com.example.myfirstapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun MovieListAndInput(movieViewModel: MovieViewModel) {
    var movieName by remember { mutableStateOf(TextFieldValue()) }
    var movieDescription by remember { mutableStateOf(TextFieldValue()) }

    val allMovies = movieViewModel.allMovies.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Movie Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = movieDescription,
            onValueChange = { movieDescription = it },
            label = { Text("Movie Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (movieName.text.isNotEmpty() && movieDescription.text.isNotEmpty()) {
                    movieViewModel.addMovie(movieName.text, movieDescription.text)
                    movieName = TextFieldValue("")  // Clear the text fields
                    movieDescription = TextFieldValue("")
                }
            },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text("Add Movie")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allMovies) { movie ->
                MovieItem(movie)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(text = "Name: ${movie.name}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Description: ${movie.description}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieItem(Movie(name = "Inception", description = "A mind-bending thriller"))
}
