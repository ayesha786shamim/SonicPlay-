# 🎵 Bhangra Beats

## Overview
This project delivers a modern and user-friendly music player application tailored for individuals seeking a personalized music experience. Unlike traditional pre-loaded apps, our solution offers enhanced functionalities and customization options for seamless and aesthetically pleasing music playback.

---

## ✨ Features

### Functional Requirements
- 🎶 **Audio Playback**: Supports multiple formats, including MP3, OGG Vorbis, WAV, and AAC.
- ❤️ **Favorites Management**: Mark songs as favorites for quick access using Android's SharedPreferences API.
- 🎨 **Themes**: Offers light and dark mode themes for a personalized look.
- 🔒 **Lock Screen Controls**: Song controls available on the lock screen and in the notification bar.
- 📂 **Playlist Grouping**: Organize songs by artists, albums, or a custom favorites playlist.
- ⏯️ **Playback Features**: Includes pause, resume, next, previous, repeat, shuffle, and a sleep timer.

### Non-Functional Requirements
- 📱 **Compatibility**: Supports devices running Android 6.0 (API 23) and above.
- 🖥️ **Responsive UI**: Optimized for various Android screen sizes.
- ⚡ **Performance**: Efficiently handles libraries with up to 10,000 songs.

---

## 🏗️ Architecture and Design

### Architecture
- 🛠️ **Model-View-Controller (MVC) Pattern**: 
  - **Model**: Manages data and business logic (e.g., `Song` class).
  - **View**: XML layout files for UI components.
  - **Controller**: Handles application flow and updates (e.g., `MainActivity` and fragment subclasses).

### Design Principles
- 🔄 **Fragment Management**: Uses `FragmentTransaction` for seamless transitions.
- 📦 **Serialization**: Implements the `Serializable` interface for model classes.
- 🔗 **Data Binding**: Binds UI components to data sources for dynamic updates.

---

## 🛠️ Tools and Libraries
- 🖥️ **Android Studio**: Primary IDE for development.
- 🗂️ **Git & GitHub**: Version control for tracking changes.
- 🖼️ **Glide**: For efficient image caching.
- 🎛️ **Native MediaPlayer**: For robust audio playback.

---

## ⚙️ Implementation

### Core Techniques
- 🖼️ **Album Art Caching**: Uses Glide to cache and optimize album art.
- 🗃️ **MediaStore API**: Manages song metadata.
- 📁 **SharedPreferences**: Persists user preferences and data locally.

---

## 🖌️ Prototypes
- 📝 **Low-Fidelity Prototype**: Initial sketches and basic designs.
- 🖼️ **High-Fidelity Prototype**: Detailed designs available in [Figma](#).

---

## 🤝 Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

---

## 📜 License
This project is licensed under the MIT License. See the `LICENSE` file for details.
a
