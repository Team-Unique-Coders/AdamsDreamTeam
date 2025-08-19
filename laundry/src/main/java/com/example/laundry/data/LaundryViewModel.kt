package com.example.laundry.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class LaundryViewModel : ViewModel() {

    // Backing list of all providers (you can later load from repo/DB)
    private val _providers = MutableStateFlow<List<Provider>>(fake)
    val providers: StateFlow<List<Provider>> = _providers.asStateFlow()

    // Simple query/filter examples (optional)
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Derived filtered list
    val filtered: StateFlow<List<Provider>> =
        combine(_providers, _query) { list, q ->
            if (q.isBlank()) list
            else list.filter { it.name.contains(q, ignoreCase = true) || it.address.contains(q, true) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _providers.value
        )

    fun setQuery(value: String) {
        _query.value = value
    }

    // You can mutate your list here if you want to add/remove items at runtime
    fun addProvider(p: Provider) {
        _providers.value = _providers.value + p
    }

    fun removeProvider(predicate: (Provider) -> Boolean) {
        _providers.value = _providers.value.filterNot(predicate)
    }
}
