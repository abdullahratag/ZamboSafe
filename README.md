# ZamboSafe

A Zamboanga City emergency service directory for Android. Single screen, fully offline:
a searchable, filterable list of agencies with one-tap **Call**, **Facebook** and **Website** buttons.

- Kotlin, XML views (RecyclerView + Material 3), no backend
- `minSdk 24`, `targetSdk 36`, Gradle 9.5.0 / AGP 9.3.2 (same as the course projects)
- Package: `com.example.zambosafe`

> ⚠️ **All data in `agencies.json` is PLACEHOLDER.** Numbers are `0000-000-000X` and links point to
> `example.com` / `facebook.com/PLACEHOLDER`. Replace with verified data before any real use.

## Getting started
1. Clone the repo and open the folder in Android Studio.
2. Let Gradle sync, then run on an emulator or device.

## Where do I edit what?

| I want to change…                                  | Edit this file                                                  |
|----------------------------------------------------|-----------------------------------------------------------------|
| Agencies, numbers, links, categories               | `app/src/main/assets/agencies.json`                             |
| Colors (light + dark)                              | `res/values/colors.xml`, `res/values-night/colors.xml`          |
| Font sizes, card/button look                       | `res/values/styles.xml`                                         |
| Spacing, corner radius                             | `res/values/dimens.xml`                                         |
| App text (title, hints, button labels)             | `res/values/strings.xml`                                        |
| Screen layout (header, search, chips, list)        | `res/layout/activity_main.xml`                                  |
| Look of one agency card                            | `res/layout/item_agency.xml`                                    |
| Category chip look                                 | `res/layout/item_category_chip.xml`                             |
| Search rule (which fields are searched)            | `model/Agency.kt` → `matches()`                                 |
| Screen logic (filtering, chips)                    | `MainActivity.kt`                                               |
| How a card is filled in / button visibility        | `ui/AgencyAdapter.kt`                                           |
| Call / Facebook / Website intents                  | `util/ActionLauncher.kt`                                        |
| JSON loading / adding a new data field             | `data/AgencyRepository.kt` + `model/Agency.kt`                  |

Suggested split (from the SRS): **UI/Design** → `res/` · **Functionality** → `*.kt` ·
**Data & QA** → `agencies.json` and `src/test`.

## Project layout
```
app/src/main/
├── assets/agencies.json          ← the data
├── java/com/example/zambosafe/
│   ├── MainActivity.kt           ← the one screen
│   ├── model/Agency.kt           ← data class + search rule
│   ├── data/AgencyRepository.kt  ← JSON → List<Agency>
│   ├── ui/AgencyAdapter.kt       ← RecyclerView adapter (card binding)
│   └── util/ActionLauncher.kt    ← dial / open-URL intents
└── res/                          ← layouts, colors, styles, strings, dimens
```

## Data format (`agencies.json`)
A JSON array. Each entry:

```json
{
  "id": "police-1",            // required, must be UNIQUE
  "name": "Agency name",       // required
  "category": "Police",        // required; new categories get a filter chip automatically
  "location": "Tetuan",        // optional
  "phone": "0000-000-0000",    // optional → Call button hidden if missing/null/""
  "facebook": "https://...",   // optional → Facebook button hidden if missing
  "website": "https://..."     // optional → Website button hidden if missing
}
```
Chip order follows the order categories first appear in the file.

## Design
Minimal grayscale palette:

| Role      | Light              | Dark               | Used for                              |
|-----------|--------------------|--------------------|---------------------------------------|
| Primary   | `#EEEEEE`          | `#111111`          | Background, card surface              |
| Secondary | `#828282`          | `#828282`          | Borders, hints, category label        |
| Tertiary  | `#111111`          | `#EEEEEE`          | Text, filled buttons, selected chip   |

The dark-mode Secondary was not specified, so it reuses the light value. Change it in `values-night/colors.xml`
if the team prefers another gray. Secondary on the light background has lower contrast (~3:1), so it is used
only for borders and small labels, not main text.

## Notes / TODO
- Launcher icon is a simple placeholder plus sign (adaptive icon). Legacy `mipmap-*` WebP icons (API 24–25)
  are still the Android Studio default, so replace them via *New → Image Asset* when the logo exists.
- No permissions are needed: Call opens the dialer (`ACTION_DIAL`), links open via `ACTION_VIEW`.
- Possible next steps: email buttons, detail screen (explicit intent), favorites.
