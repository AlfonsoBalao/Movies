package com.alfonsoftware.movies

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alfonsoftware.movies.ui.theme.MoviesTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// Api key -> 1e4876ca00a766979c52932487cd413b

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesTheme {

                val movies = produceState<List<ServerMovie>>(initialValue = emptyList()) {
                   value = Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MovieService::class.java)
                        .getMovies()
                        .results

                }


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold (
                        topBar = {
                            TopAppBar(title = { Text(text = "Movies") })
                        }
                    ){padding ->
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(120.dp),
                            modifier = Modifier.padding(padding),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            items(movies.value){movie ->
                                MovieItem(movie)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MovieItem(movie: ServerMovie) {
    Column {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500/${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
        )
        Text(text = movie.title,
            modifier = Modifier.padding(16.dp).height(48.dp).fillMaxWidth(),
            maxLines = 2
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoviesTheme {
        Greeting("Android")
    }
}