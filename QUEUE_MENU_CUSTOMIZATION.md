# Queue Menu Customization Guide

The queue menus (Unranked and Ranked) are now fully customizable through YAML configuration files.

## Configuration Files

- **Unranked Queue**: `plugins/Elley/menus/unranked.yml`
- **Ranked Queue**: `plugins/Elley/menus/ranked.yml`

## What You Can Customize

### Menu Settings

#### Title
```yaml
menu:
  title: "&6&lUnranked Queue"  # Change the menu title
```

#### Size
```yaml
menu:
  size: 45  # Must be multiple of 9 (9, 18, 27, 36, 45, 54)
```

#### Border
```yaml
menu:
  border:
    enabled: true
    material: STAINED_GLASS_PANE
    data: 15  # 15 = black, 14 = red, etc.
    name: " "
```

#### Back Button (Unranked only)
```yaml
menu:
  back-button:
    enabled: true
    slot: 0
    material: ARROW
    name: "&cBack"
    lore:
      - "&7Click to go back"
```

#### Close Button (Unranked only)
```yaml
menu:
  close-button:
    enabled: true
    slot: 40
    material: INK_SACK
    data: 1  # 1 = red dye, 2 = green, 4 = yellow, etc.
    name: "&cClose"
    lore:
      - "&7Click to close this menu"
```

### Queue Button Settings

#### Starting Position
```yaml
queue-button:
  start-slot: 10  # Where queue items start appearing
  skip-border-slots: true  # Skip slots at row edges
```

#### Lore Customization

##### Separator
```yaml
queue-button:
  lore:
    separator: "&6&m--------------------"  # Top and bottom line
```

##### Lore Lines
Each lore line can be customized with:
- `text`: The text to display (with placeholders)
- `enabled`: true/false to show/hide the line
- `show-if-empty`: Only show if placeholder has value

**Example:**
```yaml
lines:
  - text: "&7{kit-description}"
    enabled: true
    show-if-empty: false  # Hide if kit has no description

  - text: ""  # Empty line
    enabled: true

  - text: "&6│ &rPlaying: &6{playing}"
    enabled: true

  - text: "&6│ &rQueueing: &6{queueing}"
    enabled: true
```

## Available Placeholders

### Common Placeholders (Both Menus)
- `{kit-name}` - Name of the kit
- `{kit-description}` - Description of the kit
- `{playing}` - Number of players currently playing
- `{queueing}` - Number of players in queue
- `{player}` - Player's name

### Ranked-Specific Placeholders
- `{player-elo}` - Player's ELO for this kit
- `{top1-name}` - Top player #1 name (placeholder)
- `{top1-elo}` - Top player #1 ELO (placeholder)
- `{top2-name}`, `{top2-elo}` - Top player #2
- `{top3-name}`, `{top3-elo}` - Top player #3

### Unranked-Specific Placeholders
- `{daily-streak}` - Player's daily streak (placeholder)
- `{top1-name}` - Top player #1 name (placeholder)
- `{top1-value}` - Top player #1 value (placeholder)
- `{top2-name}`, `{top2-value}` - Top player #2
- `{top3-name}`, `{top3-value}` - Top player #3

## Color Codes

Use `&` for color codes:
- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White

### Formatting Codes
- `&l` - Bold
- `&m` - Strikethrough
- `&n` - Underline
- `&o` - Italic
- `&r` - Reset

## Example Customizations

### Simple Clean Look
```yaml
queue-button:
  lore:
    separator: "&7&m---------------------"
    lines:
      - text: "&7{kit-description}"
        enabled: true
        show-if-empty: false

      - text: ""
        enabled: true

      - text: "&7Playing: &f{playing}"
        enabled: true

      - text: "&7In Queue: &f{queueing}"
        enabled: true

      - text: ""
        enabled: true

      - text: "&aClick to join!"
        enabled: true
```

### Detailed Info Look
```yaml
queue-button:
  lore:
    separator: "&6&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"
    lines:
      - text: "&e&l{kit-name}"
        enabled: true

      - text: "&7{kit-description}"
        enabled: true
        show-if-empty: false

      - text: ""
        enabled: true

      - text: "&6◆ &fCurrently Playing: &e{playing}"
        enabled: true

      - text: "&6◆ &fWaiting in Queue: &e{queueing}"
        enabled: true

      - text: ""
        enabled: true

      - text: "&a&l► Click to Join Queue"
        enabled: true
```

### Minimal Look
```yaml
queue-button:
  lore:
    separator: ""  # No separator
    lines:
      - text: "&7{playing} playing · {queueing} in queue"
        enabled: true

      - text: ""
        enabled: true

      - text: "&aClick to play"
        enabled: true
```

## Tips

1. **Reload Config**: After making changes, reload the plugin or restart the server
2. **Test Changes**: Open the queue menu in-game to see your changes
3. **Color Combinations**: Use `&l` with colors for bold text (e.g., `&6&lBold Gold`)
4. **Empty Lines**: Use `- text: ""` for spacing between sections
5. **Disable Lines**: Set `enabled: false` to hide any line without deleting it

## Files Modified

The following files were updated to support configuration:
- `RankedMenu.java` - Reads from `ranked.yml`
- `UnrankedMenu.java` - Reads from `unranked.yml`
- `RankedButton.java` - Lore from config
- `UnrankedButton.java` - Lore from config
- `QueuesMenuModern.java` - Uses unranked config
- `ExtraModesMenu.java` - Uses unranked config
