package com.notemates.ui.search

import com.notemates.core.utils.StateStatus
import com.notemates.data.models.responses.SearchResponse

data class SearchUiState(
    val action: Action = Action.Initial,
    val status: StateStatus = StateStatus.Initial,
    val message: String = "",
    val searchResponse: SearchResponse? = null,
) {
    enum class Action {
        Initial, Search,
    }
}
