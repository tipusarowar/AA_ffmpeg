# DhakaConnect (ঢাকা কানেক্ট)

A Facebook-style social networking app for Android, built for residents of Dhaka,
Bangladesh. People can post updates, like and comment, add friends, join
neighborhood-and-interest groups, and message each other — all scoped around
real Dhaka areas (Gulshan, Dhanmondi, Mirpur, Uttara, Old Dhaka, and more).

This is a self-contained demo app: it has no real backend. All data — users,
posts, comments, friendships, groups, messages, notifications — lives in a
local Room (SQLite) database on the device, and the app ships with realistic
seed data so it feels alive from the first launch. This makes it easy to run,
demo, and extend without standing up any server infrastructure.

## Features

- **Feed** — text/photo posts from friends and groups, like and comment
- **Create post** — write updates, attach a photo from the device gallery, tag
  a Dhaka area
- **Friends** — friend requests (send/accept/decline), friends list, "people
  you may know" suggestions
- **Groups** — Dhaka-themed community groups (traffic alerts, food, DU alumni,
  heritage walks, tech meetups) with a group feed
- **Profile** — cover photo, avatar, bio, area, own posts, edit profile
- **Messages** — a Messenger-style conversation list and 1:1 chat
- **Notifications** — likes, comments, friend requests/accepts
- **Search** — find people by name or Dhaka area
- Bilingual touches (English/Bangla) reflecting the Dhaka audience

## Try it

The app ships with a pre-seeded demo account:

- **Phone:** `01711000000`
- **Password:** `dhaka123`

Or tap "Create an account" to register a new profile — pick your area from a
list of real Dhaka neighborhoods.

## Tech stack

- **Kotlin** + **Jetpack Compose** (Material 3) for UI
- **MVVM**: `ViewModel` + `StateFlow`, screens built as stateless composables
- **Navigation-Compose**: one root `NavHost` for full-screen destinations, plus
  a nested `NavHost` inside the bottom-navigation `Scaffold` for the four main
  tabs (Home, Friends, Groups, Menu)
- **Room** for local persistence, exposed as `Flow`s so the UI updates reactively
- **DataStore Preferences** for the signed-in session
- **Coil** for async image loading (avatars, cover photos, post photos)
- Manual, lightweight dependency injection via an `AppContainer` (no DI
  framework) — kept simple on purpose for a project this size

## Project structure

```
app/src/main/java/com/dhaka/connect/
├── data/
│   ├── local/            Room entities, DAOs, database, type converters
│   ├── repository/       Repositories wrapping DAOs, exposed as Flows
│   ├── session/           DataStore-backed SessionManager (who's logged in)
│   ├── DhakaAreas.kt      List of Dhaka neighborhoods used across the app
│   ├── SeedData.kt        Demo data populated on first database creation
│   └── PasswordHasher.kt  Local-only SHA-256 hashing for the demo auth flow
├── ui/
│   ├── auth/              Splash, login, register
│   ├── feed/               Feed, create post, post detail/comments
│   ├── friends/            Friends list, requests, suggestions
│   ├── groups/              Groups list and group detail/feed
│   ├── profile/             Profile view and edit
│   ├── notifications/       Notifications list
│   ├── messages/             Conversations list and chat
│   ├── search/                People search
│   ├── home/                  Bottom-nav scaffold + menu tab
│   ├── navigation/            Route definitions and the nav graph
│   └── theme/                  Material 3 color/typography/theme
├── AppContainer.kt        Wires repositories + database + session manager
├── DhakaConnectApp.kt     Application class
└── MainActivity.kt
```

## Running the app

This project uses a standard Gradle/Android Studio layout and was authored in
an environment without the Android SDK installed, so it has not been built
here. To run it:

1. Open the `DhakaConnect/` folder in Android Studio (Koala or newer
   recommended).
2. Let Gradle sync — it will pull the Android Gradle Plugin, Kotlin, Compose,
   Room, Navigation, DataStore and Coil dependencies from Google/Maven
   Central as declared in `app/build.gradle.kts`.
3. Run the `app` module on an emulator or device running Android 8.0 (API 26)
   or newer.

No API keys or backend configuration are required — everything runs locally.

## Notes on the demo data model

Because there's no server, "other users" acting on your posts (likes,
comments, friend requests) are simulated through seed data rather than a
second real account. Posts store a `baseLikeCount` representing likes from
seed users, plus a per-user `post_likes` join table that tracks whether the
signed-in account has liked a post — the displayed count is the sum of both.
Comments, friendships, group memberships, conversations and notifications
follow the same pattern: real local writes on top of a realistic seeded
starting state.
