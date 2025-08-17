# ![2fa Icon](./assets/icon.svg) 2fa

2fa by 979 is a free, open-source one-time-password manager published under the European Union Public License (EUPL). Designed for privacy and anonymity, it contains no networking code, uses minimal dependencies, and collects no telemetry — everything stays on your device. Development is community-driven and fully auditable. The app is developed and tested primarily on GrapheneOS with Google Pixel devices, while remaining compatible with other Android phones such as Samsung.

## Features
- Full support for Time-based one-time password (TOTP) and HMAC-based one-time password (HOTP)
  - Configurable intervals starting from 10 seconds
  - Support for 4–10 digit codes
  - Hash algorithms: SHA-1, SHA-256, and SHA-512

## Installation

- **Google Play Store (Internal Testing):** To join, send us your Play Store email via **DM on [Discord](https://discord.com/invite/zxgXNzhYJu)**
- **Direct APK:** Download the latest release from [Releases](https://github.com/979st/2fa-android/releases) and verify using AppVerifier or apksigner

## Development Roadmap
- **Alpha:** Adding essential features and integrating user suggestions
- **Beta:** Refining UI, improving usability, adding animations, and launching on [Accrescent](https://accrescent.app/)
- **Release Candidate:** Preparing for a full launch on Google Play Store

## Verifying APKs
All official release APKs are signed with our release key. Fingerprints of the signing certificate:

- **SHA-256:** `e5b28ea1e051c232dea94c27c0592e2c002d17c5ca81788b51306d608afe52c5`
- **SHA-1:** `4cb3772c2db89e65fbe5fa23ba33e3eee9b43421`
- **MD5:** `98d63ad9da56d57892207e53c7c13ea1`

We recommend verifying downloads using either [AppVerifier](https://github.com/soupslurpr/AppVerifier) or Android's official [apksigner](https://developer.android.com/tools/apksigner) tool.

## 2fa Alpha 2 Demo
![2fa Alpha 2 Demo](assets/vc-2.gif)
