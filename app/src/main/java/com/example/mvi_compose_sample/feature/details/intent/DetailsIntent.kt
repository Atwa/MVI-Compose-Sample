package com.example.mvi_compose_sample.feature.details.intent

sealed class DetailsIntent {
    object FetchTrailers : DetailsIntent()
    object GetLikeState : DetailsIntent()
    object UpdateLikeState : DetailsIntent()
}