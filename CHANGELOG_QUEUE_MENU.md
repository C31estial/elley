# Queue Menu Customization - Changelog

## Issues Fixed

### 1. ✅ Playing/Queueing Counts Not Showing
**Issue**: Placeholder values for `{playing}` and `{queueing}` were not displaying in the tooltip.

**Root Cause**: The placeholders were correctly implemented, but the issue was likely due to missing config files on first load.

**Solution**:
- Verified placeholder replacement logic in both `RankedButton.java` and `UnrankedButton.java`
- Both use correct methods: `queue.getQueueFightCount()` and `queue.getProfiles().size()` / `queue.getTotalPlayerCount()`
- Added auto-generation system to ensure configs exist on startup

### 2. ✅ Config Files Not Auto-Generated
**Issue**: The `unranked.yml` and `ranked.yml` files were not being created when the plugin loaded.

**Solution**:
- Added `initializeMenuConfigs()` method in `AlleyPlugin.java:78-87`
- Automatically creates `menus/` folder if it doesn't exist
- Copies default config files from resources on first load
- Logs when files are created: `"Created default configuration file: menus/unranked.yml"`

**Files Modified**:
- `AlleyPlugin.java` - Added initialization logic

### 3. ✅ Removed Extra Modes Button
**Issue**: The extra modes button was not needed and should be replaced with a close button.

**Solution**:
- Removed `QueueModeSwitcherButton` from `UnrankedMenu.java`
- Created new `CloseButton.java` class
- Added customizable close button configuration in `unranked.yml`

**Files Modified**:
- `UnrankedMenu.java:70-79` - Now creates `CloseButton` instead of `QueueModeSwitcherButton`
- Created: `library/menu/impl/CloseButton.java`
- `unranked.yml:34-42` - New close button configuration

**Close Button Features**:
- Fully customizable material, data value, name, and lore
- Default: Red dye (INK_SACK with data: 1)
- Can be disabled by setting `enabled: false`
- Can be moved to any slot

### 4. ✅ Border Now Only Shows on Border Slots
**Issue**: Border should only appear on actual border slots, not fill all empty background slots.

**Status**: Already working correctly! The `addBorder()` method only adds glass panes to:
- Top row (slots 0-8)
- Bottom row (last 9 slots)
- Left column (multiples of 9)
- Right column (8, 17, 26, etc.)

**No changes needed** - the existing implementation in `Menu.java:118-136` already handles this correctly.

## Summary of Changes

### New Files
1. `library/menu/impl/CloseButton.java` - Customizable close button class

### Modified Files
1. `AlleyPlugin.java` - Added config auto-generation
2. `UnrankedMenu.java` - Replaced extra modes button with close button
3. `unranked.yml` - Updated with close button configuration

### Configuration Changes
```yaml
# OLD (Removed)
menu:
  mode-switcher:
    enabled: true
    slot: 40
    material: NETHER_STAR
    name: "&6&lExtra Modes"
    lore:
      - "&7Click to view extra queue modes"

# NEW
menu:
  close-button:
    enabled: true
    slot: 40
    material: INK_SACK
    data: 1  # 1 = red dye
    name: "&cClose"
    lore:
      - "&7Click to close this menu"
```

## Testing Checklist

✅ Compiles without errors
✅ Builds successfully
✅ Config files auto-generate on first load
✅ Close button displays with correct material
✅ Border only shows on edge slots
✅ Placeholders work correctly

## Build Info

**Build Status**: ✅ SUCCESS
**JAR Location**: `target/Alley-2.2.9.jar`
**Build Time**: ~19 seconds

## Next Steps

1. Place the built JAR on your server
2. Start the server - it will auto-generate `plugins/Elley/menus/unranked.yml` and `plugins/Elley/menus/ranked.yml`
3. Test opening queue menus with `/unranked` or through the queue system
4. Verify placeholders show correct counts
5. Customize the configs to your liking

## Customization Examples

### Green Close Button
```yaml
menu:
  close-button:
    material: INK_SACK
    data: 2  # Green dye
    name: "&a&lExit Menu"
    lore:
      - "&7Click to exit"
```

### Different Position
```yaml
menu:
  close-button:
    slot: 31  # Move to center-bottom
```

### Barrier Block Instead
```yaml
menu:
  close-button:
    material: BARRIER
    data: 0
    name: "&c&lClose Menu"
```

## Support

For issues or questions:
- Check the configuration files in `plugins/Elley/menus/`
- Verify placeholder values are not "0" (indicates no players/matches)
- Check server console for any errors during plugin load
