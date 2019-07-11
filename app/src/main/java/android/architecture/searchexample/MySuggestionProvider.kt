package android.architecture.searchexample

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object{
        const val AUTHORITY = "com.example.MySuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }
    init {
        setupSuggestions(AUTHORITY,MODE)
    }
}