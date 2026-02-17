---
name: design-audit
description: Scans the codebase for hardcoded design elements (colors, dimensions) and compares them against theme definitions to suggest replacements.
---

# design-audit

## Purpose
To identify hardcoded design values (colors, dimensions) in the codebase that violate the design system. It helps in refactoring by suggesting replacements from the defined theme.

## When to Use This Skill
- When auditing the codebase for design inconsistencies.
- When refactoring UI components to use the centralized theme.
- When checking for hardcoded values before finalizing a feature.

## Core Capabilities
1. **Theme Definition Extraction**: Parses `ui/theme` files to understand available design tokens.
2. **Hardcoded Value Detection**: Scans UI code for raw hex colors and numeric dimensions.
3. **Replacement Suggestion**: Matches hardcoded values with theme definitions.

## Instructions

### Step 1: Extract Definitions (Theme Directory)
1. Scan the `ui/theme` directory. Look for files like `Color.kt`, `Theme.kt`, `Type.kt`, and `Dimens.kt` (if exists).
2. Extract all named definitions for:
    - **Colors**: Look for `val (Name) = Color(0x...)` or similar patterns.
    - **Dimensions**: Look for `val (Name) = ...dp` or `...sp`.
3. Create a **Theme Map** where `Key = Raw Value` and `Value = Variable Name`.
    - Example: `{ "0xFF6650a4": "Purple40", "16.dp": "PaddingMedium" }`

### Step 2: Scan Codebase (Non-Theme Directories)
1. Identify all Kotlin files in the project, EXCLUDING the `ui/theme` directory.
2. For each file, read the content and search for:
    - **Hardcoded Colors**: 
        - Patterns: `Color(0x...)`, `Color(R.color...)` (if applicable), `#RRGGBB` strings if used in relevant contexts.
        - *Ignore* simplistic integers like `0`, `1` unless clearly used as a color.
    - **Hardcoded Dimensions**:
        - Patterns: `123.dp`, `123.sp`.
        - *Ignore* `0.dp` (often valid for no padding).

### Step 3: Analyze and Report
For each found hardcoded value:
1. **Check against Theme Map**:
    - If the raw value exists in the Theme Map, mark it as **"Defined in Theme"**.
    - If not, mark it as **"New Hardcoded Value"**.
2. **Generate Report**:
    - Output a summary using the following format:

    ```markdown
    # Design Audit Report
    
    ## 🎨 Hardcoded Colors
    
    | Raw Value | Defined in Theme? | Location |
    | :--- | :--- | :--- |
    | `0xFF0000FF` | ❌ | `MainActivity.kt:42` |
    | `0xFF6650a4` | ✅ (`Purple40`) | `ProfileScreen.kt:15` |
    
    ## 📏 Hardcoded Dimensions
    
    | Raw Value | Defined in Theme? | Location |
    | :--- | :--- | :--- |
    | `16.dp` | ✅ (`PaddingMedium`) | `MainActivity.kt:50` |
    | `250.dp` | ❌ | `HeroImage.kt:20` |
    ```

3. **Provide Recommendations**:
    - If many "New Hardcoded Values" are the same, suggest adding them to the theme.
    - If "Defined in Theme" values are found, suggest replacing them with the theme variable.

## Execution
Run the audit by executing the steps above on the user's codebase.
