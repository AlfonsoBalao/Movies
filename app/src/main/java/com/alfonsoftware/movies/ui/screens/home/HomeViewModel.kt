package com.alfonsoftware.movies.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.alfonsoftware.movies.data.Movie
import com.alfonsoftware.movies.data.local.MoviesDao
import com.alfonsoftware.movies.data.local.toLocalMovie
import com.alfonsoftware.movies.data.local.toMovie
import com.alfonsoftware.movies.data.remote.MovieService
import com.alfonsoftware.movies.data.remote.MoviesRepository
import com.alfonsoftware.movies.data.remote.ServerMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(private val repository: MoviesRepository) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
      viewModelScope.launch {
          _state.value = UiState(loading = true)
          repository.requestMovies()
          repository.movies.collect{
              _state.value = UiState(movies = it)
          }
      }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(favorite = !movie.favorite))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}