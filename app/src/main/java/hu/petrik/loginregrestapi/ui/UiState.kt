package hu.petrik.loginregrestapi.ui

// Egységes UI állapot.
// A képernyők így egyszerűek:
// - Loading: mutatunk progress-t
// - Data: mutatjuk az adatot
// - Error: kiírjuk a hibát
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Data<T>(val value: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}