# Design System Master File

> **LOGIC:** When building a specific page, first check `design-system/pages/[page-name].md`.
> If that file exists, its rules **override** this Master file.
> If not, strictly follow the rules below.

---

**Project:** CardPilot
**Updated:** 2026-02-22
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
| **Surface Glass** | `#FFFFFF` (Alpha 0.4) | `SurfaceGlass` | **40% opacity** white. Main background for glass cards. |
| **Surface Glass Input** | `#FFFFFF` (Alpha 0.6) | `SurfaceGlassInput` | **60% opacity** white. Background for text inputs and read-only items. |
| **Surface Glass Button** | `#FFFFFF` (Alpha 0.75) | `SurfaceGlassButton` | **75% opacity** white. Background for outlined interactive buttons. |
| **Text Primary** | `#0C0A09` | `TextPrimary` | Stone-950. High contrast, warm black. |
| **Text Secondary** | `#57534E` | `TextSecondary` | Stone-600. Softer warm grey. |
| **Outline** | `#FFFFFF` (Alpha 0.5) | `Outline` | Semi-transparent white. Base glass border. |
| **Outline Input** | `#FFFFFF` (Alpha 0.7) | `OutlineInput` | Semi-transparent white. Border for inputs. |
| **Outline Button** | `#FFFFFF` (Alpha 0.9) | `OutlineButton` | Near-opaque white. Border for interactive buttons. |
| **Success** | `#10B981` | `Success` | Emerald-500. |
| **Warning** | `#F59E0B` | `Warning` | Amber-500. |
| **Error** | `#EF4444` | `Error` | Red-500. |
| **Pastel Violet** | `#E9D5FF` | `PastelViolet` | Violet-200. Soft, airy purple. |
| **Pastel Indigo** | `#C7D2FE` | `PastelIndigo` | Indigo-200. Soft, airy indigo. |
| **Gentle Accent** | `#5B507A` | `SoftSlateIndigo` | Muted Purple/Slate. Used for gentle buttons. |
| **White** | `#FFFFFF` | `White` | Alias for convenience use. |

**Grey Scale (Warm Stone):**

| Role | Hex | Kotlin Val | Description |
|------|-----|------------|-------------|
| **Gray50** | `#FAFAF9` | `Gray50` | Stone-50. Same as Background. |
| **Gray100** | `#F5F5F4` | `Gray100` | Stone-100. Used for dividers in settings. |
| **Gray200** | `#E7E5E4` | `Gray200` | Stone-200. Progress bar tracks, dividers, placeholder text. |
| **Gray300** | `#D6D3D1` | `Gray300` | Stone-300. Muted icons (e.g., settings arrows). |

**High Contrast for Pastel Backgrounds:**

| Role | Hex | Kotlin Val | Description |
|------|-----|------------|-------------|
| **Violet-800** | `#5B21B6` | `Violet800` | Dark violet for text on pastel backgrounds. |
| **Violet-900** | `#4C1D95` | `Violet900` | Deepest violet for maximum contrast on pastel. |


**Gradient Accents (Warm Mesh):**
- **Background:** `GlassScaffold` uses a mesh gradient of `GradientBlue`, `GradientPurple`, and `GradientPeach`.
- **Intensity:** Minimum alpha 0.5-0.8 for visibility through glass cards.

**Shared Gradients:**
- **Pastel Gradient:** `PastelViolet` -> `PastelIndigo`. Used for card placeholders and generic card backgrounds.


### Typography

- **Font Family:** `FontFamily.Default` (System SansSerif). Ideally `IBM Plex Sans`.
- **Scale:**
    - **Display**: Bold. Large headline amounts (e.g., usage totals).
    - **Headline**: SemiBold. Section headers.
    - **Title**: SemiBold/Medium. Card titles, nav bar titles.
    - **Body**: Regular. Content text.
    - **Label**: Medium/SemiBold. Buttons, input labels, small metadata.

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

### 1. GlassScaffold
- **Role:** Main scaffold wrapper for all screens. Replaces standard `Scaffold`.
- **Construction:** Base `Background` color + warm mesh vertical gradient overlay + transparent inner `Scaffold`.
- **Status Bar:** Drawn as an explicit overlay box using `lerp(White, GradientBlue, 0.8f)` to prevent content from showing underneath.
- **Nav Bar:** Transparent.

### 2. Glass Cards (General)
- **Used for:** Dropdowns, Lists, Sections, Usage Summaries.
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

### 3. CardPilotRipple
- **Role:** Wrapper composable that applies a custom ripple color to all clickable children.
- **Default Color:** `PastelViolet`.
- **Alt Color:** `GradientPeach` – used for warm-toned interactive items (benefit rows, input items).
- **Usage:** Wrap any `clickable` element to apply consistent themed ripple feedback.

### 4. Buttons
- **Primary CTA:** `CTA` background, White text.
- **Gentle Primary:** `SoftSlateIndigo` background, White text. For less aggressive actions (e.g., "Save"). **MUST have a shadow** to be distinct.
- **Glass/Outlined:** `SurfaceGlassButton` background, `OutlineButton` border, `TextPrimary` content.

### 5. Inputs

#### 5a. InputTextField (Editable)
- **Role:** Text input with label, optional icon, and placeholder.
- **Background:** `SurfaceGlassInput` (60% Alpha).
- **Border:** `1.dp` `OutlineInput` (70% Alpha).
- **Corner:** `12.dp`.
- **Height:** `56.dp`.
- **Placeholder Color:** `Gray200`.

#### 5b. InputItem (Read-only)
- **Role:** Tappable read-only display with label, optional icon, and value.
- **Background:** `SurfaceGlassInput`.
- **Border:** `1.dp` `OutlineInput`.
- **Corner:** `12.dp`.
- **Height:** `56.dp`.
- **Ripple:** `GradientPeach`.

### 6. CardDropdown
- **Role:** Card selector with animated expand/collapse arrow.
- **Glass Construction:** Standard glass card (`SurfaceGlass`, `Outline`, `24.dp` radius).
- **Dropdown Menu:** `lerp(PastelViolet, White, 0.8f)` background, `16.dp` radius, `Outline` border.
- **Includes:** Pastel card thumbnail, card name, animated rotating arrow icon.

### 7. CardListItem
- **Role:** Card entry in the card management list.
- **Glass Construction:** Standard glass card (`SurfaceGlass`, `Outline`, `24.dp` radius).
- **Contains:** Drag handle icon, pastel card thumbnail, card name, right arrow.

### 8. BenefitItem
- **Role:** Benefit row in the home screen tracker.
- **Style:** No card background – uses padding and dividers for separation.
- **Contains:** Benefit name, usage fraction text, optional explanation, progress bar.
- **Progress Bar:** `CTA` color on `Gray200` track, `8.dp` height, `CircleShape` clip.
- **Ripple:** `GradientPeach`.

### 9. BenefitItemRow
- **Role:** Editable benefit entry used in AddCardScreen's benefit list.
- **Style:** `OutlinedButton` with `SurfaceGlassButton` background, `OutlineButton` border, `12.dp` radius.
- **Contains:** Drag handle, name, optional description, delete icon (`DeleteThin`).
- **Delete Icon:** `Error` tint.

### 10. BenefitDetailHeader
- **Role:** Usage summary card at the top of BenefitDetailScreen.
- **Glass Construction:** Standard glass card (`SurfaceGlass`, `Outline`, `24.dp` radius).
- **Contains:** Optional description, large progress bar (`12.dp` height), usage fraction, remaining limit text.

### 11. CardUsageSummary
- **Role:** Monthly usage total display on HomeScreen.
- **Glass Construction:** Standard glass card (`SurfaceGlass`, `Outline`, `24.dp` radius).
- **Contains:** Label (`titleMedium`, `Secondary`), amount (`displaySmall`, `TextPrimary`).

### 12. MonthSelector
- **Role:** Pill-shaped month navigation with left/right arrows.
- **Shape:** `RoundedCornerShape(50)` (full capsule).
- **Glass Construction:** `SurfaceGlass` + `Outline` border via `Surface` composable.
- **Disabled Arrow Tint:** `Secondary` at `alpha = 0.3f`.

### 13. TransactionItem
- **Role:** Individual transaction row in benefit detail lists.
- **Style:** No card background – uses padding and dividers.
- **Contains:** Date/time column, merchant name, amount with optional eligible sub-text.

### 14. SettingsSection
- **Role:** Grouped settings category with title and glass card container.
- **Title:** `labelLarge`, `Secondary` color.
- **Glass Construction:** Standard glass card (`SurfaceGlass`, `Outline`, `24.dp` radius).
- **Inner Dividers:** `Gray100`, `1.dp` thickness.

### 15. SettingsRow
- **Role:** Individual settings menu item within a `SettingsSection`.
- **Contains:** Label (`bodyLarge`), optional value (`bodyMedium`, `Secondary`), optional arrow icon (`Gray300`).
- **Ripple:** Default (`PastelViolet`).

### 16. EdgeToEdgeColumn
- **Role:** Layout utility that applies scaffold padding (top/horizontal) while auto-adding bottom spacer for navigation bar clearance.
- **Usage:** Use inside `GlassScaffold` content blocks for proper edge-to-edge layout.

### 17. CardsIcons (DeleteThin)
- **Role:** Custom thin delete icon (Material Symbols ~300 weight).
- **Usage:** Used in `BenefitItemRow` for delete actions.

---

## Navigation / Header

- **TopAppBar:** `CenterAlignedTopAppBar` with `Color.Transparent` container.
- **Title:** `TitleLarge` (`TextPrimary`).
- **Nav Icon Color:** `TextPrimary`.
- **Back Icon:** `Icons.AutoMirrored.Filled.ArrowBack`.

---

## Anti-Patterns (Do NOT Use)

- ❌ **Drop Shadows on Glass:** Never add `shadow()` to a component with `SurfaceGlass`. It looks dirty.
- ❌ **Opaque Backgrounds:** Do not use `Color.White` for card backgrounds; use `SurfaceGlass`.
- ❌ **Cold Greys:** Avoid `#71717A` (Zinc); use Stone colors (`#78716C`).
- ❌ **Default Scaffolds:** Always use `GlassScaffold` instead of `Scaffold` for main screens.
