# Design System Master File

> **LOGIC:** When building a specific page, first check `design-system/pages/[page-name].md`.
> If that file exists, its rules **override** this Master file.
> If not, strictly follow the rules below.

---

**Project:** CardPilot
**Updated:** 2026-02-15
**Category:** Fintech/Personal Finance
**Theme:** Warm Glass, Modern, Premium (True Glassmorphism)

---

## Global Rules

### Visual Style: Warm Glass
- **Core Concept:** "True Glass" design with high transparency (40%) over a vibrant, warm mesh gradient background.
- **Vibe:** Friendly, Organic, and Premium. No "cold" greys; use "Warm Stone" tones.
- **Glass Effect:** Relies on **transparency and borders**, NOT shadows. Shadows create "muddy" artifacts behind transparent cards and are BANNED on glass components.

### Color Palette

| Role | Hex | Kotlin Val | Description |

|------|-----|------------|-------------|
| **Background** | `#FAFAF9` | `Background` / `Gray50` | Stone-50. Warm off-white base. |
| **Primary** | `#1C1917` | `Primary` | Stone-900. Warm, deep charcoal. |
| **Secondary** | `#78716C` | `Secondary` | Stone-500. Warm grey for supporting text. |
| **CTA** | `#6366F1` | `CTA` | Indigo-500. Vibrant, friendly purple-blue. |
| **Surface** | `#FFFFFF` | `Surface` | Pure White. Used only for opaque elements (avoid on glass). |
| **Surface Glass** | `#FFFFFF` (Alpha 0.4) | `SurfaceGlass` | **40% opacity** white. Main background for cards. |
| **Text Primary** | `#0C0A09` | `TextPrimary` | Stone-950. High contrast, warm black. |
| **Text Secondary** | `#57534E` | `TextSecondary` | Stone-600. Softer warm grey. |
| **Outline** | `#FFFFFF` (Alpha 0.5) | `Outline` | Semi-transparent white. Simulates light on glass edges. |
| **Success** | `#10B981` | `Success` | Emerald-500. |
| **Warning** | `#F59E0B` | `Warning` | Amber-500. |
| **Error** | `#EF4444` | `Error` | Red-500. |
| **Pastel Violet** | `#E9D5FF` | `PastelViolet` | Violet-200. Soft, airy purple. |
| **Pastel Indigo** | `#C7D2FE` | `PastelIndigo` | Indigo-200. Soft, airy indigo. |
| **Gentle Accent** | `#5B507A` | `SoftSlateIndigo` | Muted Purple/Slate. Used for gentle buttons. |


**Gradient Accents (Warm Mesh):**
- **Background:** `GlassScaffold` uses a mesh gradient of `GradientBlue`, `GradientPurple`, and `GradientPeach`.
- **Intensity:** Minimum alpha 0.5-0.8 for visibility through glass cards.

**Shared Gradients:**
- **Pastel Gradient:** `PastelViolet` -> `PastelIndigo`. Used for card placeholders and generic card backgrounds.


### Typography

- **Font Family:** `IBM Plex Sans` (or System Default `SansSerif`).
- **Scale:**
    - **Display**: Bold/Extrabold. Large amounts.
    - **Headline**: SemiBold. Section headers.
    - **Title**: Medium. Card titles.
    - **Body**: Regular. Content.
    - **Label**: Medium/SemiBold. Buttons.

### Spacing & Layout

- **Standard Padding:** `24.dp` (Horizontal)
- **Item Spacing:** `16.dp` (Components), `24.dp` (Sections)
- **Corner Radius:**
    - **Cards/Glass:** `24.dp` (Modern, super rounded)
    - **Buttons:** `12.dp` - `20.dp`

### Depth & Effects (CRITICAL)

- **True Glass Construction:**
    1.  **Background:** `SurfaceGlass` (40% Alpha White).
    2.  **Border:** `1.dp` `Outline` (50% Alpha White).
    3.  **Shadow:** **NONE**. Do NOT use elevation or `shadow()` modifiers on glass components.
    4.  **Container:** Must be placed on top of `GlassScaffold` to show the gradient underneath.

---

## Component Specs

### 1. Main Navigation / Header
- **Style:** Transparent TopAppBar.
- **Title:** `TitleLarge` or `HeadlineMedium` (`TextPrimary`).
- **Status Bar:** Transparent (via `Theme.kt`).

### 2. Glass Cards (General)
- **Used for:** Dropdowns, Lists, Sections.
- **Construction:**
    ```kotlin
    .background(SurfaceGlass, RoundedCornerShape(24.dp))
    .border(1.dp, Outline, RoundedCornerShape(24.dp))
    // NO SHADOW
    ```

### 2b. Pastel Cards (Placeholders)
- **Used for:** Card Previews, Mockup cards.
- **Background:** `PastelGradient` (`PastelViolet` -> `PastelIndigo`).
- **Text:** Must use **High Contrast Dark** colors (e.g., Violet-800 `#5B21B6`) for readability on pastel.


### 3. Buttons
- **Primary:** `CTA` background, White text.
- **Gentle Primary:** `SoftSlateIndigo` background, White text. For less aggressive actions (e.g., "Save"). **MUST have a shadow** to be distinct.
- **Glass/Outlined:** `SurfaceGlass` background, `Outline` border, `Secondary` text.


### 4. Inputs (TextFields)
- **Style:** Minimal.
- **Background:** `SurfaceGlassHigh` (Low transparency).
- **Border:** `OutlineHigh`.
- **Corner:** `12.dp`.

---

## Anti-Patterns (Do NOT Use)

- ❌ **Drop Shadows on Glass:** Never add `shadow()` to a component with `SurfaceGlass`. It looks dirty.
- ❌ **Opaque Backgrounds:** Do not use `Color.White` for card backgrounds; use `SurfaceGlass`.
- ❌ **Cold Greys:** Avoid `#71717A` (Zinc); use Stone colors (`#78716C`).
- ❌ **Default Scaffolds:** Always use `GlassScaffold` instead of `Scaffold` for main screens.
