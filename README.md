# 👾 Titan Bingo

> **A Multiplayer Retro 8-Bit Bingo Game for Android**

![Project Status](https://img.shields.io/badge/Status-Active_Development-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Backend](https://img.shields.io/badge/Backend-Firebase_Realtime_DB-orange)
![Style](https://img.shields.io/badge/Style-Retro_Pixel_Art-pink)

**Titan Bingo** is a real-time, 1v1 multiplayer Bingo game built with **Java** and **Firebase**. It features a nostalgic 8-bit aesthetic, complete with pixel-perfect fonts, custom XML drawables, and a seamless lobby system.

---

## 📸 Screenshots

| **Home Screen** | **Who Are You?** | **Gameplay** |
|:---:|:---:|:---:|
| <img src="screenshots/home.png" width="200" alt="Home Screen" /> | <img src="screenshots/input.png" width="200" alt="Input Screen" /> | <img src="screenshots/game.png" width="200" alt="Game Board" /> |
| *Retro Menu with 3D Buttons* | *Persistent User Setup* | *Real-time Sync Grid* |

*(Note: Add your screenshots to a folder named `screenshots` in your repo)*

---

## ✨ Features

* **🎨 Retro 8-Bit UI:** Custom-built XML shapes and layers to simulate 3D pixel art buttons and backgrounds without heavy image assets.
* **🔄 Real-Time Multiplayer:** Instant synchronization of turns, moves, and game state using **Firebase Realtime Database**.
* **👤 User System:** Local persistence using `SharedPreferences` so players keep their username across sessions.
* **🎲 Smart Game Logic:**
    * Automated turn switching (P1 vs P2).
    * Real-time line counting logic (horizontal, vertical, diagonal).
    * Instant "BINGO" detection and winner announcement.
* **🏠 Room System (In Progress):** Logic for Hosting and Joining public/private rooms with unique codes.

---

## 🛠️ Tech Stack

* **Language:** Java (Android Native)
* **Minimum SDK:** API 24 (Android 7.0)
* **Backend:** Firebase Realtime Database
* **Font:** `Pixelify Sans` (Google Fonts)
* **Architecture:** Event-driven (Listeners for DB changes)

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
* Android Studio Ladybug (or newer)
* A Firebase Account

### Installation

1.  **Clone the repo**
    ```bash
    git clone [https://github.com/YOUR_USERNAME/Titan-Bingo.git](https://github.com/YOUR_USERNAME/Titan-Bingo.git)
    ```
2.  **Open in Android Studio**
    * File -> Open -> Select the `Titan-Bingo` folder.
3.  **Setup Firebase**
    * Go to the [Firebase Console](https://console.firebase.google.com/).
    * Create a new project.
    * Add an Android App (package name: `app.titan.bingo`).
    * Download the `google-services.json` file.
    * Place `google-services.json` inside the `app/` folder of your project.
    * Enable **Realtime Database** in test mode (allow read/write).
4.  **Build & Run**
    * Sync Gradle files and hit the **Run** button (▶️).

---

## 🎮 How to Play

1.  **Launch:** Open the app. First-time users will be prompted to enter a **Username**.
2.  **Lobby:** On the Home Screen, click **START GAME**.
3.  **Wait:** Wait for a second player to join (or use a second device/emulator).
4.  **Play:**
    * If it's your turn, the status bar will say **"YOUR TURN!"**.
    * Tap a number on the grid to mark it. This marks the same number for your opponent.
    * Turns swap automatically.
5.  **Win:** The first player to complete **5 Lines** wins the game!

---

## 🔮 Roadmap

- [x] Basic 1v1 Gameplay
- [x] Retro UI Overhaul
- [x] Username Persistence
- [ ] **Lobby System:** Host/Join with Room Codes
- [ ] **Private Rooms:** Password protection for games
- [ ] **Sound Effects:** 8-bit click and win sounds
- [ ] **Animations:** Confetti/Particles on win

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

### Acknowledgments
* Font: [Pixelify Sans](https://fonts.google.com/specimen/Pixelify+Sans)
* Inspired by classic 8-bit arcade games.
