/*https://chatgpt.com
* prompt: build a search query for my checkInventory koitlin screen use this use case for it
class GetInventoryUseCase(
    private val inventoryClient: InventoryClient

) {

    suspend fun execute(): List<Inventory> {
        return inventoryClient.getInventory()
    }
} this will return a list of names have it so I can search this list and it will populate entries*/

package com.example.delitelligencefrontend.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.delitelligencefrontend.domain.InventoryUseCase
import com.example.delitelligencefrontend.model.Inventory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@HiltViewModel
class InventoryQueryViewModel @Inject constructor(
    private val getInventoryUseCase: InventoryUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _inventoryList = MutableStateFlow<List<Inventory>>(emptyList())

    val inventoryList: StateFlow<List<Inventory>> = combine(
        _searchQuery,
        _inventoryList
    ) { query, list ->
        if (query.isEmpty()) list
        else list.filter { inventory ->
            inventory.products.any { it.productName.contains(query, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            val inventory = getInventoryUseCase.execute()
            _inventoryList.value = inventory
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
