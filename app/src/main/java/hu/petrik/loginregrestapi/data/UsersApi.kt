package hu.petrik.loginregrestapi.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Itt írjuk le, hogy milyen HTTP végpontjaink vannak.
// Retrofit ebből automatikusan “legenerálja” a hívásokat.
interface UsersApi {

    // GET: összes user lekérése
    @GET("o1RCDT/users")
    suspend fun getUsers(): List<UserDto>

    // GET: egy user id alapján
    @GET("o1RCDT/users/{id}")
    suspend fun getUser(@Path("id") id: Long): UserDto

    // POST: új user létrehozása
    @POST("o1RCDT/users")
    suspend fun createUser(@Body req: CreateUserRequest): UserDto

    // PUT: user módosítása (id alapján)
    @PUT("o1RCDT/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body req: UpdateUserRequest): UserDto

    // DELETE: user törlése (id alapján)
    @DELETE("o1RCDT/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long)
}