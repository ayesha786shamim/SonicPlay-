# 🎵 SonicPlay - The Ultimate Music Experience

<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original-wordmark.svg" alt="SonicPlay Logo" width="150"/>
  <br><br>
  <img src="https://img.shields.io/badge/Android-6.0%2B-brightgreen?style=for-the-badge&logo=android" alt="Android Version">
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License">
  <img src="https://img.shields.io/badge/API-23%2B-orange?style=for-the-badge&logo=google" alt="API Level">
  <img src="https://img.shields.io/badge/Version-1.0.0-red?style=for-the-badge" alt="Version">
</p>

## 🌟 Overview

SonicPlay is a cutting-edge music player that transforms your listening experience with advanced features and beautiful design. Perfect for audiophiles who want complete control and style in their music app.


---

## ✨ Key Features

### 🎛 Functional Features

<table align="center">
  <tr>
    <th>Feature</th>
    <th>Description</th>
    <th>Icon</th>
  </tr>
  <tr>
    <td><b>High-Quality Playback</b></td>
    <td>Supports MP3, OGG, WAV, AAC formats with pristine audio quality</td>
    <td align="center">🔊</td>
  </tr>
  <tr>
    <td><b>Favorites System</b></td>
    <td>Save and organize your favorite tracks </td>
    <td align="center">❤</td>
  </tr>
  <tr>
    <td><b>Theme Customization</b></td>
    <td>Light & Dark modes with accent color options to match your style</td>
    <td align="center">🎨</td>
  </tr>
  <tr>
    <td><b>Lock Screen Controls</b></td>
    <td>Full playback control without unlocking your device</td>
    <td align="center">🔒</td>
  </tr>
  <tr>
    <td><b>Advanced Controls</b></td>
    <td>Shuffle, repeat modes, sleep timer, and audio visualization</td>
    <td align="center">⏯</td>
  </tr>
</table>

### 🏆 Non-Functional Features

<div style="display: flex; justify-content: space-around; flex-wrap: wrap; margin: 20px 0;">
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>Broad Compatibility</b><br>
    Android 6.0+ (API 23)
  </div>
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>Smooth UI</b><br>
    Responsive across all android devices
  </div>
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>High Performance</b><br>
    Handles 10,000+ songs
  </div>
</div>

---

## 🏗 Architecture & Design

### 🧱 MVC Pattern

<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/androidstudio/androidstudio-original.svg" width="200" alt="Android Architecture">
</p>

<table align="center">
  <tr>
    <th>Component</th>
    <th>Role</th>
    <th>Technology</th>
  </tr>
  <tr>
    <td><b>Model</b></td>
    <td>Data & business logic</td>
    <td>Java Classes</td>
  </tr>
  <tr>
    <td><b>View</b></td>
    <td>UI Presentation</td>
    <td>XML Layouts</td>
  </tr>
  <tr>
    <td><b>Controller</b></td>
    <td>User interaction</td>
    <td>Activities/Fragments</td>
  </tr>
</table>

### 🎨 Design Highlights

<div style="display: flex; justify-content: space-around; flex-wrap: wrap; margin: 20px 0;">
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>Smooth Transitions</b><br>
    Elegant fragment and activity animations
  </div>
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>Optimized Data</b><br>
    Efficient caching and memory management
  </div>
  <div style="text-align: center; margin: 10px; width: 200px;">
    <b>Real-time Updates</b><br>
    Responsive UI with background processing
  </div>
</div>

---

## 🛠 Tech Stack

<div align="center" style="margin: 30px 0;">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="60" alt="Java" style="margin: 0 15px;">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original.svg" width="60" alt="Android" style="margin: 0 15px;">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="60" alt="GitHub" style="margin: 0 15px;">
</div>

- *Development*: Android Studio (Official IDE)
- *Version Control*: Git & GitHub
- *Image Loading*: Glide Library v4.12.0
- *Audio Engine*: ExoPlayer with native MediaPlayer API support
- *Storage*: Shared PrefrencesLibrary

---

## ⚙ Core Implementation

public class MediaPlayerController {

    private MediaPlayer mediaPlayer;
    private Context context;
    private List<Song> songList;  // List of all songs
    private int currentSongIndex;  // Index of the currently playing song
    private SeekBar songSeekBar;
    private boolean isPlaying = false;
    private Handler seekBarHandler;

    public MediaPlayerController(Context context, List<Song> songList, int startSongIndex, SeekBar songSeekBar) {
        this.context = context;
        this.songList = songList;
        this.currentSongIndex = startSongIndex;
        this.songSeekBar = songSeekBar;

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        seekBarHandler = new Handler();

        setupMediaPlayerListener();
        setupSeekBarListener();
    }
    
    // Other implementation methods
}



## 🚀 Getting Started

1. Clone the repository:
   bash
   git clone https://github.com/yourusername/SonicPlay.git
   

2. Open the project in Android Studio

3. Sync Gradle and run on your device or emulator


## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m 'Add some amazing feature')
4. Push to the branch (git push origin feature/amazing-feature)
5. Open a Pull Request

---

<p align="center">
  Made with ❤ by Ayesha
</p>
