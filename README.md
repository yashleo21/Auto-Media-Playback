# Holofy-Assignment
Assignment for Holofy - Android

Objective: To display a list of cards in Home using RecyclerView, handle auto-playback of media and open the same instance of media in a detail screen with no loss of state when the home card is opened up in detailed view.

Tools & technologies used:

- Codebase in Kotlin
- MVVM Architecture
- Dependency Injection using Hilt
- Coroutines for making the caching network calls
- Exoplayer & Exoplayer's Simple Cache (default LRU) for caching feed videos
- RecyclerView with ListAdapter+DiffUtil for the home feed
- Shared Elements transition between RecyclerView Viewholder & Fragment
- Single Activity app
