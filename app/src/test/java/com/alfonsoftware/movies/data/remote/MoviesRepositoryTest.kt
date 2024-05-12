package com.alfonsoftware.movies.data.remote

import com.alfonsoftware.movies.data.Movie
import com.alfonsoftware.movies.data.local.LocalDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking


class MoviesRepositoryTest {

    @Test
    fun `When Db is empty, server called`() {
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 0
        }

        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)


        runBlocking { repository.requestMovies() }


        verifyBlocking(remoteDataSource) { getMovies() }
    }

    @Test
    fun `When Db is empty, movies saved into DB`() {
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 0
        }

        val expectedMovies = listOf(Movie(1, "Title", "Description", "PosterPath", false))
        val remoteDataSource = mock<RemoteDataSource> {
            onBlocking { getMovies() } doReturn expectedMovies
        }
        val repository = MoviesRepository(localDataSource, remoteDataSource)


        runBlocking { repository.requestMovies() }


        verifyBlocking(localDataSource) { insertAll(expectedMovies) }
    }

    @Test
    fun `When DB is not empty, remote datasource not called`() {
        val localDataSource = mock<LocalDataSource>{
            onBlocking { count() } doReturn 1
        }

        val remoteDataSource = mock<RemoteDataSource>()
        val repository = MoviesRepository(localDataSource, remoteDataSource)
        runBlocking { repository.requestMovies() }

        verifyBlocking(remoteDataSource, times(0)) { getMovies() }
    }


    @Test
    fun `When DB is not empty, movies recovered from DB`() {
        val localMovies = listOf(Movie(1, "Title", "Description", "PosterPath", false))
        val remoteMovies = listOf(Movie(2, "Title2", "Description2", "PosterPath2", false))
        val localDataSource = mock<LocalDataSource> {
            onBlocking { count() } doReturn 1
            onBlocking { movies } doReturn flowOf (localMovies)
        }
        val remoteDataSource = mock<RemoteDataSource> {
            onBlocking { getMovies() } doReturn remoteMovies
        }
        val repository = MoviesRepository(localDataSource, remoteDataSource)

        val result = runBlocking {
            repository.requestMovies()
            repository.movies.first()
        }
        assertEquals(localMovies, result)
    }
}

