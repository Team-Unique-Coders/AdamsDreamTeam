package com.example.laundry.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundry.screens.Order
import com.project.common_utils.IconTextModel
import com.project.common_utils.R
import com.project.common_utils.TextSpacerTextModel
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

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()


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

    /** Add a new order */
    fun addOrder(order: Order) {
        _orders.value = _orders.value + order
    }

    /** Remove an existing order */
    fun removeOrder(order: Order) {
        _orders.value = _orders.value - order
    }

    /** Replace an order (e.g., update by index or id) */
    fun updateOrder(old: Order, new: Order) {
        _orders.value = _orders.value.map { if (it == old) new else it }
    }

    /** Get current orders list */
    fun getOrders(): List<Order> = _orders.value

    /** Clear all orders */
    fun clearOrders() {
        _orders.value = emptyList()
    }
}

