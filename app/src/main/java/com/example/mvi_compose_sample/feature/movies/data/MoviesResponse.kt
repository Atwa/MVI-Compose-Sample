package com.example.mvi_compose_sample.feature.movies.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesResponse(
    var page: Int?,
    var total_results: Int?,
    var total_pages: Int,
    var results: List<Movie>) : Parcelable