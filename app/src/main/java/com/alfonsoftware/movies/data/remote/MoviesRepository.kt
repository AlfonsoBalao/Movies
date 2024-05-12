package com.alfonsoftware.movies.data.remote

import com.alfonsoftware.movies.data.Movie
import com.alfonsoftware.movies.data.local.LocalDataSource
import kotlinx.coroutines.flow.Flow


class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource

) {
   val movies: Flow<List<Movie>> = localDataSource.movies

    suspend fun updateMovie(movie: Movie){
        localDataSource.updateMovie(movie)
}

    suspend fun requestMovies(){
        val isDbEmpty = localDataSource.count() == 0
        if (isDbEmpty){
            localDataSource.insertAll(remoteDataSource.getMovies())
        }
    }
}