# Design System Master File

> **LOGIC:** When building a specific page, first check `design-system/pages/[page-name].md`.
> If that file exists, its rules **override** this Master file.
> If not, strictly follow the rules below.

---

**Project:** CardPilot
**Updated:** 2026-02-15
**Category:** Fintech/Personal Finance
**Theme:** Clean, Bright, Warm & Gentle

---

## Global Rules

### Color Palette

| Role | Hex | Kotlin Val | Description |
|------|-----|------------|-------------|
| **Background** | `#FFFEFC` | `Background` | Almost white with a tiny warm hint. Clean & Bright. |
| **Primary** | `#FF8A65` | `Primary` | Soft Coral. Friendly, warm brand color. |
| **Text** | `#37474F` | `TextPrimary` | Dark Blue Gray. Softer than black, readable. |
| **Secondary** | `#90A4AE` | `Secondary` | Soft Blue Gray. Muted text and icons. |
| **CTA** | `#FFCC80` | `CTA` | Soft Apricot. Accents and highlights. |
| **Surface Card** | `#FFF8E7` | `SurfaceCard` | Very Light Warm Beige. Used for cards/widgets. |
| **White** | `#FFFFFF` | `White` | Pure white. Used for elevated surfaces (Dropdowns, Rows). |
| **Gray 100** | `#F9FAFB` | `Gray100` | Very Light Cool Gray. Crisp separators. |
| **Gray 200** | `#ECEFF1` | `Gray200` | Light Blue Gray. Input backgrounds. |
| **Gray 300** | `#CFD8DC` | `Gray300` | Muted Blue Gray. Disabled states. |
| **Success** | `#A5D6A7` | `Success` | Pastel Green. |
| **Warning** | `#FFE082` | `Warning` | Pastel Amber. |
| **Error** | `#EF9A9A` | `Error` | Pastel Red. |

**Color Styling:**
- **Warm & Gentle**: Avoid harsh blacks (`#000000`) and sterile pure whites (`#FFFFFF`) for main backgrounds.
- **Soft Contrast**: Use Dark Blue Gray for text to keep it legible but gentle.

### Typography

- **Font Family:** System Default (`FontFamily.Default`) - *IBM Plex Sans planned.*
- **Scale:**
    - **Display/Headline**: Bold/SemiBold weights. Used for big amounts and page titles.
    - **Title**: SemiBold/Medium. Used for card titles and section headers.
    - **Body**: Normal weight. Generous line height for readability.
    - **Label**: Medium weight. Used for buttons and small tags.

### Spacing & Layout

- **Standard Padding:** `24.dp` (Horizontal screen padding)
- **Item Spacing:** `16.dp` - `24.dp` (Relaxed, airy feel)
- **Corner Radius:**
    - Cards/Containers: `12.dp` - `16.dp` (Soft rounded corners)
    - Buttons: `16.dp` (Friendly roundness)
    - Small Elements: `4.dp`

### Shadows (Soft)

- **Elevation:** `4.dp` - `8.dp`
- **Color:** Soft Black (`0x1A000000`)
- **Usage:** Used on `CardListItem` and Card Previews to create depth without heaviness.

---

## Component Specs

### 1. Card Dropdown
- **Style:** Floating White Surface
- **Background:** `#FFFFFF`
- **Shadow:** `4.dp`, Soft
- **Content:** Card Image (Placeholder) + Card Name. Left-aligned.
- **Padding:** Generous internal padding (`16.dp`).

### 2. Card Usage Summary
- **Style:** Warm Surface Widget
- **Background:** `SurfaceCard` (`#FFF8E7`)
- **Shadow:** Mild `2.dp`
- **Content:** Title ("이번 달 사용 금액") + Large Amount. Centered.

### 3. Month Selector
- **Style:** Row with Icon Buttons
- **Layout:** `Arrangement.SpaceBetween`
- **Components:**
    - Left/Right Arrow IconButtons (Simple, no background).
    - Current Month Text (`TitleMedium`).

### 4. Category Detail Screen
- **Header:** `CategoryHeader` with description and visual progress.
- **Add Item Button:**
    - **Style:** Text Button.
    - **Color:** Secondary (Muted).
    - **Icon:** `AddCircle` (16dp).
    - **Text:** "지출 항목 추가" (`LabelLarge`).
    - **Placement:** Aligned to the end (Right) above the transaction list.
- **List:**
    - Transaction Items separated by `HorizontalDivider` (`Gray100`).

### 5. Settings Screen
- **Section (`SettingsSection`):**
    - **Title:** `LabelLarge`, `Secondary` color, consistent padding.
    - **Container:** `SurfaceCard` background, `12.dp` corner radius.
- **Row (`SettingsRow`):**
    - **Label:** `BodyLarge`, `TextPrimary`.
    - **Value (Optional):** `BodyMedium`, `Secondary`.
    - **Icon:** `KeyboardArrowRight`, `Gray300`.
    - **Interactions:** Full-width clickable.

### 6. Card List Screen
- **Item (`CardListItem`):**
    - **Background:** `SurfaceCard`, `16.dp` corner radius.
    - **Shadow:** `4.dp` elevation.
    - **Elements:** Drag Handle (Menu icon), Card Image Placeholder (`Primary` color), Card Name, Arrow Icon.
- **Action:**
    - **FAB:** `Primary` background, `White` icon (`Add`), `CircleShape`.

### 7. Add Card Screen (Premium)
- **Header:** "Card Preview" Input.
- **Background:** `Primary` (Soft Coral) solid.
- **Shadow:** Deep soft shadow (`8.dp`).
- **Text:** White text on colored background.
- **Benefit Inputs:**
    - **Style:** Clean rows using `BenefitInputRow`.
    - **Fields:** Minimal, no outlines. Use `BasicTextField`.

---

## Anti-Patterns (Do NOT Use)

- ❌ **Harsh Black**: Do not use `#000000`. Use `#37474F`.
- ❌ **Sterile White Backgrounds**: Main background should be `#FFFEFC`.
- ❌ **Boxy Borders**: Avoid default Android borders/outlines. Use shadows or color fills.
- ❌ **Dense Layouts**: Maintain "Airy" spacing.

---
