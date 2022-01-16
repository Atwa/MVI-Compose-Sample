package com.example.mvi_compose_sample.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S , I> : ViewModel() {

    val intent: Channel<I> = Channel()
    private val _state: MutableLiveData<S> = MutableLiveData()
    val state: LiveData<S> = _state

    abstract fun getInitialState(): S

    init { observeIntents() }

    private fun observeIntents() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { processIntents(it) }
        }
    }

    protected suspend fun updateState(handler: suspend (oldState: S) -> S) {
        _state.postValue(handler(state.value ?: getInitialState()))
    }

    abstract fun processIntents(intent: I)

}