package hu.petrik.loginregrestapi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.petrik.loginregrestapi.ui.UiState
import hu.petrik.loginregrestapi.vm.AuthViewModel

// Login UI.
// A beviteli mezők lokális state-ben vannak (remember), mert itt egyszerűség a cél.
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // A ViewModel állapotát figyeljük
    val state = vm.loginState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Bejelentkezés")

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
                vm.clearErrors()
            },
            label = { Text("Email") },
            singleLine = true
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                vm.clearErrors()
            },
            label = { Text("Jelszó") },
            singleLine = true
        )

        when (state) {
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text("Hiba: ${state.message}")
            else -> Unit
        }

        Button(onClick = { vm.login(email.value, password.value, onLoginSuccess) }) {
            Text("Belépés")
        }

        OutlinedButton(onClick = onGoRegister) {
            Text("Regisztráció")
        }
    }
}