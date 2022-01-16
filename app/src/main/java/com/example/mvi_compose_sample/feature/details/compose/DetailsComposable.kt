package com.example.mvi_compose_sample.feature.details.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.compose.rememberImagePainter
import com.example.mvi_compose_sample.BuildConfig
import com.example.mvi_compose_sample.R
import com.example.mvi_compose_sample.feature.details.data.Trailer
import com.example.mvi_compose_sample.feature.details.state.DetailsState
import com.example.mvi_compose_sample.feature.movies.composable.ErrorScreen
import com.example.mvi_compose_sample.feature.movies.composable.LoadingScreen
import com.example.mvi_compose_sample.feature.movies.data.Movie
import com.example.mvi_compose_sample.theme.MVIComposeSampleTheme
import com.example.mvi_compose_sample.theme.Orange
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun MovieDetailsScreen(
    state: LiveData<DetailsState>,
    onFavClicked: () -> Unit,
    onTrailerClick: (String) -> Unit
) {
    val detailsState = state.observeAsState(DetailsState()).value

    with(detailsState) {
        ConstraintLayout(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            val logo = createRef()
            val title = createRef()
            val release = createRef()
            val votes = createRef()
            val rate = createRef()
            val ratingBar = createRef()
            val likeIcon = createRef()
            val plot = createRef()
            val trailersList = createRef()

            Image(
                painter = rememberImagePainter("${BuildConfig.IMAGE_URL}${movie?.poster_path}"),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .width(160.dp)
                    .height(135.dp)
                    .padding(end = 10.dp)
            )

            movie?.title?.let {
                Text(
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(logo.end)
                            top.linkTo(logo.top)
                        },
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(release) {
                        start.linkTo(logo.end)
                        top.linkTo(title.bottom)
                    }
                    .padding(top = 4.dp),
                fontSize = 14.sp,
                text = "Released in : ${movie?.release_date}",
                color = Orange
            )

            Text(
                modifier = Modifier
                    .constrainAs(votes) {
                        start.linkTo(logo.end)
                        top.linkTo(release.bottom)
                    }
                    .padding(top = 4.dp),
                fontSize = 14.sp,
                text = "Votes : ${movie?.vote_count}",
                color = Orange

            )

            Text(
                modifier = Modifier
                    .constrainAs(rate) {
                        start.linkTo(logo.end)
                        top.linkTo(votes.bottom)
                    }
                    .padding(top = 6.dp),
                fontSize = 16.sp,
                text = "${movie?.vote_average}",
                color = Orange
            )

            (movie?.vote_average?.div(2))?.toFloat()?.let {
                RatingBar(
                    modifier = Modifier
                        .constrainAs(ratingBar) {
                            start.linkTo(rate.end)
                            top.linkTo(votes.bottom)
                        }
                        .padding(start = 4.dp, top = 4.dp),
                    value = it,
                    ratingBarStyle = RatingBarStyle.Normal,
                    onRatingChanged = {},
                    onValueChange = {},
                )
            }

            Image(
                modifier = Modifier
                    .constrainAs(likeIcon) {
                        start.linkTo(logo.end)
                        bottom.linkTo(logo.bottom)
                    }
                    .size(30.dp)
                    .padding(top = 4.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onFavClicked()
                    },
                contentDescription = "",
                painter = painterResource(
                    id = if (isLiked) R.drawable.like
                    else R.drawable.dislike
                )
            )

            movie?.overview?.let {
                Text(
                    modifier = Modifier
                        .constrainAs(plot) {
                            start.linkTo(parent.start)
                            top.linkTo(logo.bottom)
                        }
                        .padding(top = 16.dp),
                    text = it,
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(trailersList) {
                        start.linkTo(parent.start)
                        top.linkTo(plot.bottom)
                    }
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                if (isLoading) LoadingScreen()
                errorMessage?.let {
                    ErrorScreen(error = it)
                    return@Box
                }
                if (!trailers.isNullOrEmpty()) TrailerList(trailers, onTrailerClick)
            }
        }
    }
}

@Composable
fun TrailerList(trailers: List<Trailer>, onTrailerClick: (String) -> Unit) {
    LazyColumn {
        items(items = trailers) { trailer ->
            Row(
                Modifier.fillParentMaxWidth(),
            ) {
                repeat(trailers.size) {
                    Box(
                        Modifier.fillMaxWidth()
                    ) {
                        TrailerItem(trailer, onTrailerClick)
                    }
                }
            }
        }
    }
}

@Composable
fun TrailerItem(trailer: Trailer, onTrailerClick: (String) -> Unit) {
    ConstraintLayout {
        val name = createRef()
        val icon = createRef()

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .height(35.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .fillMaxWidth(.9f),
            text = trailer.name
        )

        Image(
            modifier = Modifier
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(name.end)
                }
                .padding(2.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    onTrailerClick(trailer.key)
                },
            painter = painterResource(id = android.R.drawable.ic_media_play),
            colorFilter = ColorFilter.tint(Color.Red),
            contentDescription = "play icon"
        )

    }
}

@Preview
@Composable
fun MovieDetailsPreview() {
    val movieDetailsState = DetailsState(
        movie =
        Movie(
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
        ),
        trailers = arrayListOf(
            Trailer(id = "1", name = "spiderman intro", key = ""),
            Trailer(id = "1", name = "spiderman highlights", key = ""),
            Trailer(id = "1", name = "spiderman end", key = ""),
        ),
        isLiked = true
    )
    MVIComposeSampleTheme {
        MovieDetailsScreen(state = MutableLiveData(movieDetailsState), {}, {})
    }
}

