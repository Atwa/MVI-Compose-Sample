package com.example.mvi_compose_sample.feature.details.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mvi_compose_sample.utils.AppConstants
import com.example.mvi_compose_sample.feature.details.compose.MovieDetailsScreen
import com.example.mvi_compose_sample.feature.details.intent.DetailsIntent
import com.example.mvi_compose_sample.feature.details.viewmodel.DetailsViewModel
import com.example.mvi_compose_sample.feature.movies.data.Movie
import com.example.mvi_compose_sample.theme.MVIComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<Movie>(AppConstants.MOVIE_BUNDLE_KEY)?.let {
            viewModel.movie = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContent {
            MVIComposeSampleTheme {
                MovieDetailsScreen(
                    state = viewModel.state,
                    onFavClicked = ::onFavClicked, onTrailerClick = ::onTrailerClicked)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendIntent(DetailsIntent.FetchTrailers)
        sendIntent(DetailsIntent.GetLikeState)
    }

    private fun onTrailerClicked(trailerKey: String) {
        val intent = try {
            Intent(Intent.ACTION_VIEW, Uri.parse("${AppConstants.YOUTUBE_APP_URI}$trailerKey"))
        } catch (ex: ActivityNotFoundException) {
            Intent(Intent.ACTION_VIEW, Uri.parse("${AppConstants.YOUTUBE_WEB_URI}$trailerKey"))
        }
        startActivity(intent)
    }

    private fun onFavClicked() {
        sendIntent(DetailsIntent.UpdateLikeState)
    }


    private fun sendIntent(intent: DetailsIntent) {
        lifecycleScope.launch { viewModel.intent.send(intent) }
    }

}




