package com.example.myfirstapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {

    private val _allMovies = mutableStateOf<List<Movie>>(emptyList())
    val allMovies: State<List<Movie>> = _allMovies

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _allMovies.value = movieDao.getAllMovies()
        }
    }

    fun addMovie(name: String, description: String) {
        viewModelScope.launch {
            val movie = Movie(name = name, description = description)
            movieDao.insert(movie)
            loadMovies()
        }
    }
}
