package com.hana.umuljeong.data.datasource

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val searchHistoryKey = "search_history"
    private val maxHistorySize = 5

    fun addSearchQuery(query: String) {
        val searchHistory =
            sharedPreferences.getStringSet(searchHistoryKey, mutableSetOf()) ?: mutableSetOf()
        searchHistory.add(query)
        if (searchHistory.size > maxHistorySize) {
            searchHistory.remove(searchHistory.first())
        }
        sharedPreferences.edit().putStringSet(searchHistoryKey, searchHistory).apply()
    }

    fun getSearchHistory(): List<String> {
        val searchHistory =
            sharedPreferences.getStringSet(searchHistoryKey, mutableSetOf()) ?: mutableSetOf()
        return ArrayList(searchHistory)
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove(searchHistoryKey).apply()
    }
}




