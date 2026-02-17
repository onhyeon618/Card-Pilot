# Design Audit Skill

The `design-audit` skill helps you maintain a clean and consistent UI by identifying hardcoded design elements (colors and dimensions) in your codebase.

## Features
- **Detects Hardcoded Colors**: Finds hex codes and `Color(...)` usages.
- **Detects Hardcoded Dimensions**: Finds `.dp` and `.sp` usages.
- **Theme Awareness**: Checks if a hardcoded value is already defined in your `ui/theme` directory and suggests the variable name.

## How to Use
Simply ask the agent to run a design audit:
- "Run a design audit on the project."
- "Check for hardcoded colors and dimensions."
- "Are there any design violations in the codebase?"

## Output
The skill will generate a markdown report listing all violations, their locations, and suggested fixes if the value is already defined in your theme.
