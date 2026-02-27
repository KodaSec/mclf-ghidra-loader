# mclf-ghidra-loader

[![GitHub Release](https://img.shields.io/github/v/release/KodaSec/mclf-ghidra-loader)](https://github.com/KodaSec/mclf-ghidra-loader/releases)
[![GitHub Downloads (all assets, latest release)](https://img.shields.io/github/downloads/KodaSec/mclf-ghidra-loader/latest/total)](https://github.com/KodaSec/mclf-ghidra-loader/releases)

mclf-ghidra-loader is a Ghidra extension that lets you load and analyze MobiCore Loadable Format (MCLF) binaries.

## Installing mclf-ghidra-loader

### Requirements

| Tool | Version | Source |
|---|---|---|
| Ghidra | `>= 12.0` | https://github.com/NationalSecurityAgency/ghidra/releases |
| Java | `>= 21.0.0` | https://adoptium.net/temurin/releases |

Use the following steps to install mclf-ghidra-loader to your Ghidra environment:

1. Download the latest mclf-ghidra-loader [release](https://github.com/KodaSec/mclf-ghidra-loader/releases)
2. Install the extension (`.zip`) into Ghidra:
   * Navigate to `File > Install Extensions...`
   * Click the green `+` button
   * Navigate to the mclf-ghidra-loader extension (`.zip`)
   * Click `Ok`
3. Restart Ghidra

## Using mclf-ghidra-loader

Open an MCLF binary with `File > Import File...` and Ghidra will automatically recognize the format.

## Building from source

```
$ export GHIDRA_INSTALL_DIR=<absolute_path_to_ghidra_install_dir>
$ $GHIDRA_INSTALL_DIR/support/gradle/gradlew buildExtension
```

The built extension ZIP will be in `dist/`.
