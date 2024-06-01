package com.notemates.ui.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.notemates.R
import com.notemates.core.utils.StateStatus
import com.notemates.data.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val application: Application,
    private val searchRepository: SearchRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun search(keyword: String) {
        _uiState.value = uiState.value.copy(
            action = SearchUiState.Action.Search,
            status = StateStatus.Loading,
        )

        viewModelScope.launch {
            val result = searchRepository.search(keyword)
            launch {
                when (result) {
                    is Either.Left -> _uiState.value = uiState.value.copy(
                        action = SearchUiState.Action.Search,
                        status = StateStatus.Failure,
                        message = result.value.message
                            ?: application.getString(R.string.something_went_wrong),
                    )

                    is Either.Right -> _uiState.value = uiState.value.copy(
                        action = SearchUiState.Action.Search,
                        status = StateStatus.Success,
                        searchResponse = result.value,
                    )
                }
            }
        }
    }
}