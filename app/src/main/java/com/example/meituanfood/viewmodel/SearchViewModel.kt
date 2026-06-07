package com.example.meituanfood.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.meituanfood.data.model.Restaurant
import com.example.meituanfood.data.repository.MockData
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : AndroidViewModel(app) {

    private val _results = MutableLiveData<List<Restaurant>>()
    val results: LiveData<List<Restaurant>> = _results

    private val _recentSearches = MutableLiveData(
        listOf("Burger Lab", "Pizza", "Sushi", "Desi", "Dessert")
    )
    val recentSearches: LiveData<List<String>> = _recentSearches

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    private val _isEmpty = MutableLiveData(false)
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _currentQuery = MutableLiveData("")
    val currentQuery: LiveData<String> = _currentQuery

    val hotKeywords = MockData.getHotKeywords()

    fun search(query: String) {
        if (query.isBlank()) return
        _currentQuery.value = query
        viewModelScope.launch {
            _isSearching.value = true
            kotlinx.coroutines.delay(400)
            val all = MockData.getRestaurants()
            val filtered = all.filter { r ->
                r.name.contains(query, true) ||
                r.categories.any { it.contains(query, true) } ||
                r.tags.any { it.contains(query, true) }
            }
            _results.value = filtered
            _isEmpty.value = filtered.isEmpty()
            _isSearching.value = false
            addToRecent(query)
        }
    }

    private fun addToRecent(query: String) {
        val current = _recentSearches.value?.toMutableList() ?: mutableListOf()
        current.remove(query)
        current.add(0, query)
        _recentSearches.value = current.take(8)
    }

    fun clearRecent() {
        _recentSearches.value = emptyList()
    }

    fun clearResults() {
        _results.value = emptyList()
        _isEmpty.value = false
        _currentQuery.value = ""
    }
}
