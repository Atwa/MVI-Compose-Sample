package com.example.mvi_compose_sample.feature.details.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerResponse(
    var id: Int,
    var results: List<Trailer>
) : Parcelable