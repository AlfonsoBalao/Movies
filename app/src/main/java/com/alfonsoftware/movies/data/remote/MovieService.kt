package com.alfonsoftware.movies.data.remote

import retrofit2.http.GET


interface MovieService {
    //Introduce your TMDB api here ↓
    @GET("discover/movie?api_key=")
    suspend fun getMovies(): MovieResult
}