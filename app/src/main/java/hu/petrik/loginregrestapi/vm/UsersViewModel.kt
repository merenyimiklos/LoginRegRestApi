package hu.petrik.loginregrestapi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.petrik.loginregrestapi.data.UserDto
import hu.petrik.loginregrestapi.data.UsersApi
import hu.petrik.loginregrestapi.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Ez a ViewModel a "Felhasználók listája" képernyőhöz tartozik.
//
// Feladatai:
// 1) Lekérni a user listát GET-tel
// 2) Törölni egy user-t DELETE-tel
// 3) Frissíteni a listát törlés után
class UsersViewModel(
    private val api: UsersApi
) : ViewModel() {

    // A képernyő állapota (Loading, Data, Error)
    private val _state = MutableStateFlow<UiState<List<UserDto>>>(UiState.Idle)
    val state: StateFlow<UiState<List<UserDto>>> = _state

    // GET /users
    fun load() {
        _state.value = UiState.Loading

        // viewModelScope.launch: coroutine a ViewModel életciklusához kötve
        viewModelScope.launch {
            try {
                val users = api.getUsers()
                _state.value = UiState.Data(users)
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Hálózati hiba")
            }
        }
    }

    // DELETE /users/{id}
    // onDone: azért van, hogy a UI pl. bezárhassa a dialogot siker esetén
    fun deleteUser(id: Long, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                api.deleteUser(id)

                // Törlés után frissítjük a listát (új GET)
                load()

                onDone()
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Nem sikerült törölni")
            }
        }
    }
}