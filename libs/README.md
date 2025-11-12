# Library Dependencies

## FastAsyncWorldEdit

This project requires FastAsyncWorldEdit (FAWE) for 1.8 as a compile-time dependency.

### Setup Instructions:

1. Download FAWE for 1.8 (version 3.5.1 or compatible) from:
   - https://github.com/IntellectualSites/FastAsyncWorldEdit-1.8/releases
   - Or your FAWE distribution source

2. Rename the jar file to `FastAsyncWorldEdit.jar`

3. Place it in this `libs/` directory

The file should be located at: `libs/FastAsyncWorldEdit.jar`

### Alternative: Skip FAWE Compilation Check

If you just want to compile without FAWE available locally (and will install FAWE on your server instead), you can temporarily comment out the FAWE dependency in `pom.xml`. However, this will cause compilation errors in schematic-related code.

### Note

FAWE must be installed on your Minecraft server for this plugin to work properly. The plugin requires FAWE to load and paste arena schematics.
