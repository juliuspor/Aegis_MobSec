<img align="left" width="80" height="80" src="metadata/en-US/images/icon.png" alt="App icon">

# Amalgis - Research Malware (Based on Aegis)

<br>

[![Build](https://github.com/beemdevelopment/Aegis/actions/workflows/build-app-workflow.yaml/badge.svg)](https://github.com/beemdevelopment/Aegis/actions/workflows/build-app-workflow.yaml?query=branch%3Amaster) [![Research Project](https://img.shields.io/badge/status-research%20only-red)]()

**Amalgis** is a research-focused fork of Aegis Authenticator designed to demonstrate security vulnerabilities in 2FA applications. This project was created for educational purposes on campus to study malware techniques and security weaknesses. **DO NOT USE THIS IN PRODUCTION OR FOR PERSONAL AUTHENTICATION.**

## Research Purpose

This project demonstrates potential security vulnerabilities in authenticator applications by implementing controlled malware features in a sandboxed environment. The goal is to educate security professionals about potential attack vectors and improve defensive measures.

## User Scenario Research

Our research examines how users might be tricked into adopting malicious authenticator apps:

- **Target Profile**: Freelance professionals (e.g., graphic designers) who frequently use online services requiring two-factor authentication
- **Discovery Vector**: Targeted advertising emphasizing user-friendly recovery options
- **Motivation for Adoption**: User frustration with current authenticator limitations and appealing new features
- **Conversion Point**: Promise of better user experience leading to app installation

## Implemented Malware Features (For Research Only)

### Feature #1: Data Exfiltration via Export Functionality
- **Malware Type**: Data exfiltration
- **Mechanism**: Exploits the legitimate export functionality
- **Technique**: Disables the encryption process during export
- **Concealment**: Actions hidden within legitimate processes
- **Exfiltration**: Sends unencrypted authentication data to a designated server

### Feature #2: Screenshot Surveillance
- **Malware Type**: Data exfiltration via screen capture
- **Mechanism**: Takes screenshots every 10 seconds
- **Transmission Technique**: Segments screenshots into multiple chunks
- **Protocol**: Uses UDP for transmission
- **Assembly**: Designated remote host reassembles the fragments

## Original Aegis Features (Baseline Application)

- Free and open source
- Secure
  - The vault is encrypted (AES-256-GCM), and can be unlocked with:
    - Password (scrypt)
    - Biometrics (Android Keystore)
  - Screen capture prevention
  - Tap to reveal
- Compatible with Google Authenticator
- Supports industry standard algorithms:
  [HOTP](https://tools.ietf.org/html/rfc4226) and
  [TOTP](https://tools.ietf.org/html/rfc6238)
- Lots of ways to add new entries
  - Scan a QR code or an image of one
  - Enter details manually
  - Import from other authenticator apps
- Organization features
  - Alphabetic/custom sorting
  - Custom or automatically generated icons
  - Group entries together
  - Advanced entry editing
  - Search by name/issuer
- Material design with multiple themes: Light, Dark, AMOLED
- Export (plaintext or encrypted)
- Automatic backups of the vault to a location of your choosing

## Research Disclaimer

**IMPORTANT**: This software is developed SOLELY for security research and educational purposes in a controlled environment. It contains intentional security vulnerabilities and malicious features.

- DO NOT use this application for personal authentication
- DO NOT distribute outside of the research environment
- DO NOT install on production devices
- DO NOT use to compromise real user data

This project is intended to demonstrate how malicious actors might exploit trusted authentication tools, helping security professionals develop better detection and defense mechanisms.

## Research Team

This project had been developed as part of a campus research initiative on mobile security vulnerabilities. 

## License

The original Aegis project is licensed under the GNU General Public License v3.0. This research fork maintains the same license for the base code. See the [LICENSE](LICENSE) file for details.
