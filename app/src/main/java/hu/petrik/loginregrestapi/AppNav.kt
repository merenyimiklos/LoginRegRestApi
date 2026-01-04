package hu.petrik.loginregrestapi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.petrik.loginregrestapi.ui.screens.EditUserScreen
import hu.petrik.loginregrestapi.ui.screens.LoginScreen
import hu.petrik.loginregrestapi.ui.screens.RegisterScreen
import hu.petrik.loginregrestapi.ui.screens.UsersScreen
import hu.petrik.loginregrestapi.vm.AuthViewModel
import hu.petrik.loginregrestapi.vm.EditUserViewModel
import hu.petrik.loginregrestapi.vm.UsersViewModel

// Route-ok: egyszerű stringek.
// Edit route-ban paramétert adunk át: user id.
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val USERS = "users"
    const val EDIT = "edit"
}

// AppNav: összeköti a képernyőket.
// Itt döntjük el, melyik képernyőről hova lehet navigálni.
@Composable
fun AppNav(
    authVm: AuthViewModel,
    usersVm: UsersViewModel,
    editVmFactory: (Long) -> EditUserViewModel
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {
        // LOGIN képernyő
        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authVm,
                onLoginSuccess = {
                    // Belépés után: Users képernyő.
                    // popUpTo: hogy ne lehessen “Back”-kel visszamenni loginra.
                    nav.navigate(Routes.USERS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoRegister = {
                    nav.navigate(Routes.REGISTER)
                }
            )
        }

        // REGISTER képernyő
        composable(Routes.REGISTER) {
            RegisterScreen(
                vm = authVm,
                onRegisterSuccess = {
                    // Regisztráció után vissza loginra
                    nav.popBackStack()
                },
                onBack = {
                    nav.popBackStack()
                }
            )
        }

        // USERS képernyő
        composable(Routes.USERS) {
            UsersScreen(
                vm = usersVm,
                onEdit = { id ->
                    // Swipe jobbra esetén ide jutunk.
                    nav.navigate("${Routes.EDIT}/$id")
                }
            )
        }

        // EDIT képernyő paraméterrel
        composable(
            route = "${Routes.EDIT}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L

            // remember(id): ha másik id-re navigálunk, új VM-et készítünk
            val editVm = remember(id) { editVmFactory(id) }

            EditUserScreen(
                vm = editVm,
                onDone = {
                    // Mentés után frissítjük a listát és visszalépünk
                    usersVm.load()
                    nav.popBackStack()
                }
            )
        }
    }
}