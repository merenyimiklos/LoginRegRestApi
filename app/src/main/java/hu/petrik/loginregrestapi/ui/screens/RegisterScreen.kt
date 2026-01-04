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

// Regisztráció UI.
// Itt létrehozunk egy új user-t a POST végponttal.
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val birthdate = remember { mutableStateOf("") }

    val state = vm.registerState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Regisztráció")

        OutlinedTextField(value = name.value, onValueChange = { name.value = it; vm.clearErrors() }, label = { Text("Név") })
        OutlinedTextField(value = email.value, onValueChange = { email.value = it; vm.clearErrors() }, label = { Text("Email") })
        OutlinedTextField(value = password.value, onValueChange = { password.value = it; vm.clearErrors() }, label = { Text("Jelszó") })

        // Egyszerűség: a birthdate string.
        OutlinedTextField(
            value = birthdate.value,
            onValueChange = { birthdate.value = it; vm.clearErrors() },
            label = { Text("Születés (string)") }
        )

        when (state) {
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text("Hiba: ${state.message}")
            else -> Unit
        }

        Button(onClick = { vm.register(name.value, email.value, password.value, birthdate.value, onRegisterSuccess) }) {
            Text("Regisztrálok")
        }

        OutlinedButton(onClick = onBack) {
            Text("Vissza")
        }
    }
}