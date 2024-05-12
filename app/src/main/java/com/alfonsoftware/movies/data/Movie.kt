package com.alfonsoftware.movies.data

import androidx.room.Entity

@Entity
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val favorite: Boolean
)
