# 🍽️ CaloryTracker

> A modern, feature-rich Android calorie & macro tracking app built with **Jetpack Compose**, **Material 3**, and a **custom charting library**. Track your nutrition with beautiful visualizations, local-first data storage, and Garmin integration.

![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-4285F4?logo=jetpackcompose&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-1.3.0-6750A4?logo=materialdesign&logoColor=white)
![Room](https://img.shields.io/badge/Room-2.8.4-4285F4?logo=android&logoColor=white)
![Navigation 3](https://img.shields.io/badge/Navigation-3-4285F4?logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-36%20(Android%2014)-3DDC84)
![License](https://img.shields.io/badge/License-MIT-green)

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🍎 **Food Tracking** | Log meals with calories, macros (protein, carbs, fat), sugar & fiber |
| 📊 **Custom Charts** | Beautiful line, pie, column & row charts built on Compose Canvas |
| 🎯 **Macro Goals** | Set targets for calories, protein, carbs & fat with sliders |
| 💾 **Local-First** | Room database with migrations — your data stays on your device |
| 📤 **Data Export** | Export your entire database as JSON for backups or analysis |
| 🏃 **Garmin Connect** | WebView integration for syncing activity data |
| 🎨 **Material 3 + Edge-to-Edge** | Modern adaptive UI with dynamic colors |
| 🧭 **Navigation 3** | Type-safe, composable navigation with nested graphs |
| ⚡ **Kotlin 2.3 + KSP** | Modern Kotlin with fast KSP annotation processing |

---

### Key Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| **MVVM + StateFlow** | Reactive UI state |
| **Room + KSP** | Type-safe DB, fast compile-time codegen |
| **Navigation 3** | Composable, type-safe routes with nested graphs |
| **Kotlin Serialization** | Multi-platform JSON for export/import |
| **Min SDK 36** | Edge-to-edge, predictive back, modern APIs only |

---

## 🎨 The Charting Library

Thanks to [ComposeCharts](https://github.com/ehsannarmani/ComposeCharts):

```kotlin
// Line chart with animations, gradients, popups, indicators
LineChart(
    data = listOf(
        Line(
            values = listOf(1200.0, 1500.0, 1800.0, 2100.0, 1900.0),
            color = Color.Blue,
            drawStyle = DrawStyle.Stroke(width = 3.dp),
            gradientProgress = remember { Animatable(0f) },
        )
    ),
    curvedEdges = true,
    animationMode = AnimationMode.OneByOne,
    indicatorProperties = HorizontalIndicatorProperties(count = 5),
    popupProperties = PopupProperties(
        mode = PopupProperties.Mode.CursorMode,
        enabled = true
    ),
    dotsProperties = DotProperties(enabled = true, radius = 6.dp)
)

// Pie chart with animations
PieChart(
    data = listOf(
        PieSlice(value = 120f, color = Color.Red, label = "Protein"),
        PieSlice(value = 80f, color = Color.Blue, label = "Fat"),
        PieSlice(value = 200f, color = Color.Green, label = "Carbs"),
    ),
    holeRadius = 60.dp,
    animationEnabled = true
)
```

**Features:**
- 🎬 Smooth entry animations (stroke, gradient, dot reveal)
- 🎨 Gradients, strokes, fills, custom path effects
- 📍 Interactive popups (tap/drag) with collision avoidance
- 📊 Horizontal/vertical indicators with custom formatters
- 🏷️ Labels, legends, grid lines, zero lines
- 🔄 Multiple animation modes: `Together`, `OneByOne`, `None`
- 📱 Touch gestures: drag for cursor, tap for point popup

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK 17+**
- **Android SDK 36** (Android 14) — *minSdk 36*

### Clone & Build

```bash
git clone https://github.com/yourusername/CaloryTracker.git
cd CaloryTracker
./gradlew assembleDebug
```

### Run on Device/Emulator

```bash
./gradlew installDebug
# or open in Android Studio ▶️ Run
```

---

## ⚙️ Configuration

### Daily Macro Goals (Settings Screen)

| Nutrient | Default | Range |
|----------|---------|-------|
| Calories | 2000 kcal | 800–2500 |
| Protein | 150 g | 10–200 |
| Carbs | 200 g | 10–200 |
| Fat | 70 g | 10–200 |

Goals persist in Room and drive the Home dashboard progress rings.

### Database Export

`Settings → Export DB` → saves a `.txt` (JSON) file via `ACTION_CREATE_DOCUMENT`.

---

## 🧪 Testing

```bash
# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests (requires device/emulator)
./gradlew connectedDebugAndroidTest
```

---

## 📦 Dependencies (Version Catalog)

| Category | Libraries |
|----------|-----------|
| **Compose** | BOM 2024.09.00, Material3, Foundation, UI Graphics, Tooling |
| **Navigation** | Navigation 3 (UI + Runtime), Lifecycle ViewModel Nav3, Adaptive Nav3 |
| **Database** | Room 2.8.4 (Runtime, KTX, Compiler via KSP) |
| **Serialization** | Kotlinx Serialization Core 1.10.0, JSON 1.11.0 |
| **Network** | OkHttp 5.3.0 |
| **Testing** | JUnit 4.13.2, Espresso 3.7.0, Compose UI Test |

---

## 🤝 Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feat/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push: `git push origin feat/amazing-feature`
5. Open a Pull Request

### Code Style
- Follows **Kotlin Coding Conventions** + **Compose Guidelines**
- `ktlint` / `detekt` config welcome (not yet configured — PRs welcome!)

---

## 🗺️ Roadmap

- [ ] **Widget** for home screen macro summary

