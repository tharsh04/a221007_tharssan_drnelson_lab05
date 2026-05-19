package com.example.a221007_tharssan_nelson_lab05.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a221007_tharssan_nelson_lab05.data.CupcakeOrderEntity
import com.example.a221007_tharssan_nelson_lab05.data.CupcakeOrderRepository
import com.example.a221007_tharssan_nelson_lab05.data.CupcakeOrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel(private val repository: CupcakeOrderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CupcakeOrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<CupcakeOrderUiState> = _uiState.asStateFlow()

    // Expose Room database flow tracking state updates live
    val allOrders: StateFlow<List<CupcakeOrderEntity>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setQuantity(numberCupcakes: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberCupcakes,
                price = calculatePrice(quantity = numberCupcakes)
            )
        }
    }

    fun setFlavor(desiredFlavor: String) {
        _uiState.update { currentState ->
            currentState.copy(flavor = desiredFlavor)
        }
    }

    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }

    fun resetOrder() {
        _uiState.value = CupcakeOrderUiState(pickupOptions = pickupOptions())
    }

    fun saveOrderToDatabase() {
        viewModelScope.launch {
            val entity = CupcakeOrderEntity(
                quantity = _uiState.value.quantity,
                flavor = _uiState.value.flavor,
                date = _uiState.value.date,
                price = _uiState.value.price
            )
            repository.insert(entity)
        }
    }

    fun deleteOrder(order: CupcakeOrderEntity) {
        viewModelScope.launch {
            repository.delete(order)
        }
    }

    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        return NumberFormat.getCurrencyInstance().format(calculatedPrice)
    }

    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }
}