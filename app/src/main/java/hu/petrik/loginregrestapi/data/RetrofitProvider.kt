package hu.petrik.loginregrestapi.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Ez a fájl felelős azért, hogy legyen egy Retrofit példányunk.
// Retrofit = “API hívó keret”
// OkHttp = a motor, ami ténylegesen elküldi a requestet
// Moshi = JSON átalakítás Kotlin data class-ra
object RetrofitProvider {

    // A Retrofit baseUrl mindig / jellel végződjön
    private const val BASE_URL = "https://retoolapi.dev/"

    fun create(): Retrofit {
        // Logging interceptor: a Logcatben látni fogjátok az összes requestet és response-t
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient: hálózati beállítások + interceptorok
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Moshi: JSON <-> Kotlin átalakítás
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // Retrofit példány összerakása
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}