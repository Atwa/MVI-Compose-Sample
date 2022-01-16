package com.example.mvi_compose_sample.feature.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvi_compose_sample.utils.AppConstants
import com.example.mvi_compose_sample.R
import com.example.mvi_compose_sample.feature.movies.composable.*
import com.example.mvi_compose_sample.feature.movies.data.Movie
import com.example.mvi_compose_sample.feature.movies.intent.MoviesIntent
import com.example.mvi_compose_sample.feature.movies.viewmodel.MoviesViewModel
import com.example.mvi_compose_sample.theme.MVIComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()

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
                MoviesScreen(state = viewModel.state, onMovieClick = ::onMovieClicked)
            }
        }
    }

    private fun onMovieClicked(movie: Movie) {
        activity?.let {
            findNavController().navigate(
                R.id.details,
                Bundle().apply { putParcelable(AppConstants.MOVIE_BUNDLE_KEY, movie) })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendIntent(MoviesIntent.FetchMovies)
    }

    private fun sendIntent(intent: MoviesIntent) {
        lifecycleScope.launch { viewModel.intent.send(intent) }
    }

}




