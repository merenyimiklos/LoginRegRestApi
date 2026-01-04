package hu.petrik.loginregrestapi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.petrik.loginregrestapi.data.UpdateUserRequest
import hu.petrik.loginregrestapi.data.UsersApi
import hu.petrik.loginregrestapi.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// A szerkesztő képernyő form állapota.
// Ez külön van, mert a UI ezt szerkeszti.
data class EditForm(
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val birthdate: String = ""
)

// Ez a ViewModel egyetlen user szerkesztését kezeli.
//
// Feladatai:
// 1) Betölteni a user-t id alapján (GET /users/{id})
// 2) Kezelni a form mezőinek változásait
// 3) Menteni a módosításokat (PUT /users/{id})
class EditUserViewModel(
    private val api: UsersApi,
    private val userId: Long
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<EditForm>>(UiState.Loading)
    val state: StateFlow<UiState<EditForm>> = _state

    init {
        // Amint létrejön, azonnal betölti az adatot
        load()
    }

    private fun load() {
        _state.value = UiState.Loading

        viewModelScope.launch {
            try {
                val u = api.getUser(userId)

                _state.value = UiState.Data(
                    EditForm(
                        id = u.id,
                        name = u.name,
                        email = u.email,
                        password = u.password,
                        birthdate = u.birthdate
                    )
                )
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Nem sikerült betölteni")
            }
        }
    }

    // Ezeket hívja a UI, amikor gépelnek a mezőkbe.
    fun onName(v: String) = update { it.copy(name = v) }
    fun onEmail(v: String) = update { it.copy(email = v) }
    fun onPassword(v: String) = update { it.copy(password = v) }
    fun onBirthdate(v: String) = update { it.copy(birthdate = v) }

    private fun update(block: (EditForm) -> EditForm) {
        val current = (_state.value as? UiState.Data)?.value ?: return
        _state.value = UiState.Data(block(current))
    }

    // PUT /users/{id}
    fun save(onDone: () -> Unit) {
        val form = (_state.value as? UiState.Data)?.value ?: return
        _state.value = UiState.Loading

        viewModelScope.launch {
            try {
                api.updateUser(
                    id = form.id,
                    req = UpdateUserRequest(
                        name = form.name,
                        email = form.email,
                        password = form.password,
                        birthdate = form.birthdate
                    )
                )

                // Siker esetén visszalépünk a listára
                onDone()
            } catch (t: Throwable) {
                _state.value = UiState.Error(t.message ?: "Nem sikerült menteni")
            }
        }
    }
}