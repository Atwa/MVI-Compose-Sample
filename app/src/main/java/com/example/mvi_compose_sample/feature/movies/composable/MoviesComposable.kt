package com.example.mvi_compose_sample.feature.movies.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import coil.compose.rememberImagePainter
import com.example.mvi_compose_sample.BuildConfig
import com.example.mvi_compose_sample.feature.movies.data.Movie
import com.example.mvi_compose_sample.feature.movies.state.MoviesState
import com.example.mvi_compose_sample.theme.MVIComposeSampleTheme
import com.example.mvi_compose_sample.theme.Purple700


@Composable
fun MoviesScreen(state: LiveData<MoviesState>, onMovieClick: (Movie) -> Unit) {
    val moviesState = state.observeAsState().value
    moviesState?.let {
        with(it) {
            if (isLoading) LoadingScreen()
            errorMessage?.let { error ->
                ErrorScreen(error = error)
            }
            if (!movies.isNullOrEmpty()) MoviesListScreen(movies = movies, onMovieClick)
        }
    }
}

@Composable
fun MoviesListScreen(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    val rowSize = 2
    LazyColumn() {
        items(items = movies.chunked(rowSize)) { row ->
            Row(
                Modifier
                    .fillParentMaxWidth()
                    .fillParentMaxHeight(.5f),
            ) {
                for ((index, item) in row.withIndex()) {
                    Box(
                        Modifier
                            .fillMaxWidth(1f / (rowSize - index))
                            .padding(1.dp)
                    ) {
                        MovieItem(item, onMovieClick)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 7.dp,
        modifier = Modifier
            .shadow(2.dp)
            .clickable(onClick = {
                onMovieClick(movie)
            })
    ) {
        Image(
            painter = rememberImagePainter(data = "${BuildConfig.IMAGE_URL}${movie.poster_path}"),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun LoadingScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(color = Purple700)
    }
}

@Composable
fun ErrorScreen(error: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "Oops, $error!")
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    MVIComposeSampleTheme {
        ErrorScreen("Network Error!")
    }
}

@Preview
@Composable
fun LoadingPreview() {
    MVIComposeSampleTheme {
        LoadingScreen()
    }
}

@Preview
@Composable
fun MoviesListPreview() {
    val movie = Movie(
        id = 1,
        poster_path = "https://picsum.photos/seed/picsum/200/300\n",
        title = "SpiderMan",
        release_date = "2021-05-26",
        vote_average = 8.7,
        vote_count = 100,
        original_language = "en",
        original_title = "",
        popularity = 88.5,
        video = true,
        overview = "Spiderman movies is about man turning into a spider which can fly and attack bad people falling them dead",
    )
    MVIComposeSampleTheme {
        MoviesListScreen(
            arrayListOf(
                movie,
                movie,
                movie,
                movie
            )
        ) {}
    }
}

