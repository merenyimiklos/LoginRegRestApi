package hu.petrik.loginregrestapi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

import hu.petrik.loginregrestapi.data.UserDto
import hu.petrik.loginregrestapi.ui.UiState
import hu.petrik.loginregrestapi.vm.UsersViewModel

// Ez a képernyő a felhasználók listáját mutatja.
// Fő extra:
// - Swipe balra: törlés (de dialoggal megerősítjük)
// - Swipe jobbra: szerkesztés (navigálás Edit képernyőre)
@Composable
fun UsersScreen(
    vm: UsersViewModel,
    onEdit: (Long) -> Unit
) {
    val state = vm.state.collectAsState().value

    // Itt tároljuk azt a user-t, akit törölni akarunk.
    // Ha null, nincs dialog. Ha nem null, mutatjuk a megerősítő dialogot.
    val deleteCandidate = remember { mutableStateOf<UserDto?>(null) }

    // LaunchedEffect(Unit): a képernyő első megjelenésekor lefut
    LaunchedEffect(Unit) {
        vm.load()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Felhasználók")

        // Kézi frissítés gomb tanórán hasznos
        OutlinedButton(onClick = vm::load) {
            Text("Frissítés")
        }

        when (state) {
            UiState.Idle -> Text("Készen áll.")
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text("Hiba: ${state.message}")
            is UiState.Data -> UsersList(
                users = state.value,
                onSwipeDelete = { user -> deleteCandidate.value = user },
                onSwipeEdit = { user -> onEdit(user.id) }
            )
        }
    }

    // Törlés megerősítő popup
    if (deleteCandidate.value != null) {
        val u = deleteCandidate.value!!

        AlertDialog(
            onDismissRequest = { deleteCandidate.value = null },
            title = { Text("Törlés") },
            text = { Text("Biztosan törlöd: ${u.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Tényleges törlés a ViewModelben
                        vm.deleteUser(u.id) {
                            // Siker esetén bezárjuk a dialogot
                            deleteCandidate.value = null
                        }
                    }
                ) { Text("Igen") }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteCandidate.value = null }) {
                    Text("Mégse")
                }
            }
        )
    }
}

@Composable
private fun SwipeBackground(value: SwipeToDismissBoxValue) {
    // Ha nincs swipe, ne legyen háttér
    if (value == SwipeToDismissBoxValue.Settled) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    // Itt csak SwipeToDismissBoxValue-t használunk, ahogy kérted
    val (bgColor, icon, alignment) = when (value) {
        SwipeToDismissBoxValue.StartToEnd -> {
            Triple(Color(0xFF2E7D32), Icons.Filled.Edit, Alignment.CenterStart) // jobbra -> edit
        }
        SwipeToDismissBoxValue.EndToStart -> {
            Triple(Color(0xFFC62828), Icons.Filled.Delete, Alignment.CenterEnd) // balra -> delete
        }
        else -> {
            Triple(Color.Transparent, Icons.Filled.Edit, Alignment.Center) // biztosíték
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersList(
    users: List<UserDto>,
    onSwipeDelete: (UserDto) -> Unit,
    onSwipeEdit: (UserDto) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(users, key = { it.id }) { user ->

            // DismissState dönti el, hogy a swipe “elfogadódott-e”.
            // confirmValueChange-ben megfogjuk a swipe eredményt.
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    when (value) {
                        // DismissedToStart = balra húzás
                        SwipeToDismissBoxValue.EndToStart -> {
                            onSwipeDelete(user)
                            false // false: ne tűnjön el az elem automatikusan
                        }

                        // DismissedToEnd = jobbra húzás
                        SwipeToDismissBoxValue.StartToEnd -> {
                            onSwipeEdit(user)
                            false
                        }

                        else -> false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                // háttér tartalom: most üres, hogy ne bonyolítsuk
                backgroundContent = {
                    SwipeBackground(value = dismissState.targetValue)
                },
                content = {
                    UserRow(user)
                }
            )
        }
    }
}

@Composable
private fun UserRow(user: UserDto) {
    // Egy listaelem megjelenítése.
    // Direkt egyszerű, hogy a fókusz az API és a flow maradjon.
    Column(modifier = Modifier.padding(12.dp)) {
        Text(user.name)
        Text(user.email)
        Text("Születés: ${user.birthdate}")
    }
}