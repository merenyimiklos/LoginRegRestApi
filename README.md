# LoginRegRestApi – Jetpack Compose CRUD mintaalkalmazás

Ez a projekt egy **oktatási célú Android alkalmazás**, amely bemutatja,
hogyan lehet **Jetpack Compose + Retrofit + REST API** segítségével
egy egyszerű, de teljes értékű CRUD alkalmazást készíteni.

A projekt kifejezetten **tanórai használatra** lett felépítve:
- kevés réteg
- átlátható mappa struktúra
- kommentelt kód
- lépésről lépésre bővíthető funkcionalitás

---

## A projekt célja

A diákok megértsék és gyakorolják:
- REST API használatát Androidon
- GET / POST / PUT / DELETE műveleteket
- aszinkron hálózati hívásokat (coroutines)
- state-alapú UI-t Jetpack Compose-ban
- alap navigációt több képernyő között
- modern mobil UX-et (swipe gesztusok)

---

## Funkcionalitás

### Bejelentkezés
- Email + jelszó alapú login
- Oktatási megoldás:
  - lekérjük a felhasználók listáját
  - megkeressük az egyező email + jelszó párost

### Regisztráció
- Új felhasználó létrehozása REST API-n keresztül
- POST kérés a szerver felé
- Sikeres regisztráció után visszalépés a login képernyőre

### Felhasználók listája
- Felhasználók lekérése API-ból (GET)
- LazyColumn megjelenítés
- Manuális frissítés gomb

### Swipe műveletek a listában
- **Jobbra húzás** → szerkesztés
  - zöld háttér
  - ceruza ikon
- **Balra húzás** → törlés
  - piros háttér
  - kuka ikon
  - megerősítő dialog

### Felhasználó szerkesztése
- Egy felhasználó betöltése ID alapján
- Előtöltött mezők
- Mentés PUT kéréssel
- Visszalépés a listára frissítéssel

---

##  Használt REST API

A projekt a **Retool public REST API**-t használja:


Támogatott műveletek:
- GET – felhasználók lekérése
- POST – új felhasználó létrehozása
- PUT – felhasználó módosítása
- DELETE – felhasználó törlése

> Megjegyzés: ez egy demo API, valódi authentikáció nincs mögötte.

---

## Projekt struktúra

com.example.loginregrestapi
│
├── app
│ ├── MainActivity.kt // Belépési pont, ViewModel-ek összedrótozása
│ └── AppNav.kt // Navigáció (Login, Register, Users, Edit)
│
├── data
│ ├── RetrofitProvider.kt // Retrofit + Moshi + OkHttp konfiguráció
│ ├── UsersApi.kt // REST végpontok
│ └── Models.kt // DTO és request modellek
│
├── ui
│ ├── UiState.kt // Loading / Data / Error állapot
│ └── screens
│ ├── LoginScreen.kt
│ ├── RegisterScreen.kt
│ ├── UsersScreen.kt
│ └── EditUserScreen.kt
│
└── vm
├── AuthViewModel.kt // Login + regisztráció logika
├── UsersViewModel.kt // Lista + törlés
└── EditUserViewModel.kt // Szerkesztés


---

## Használt technológiák

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **ViewModel**
- **Coroutines**
- **Retrofit**
- **Moshi**
- **OkHttp**

---

## ⚙️ Fontos beállítás

### Internet permission

Az API hívásokhoz kötelező:

```xml
<uses-permission android:name="android.permission.INTERNET" />
