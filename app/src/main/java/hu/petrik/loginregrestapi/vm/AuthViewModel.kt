package hu.petrik.loginregrestapi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.petrik.loginregrestapi.data.CreateUserRequest
import hu.petrik.loginregrestapi.data.UsersApi
import hu.petrik.loginregrestapi.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Ez a ViewModel kezeli a login és regisztráció logikát.
//
// Fontos: nincs igazi auth a Retool API-ban.
// Oktatási megoldás:
// - Login: GET /users, majd keresés email+jelszó alapján
// - Register: POST /users (új user létrehozása)
class AuthViewModel(
    private val api: UsersApi
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState

    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerState: StateFlow<UiState<Unit>> = _registerState

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("Add meg az emailt és a jelszót.")
            return
        }

        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val users = api.getUsers()

                // Megnézzük, van-e olyan user, akinek egyezik az email és a password
                val ok = users.any {
                    it.email.equals(email, ignoreCase = true) && it.password == password
                }

                if (ok) {
                    _loginState.value = UiState.Data(Unit)
                    onSuccess()
                } else {
                    _loginState.value = UiState.Error("Hibás email vagy jelszó.")
                }
            } catch (t: Throwable) {
                _loginState.value = UiState.Error(t.message ?: "Hálózati hiba")
            }
        }
    }

    fun register(name: String, email: String, password: String, birthdate: String, onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || birthdate.isBlank()) {
            _registerState.value = UiState.Error("Minden mező kötelező.")
            return
        }

        _registerState.value = UiState.Loading
        viewModelScope.launch {
            try {
                api.createUser(
                    CreateUserRequest(
                        name = name,
                        email = email,
                        password = password,
                        birthdate = birthdate
                    )
                )
                _registerState.value = UiState.Data(Unit)
                onSuccess()
            } catch (t: Throwable) {
                _registerState.value = UiState.Error(t.message ?: "Hálózati hiba")
            }
        }
    }

    // Kis segéd: ha gépelnek, eltüntetjük a régi hibaüzenetet.
    fun clearErrors() {
        if (_loginState.value is UiState.Error) _loginState.value = UiState.Idle
        if (_registerState.value is UiState.Error) _registerState.value = UiState.Idle
    }
}