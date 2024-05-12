package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MoviesViewModelFactory(private val repository: MovieRepository): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesViewModel::class.java)){
            return MoviesViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        viewModelScope.launch {
            repository.getAllMovies().distinctUntilChanged()
                .collect { listOfMovies ->
                    _movies.value = listOfMovies
                }
        }
    }
}

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieWithImages = MutableStateFlow<MovieWithImages?>(null)
    val movieWithImages: StateFlow<MovieWithImages?> = _movieWithImages

    fun fetchMovieById(movieId: Long) {
        viewModelScope.launch {
            repository.getById(movieId).collect { movieWithImages ->
                _movieWithImages.value = movieWithImages        //?
            }
        }
    }
}

class WatchlistViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _watchlistMovies = MutableStateFlow<List<Movie>>(emptyList())
    val watchlistMovies: StateFlow<List<Movie>> = _watchlistMovies

    init {
        viewModelScope.launch {
            repository.getFavoriteMovies().distinctUntilChanged()
                .collect { listOfMovies ->
                    _watchlistMovies.value = listOfMovies
                }
        }
    }
}