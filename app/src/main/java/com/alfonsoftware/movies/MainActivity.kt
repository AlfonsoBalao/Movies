package com.alfonsoftware.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.room.Room
import com.alfonsoftware.movies.data.local.MoviesDatabase
import com.alfonsoftware.movies.data.local.LocalDataSource
import com.alfonsoftware.movies.data.remote.MoviesRepository
import com.alfonsoftware.movies.data.remote.RemoteDataSource
import com.alfonsoftware.movies.ui.screens.home.Home

class MainActivity : ComponentActivity() {

    lateinit var db : MoviesDatabase

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java, "movies-db"
        ).build()

        val repository= MoviesRepository(
            localDataSource = LocalDataSource( db.moviesDao()),
            remoteDataSource = RemoteDataSource()
        )

        setContent {
          Home(repository)
        }
    }
}