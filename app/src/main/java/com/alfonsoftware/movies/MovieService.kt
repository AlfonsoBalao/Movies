package com.alfonsoftware.movies

import retrofit2.http.GET


interface MovieService {
    @GET("discover/movie?api_key=1e4876ca00a766979c52932487cd413b")
    suspend fun getMovies(): MovieResult
}