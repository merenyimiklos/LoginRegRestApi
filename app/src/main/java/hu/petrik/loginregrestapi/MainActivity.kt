package hu.petrik.loginregrestapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.petrik.loginregrestapi.data.RetrofitProvider
import hu.petrik.loginregrestapi.data.UsersApi
import hu.petrik.loginregrestapi.ui.theme.LoginRegRestApiTheme
import hu.petrik.loginregrestapi.vm.AuthViewModel
import hu.petrik.loginregrestapi.vm.EditUserViewModel
import hu.petrik.loginregrestapi.vm.UsersViewModel

// MainActivity: az app belépési pontja.
// Itt:
// 1) létrehozzuk a Retrofitet
// 2) létrehozzuk az API interface implementációját
// 3) létrehozzuk a ViewModel-eket
// 4) betöltjük a navigációt (AppNav)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrofit példány
        val retrofit = RetrofitProvider.create()

        // UsersApi implementáció (Retrofit generálja)
        val api = retrofit.create(UsersApi::class.java)

        // ViewModel-ek
        // Auth: login + register
        val authVm = AuthViewModel(api)

        // Users: lista + delete
        val usersVm = UsersViewModel(api)

        setContent {
            AppNav(
                authVm = authVm,
                usersVm = usersVm,
                // Edit VM gyár: id alapján új EditUserViewModel kell
                editVmFactory = { id -> EditUserViewModel(api, id) }
            )
        }
    }
}