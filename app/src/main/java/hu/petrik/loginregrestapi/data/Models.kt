package hu.petrik.loginregrestapi.data

import com.squareup.moshi.Json

// A Retool API ilyen kulcsokkal adja vissza az adatokat:
// "Name", "Email", "Password", "Birthdate"
// Kotlinban mi normális camelCase neveket szeretnénk.
// Ezért használjuk a @Json(name="...") annotációt.

data class UserDto(
    val id: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Email") val email: String,
    @Json(name = "Password") val password: String,
    @Json(name = "Birthdate") val birthdate: String
)

// POST-hoz használt request body
data class CreateUserRequest(
    @Json(name = "Name") val name: String,
    @Json(name = "Email") val email: String,
    @Json(name = "Password") val password: String,
    @Json(name = "Birthdate") val birthdate: String
)

// PUT-hoz használt request body
data class UpdateUserRequest(
    @Json(name = "Name") val name: String,
    @Json(name = "Email") val email: String,
    @Json(name = "Password") val password: String,
    @Json(name = "Birthdate") val birthdate: String
)