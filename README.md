# AI Game Launcher: Performance Booster

A real-time performance-focused game launcher for Android 10+ that prepares the device before launching games, provides a one-tap boost, and exposes an AI engine interface (heuristics + TFLite stub) for resource tuning.

## Features
- Game detection and library listing
- One-tap boost foreground service with ongoing notification
- Pre-launch optimization then launch target package
- AI engine interface with heuristic implementation; TFLite stub provided
- Basic layouts and Material 3 theme

## Requirements
- Android Studio Jellyfish or newer
- Android Gradle Plugin 8.6+
- Kotlin 2.0+
- Android 10 (API 29) and up

## Permissions
- PACKAGE_USAGE_STATS: guide user to grant Usage Access for monitoring usage
- POST_NOTIFICATIONS: to show foreground service notification (Android 13+)
- FOREGROUND_SERVICE: run booster while optimizing/playing
- QUERY_ALL_PACKAGES: used to list games. For Play release, narrow queries via queries section and intent-filters

## Build & Run
1. Open project in Android Studio
2. Let it sync Gradle
3. Run the `app` configuration on a device (Android 10+)

## Notes on Optimization
- Without root/Shizuku, we cannot directly control CPU/GPU governors; we focus on safe heuristics (memory trim, scheduling hints, prefetched launch)
- Integrate Shizuku or vendor SDKs for deeper control (thermal/cpu policies). Wire into `AiEngine.adjustThermalAndCpuPolicy()`
- Add a TFLite model in `assets/` and load into `TFLiteAiEngine`

## Roadmap
- Real-time overlay for FPS/CPU/GPU using UsageStats and debug overlays
- Per-game profiles with persistence
- AI-driven pre-allocation and dynamic resolution hints (where supported)