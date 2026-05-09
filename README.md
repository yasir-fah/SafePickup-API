# SafePickup

## Overview

SafePickup is a mobile application designed to streamline and secure the student pickup process at schools. The system leverages NFC technology, real-time proximity verification, and multi-factor authentication to ensure that students are only released to their registered, verified guardians.

---

## How It Works

Each student is assigned an NFC tag upon enrollment in the SafePickup system. When a student approaches the school gate, they tap their NFC tag on the reader installed at the exit point. The system then validates the pickup request against the student's associated parent or guardian profile before granting exit approval.

To complete a successful pickup, the following conditions must be satisfied:

- **Proximity Verification:** The registered parent or guardian must be within 1,500 meters of the school at the time of the pickup request, confirming physical presence in the vicinity.
- **Authentication:** The parent or guardian must verify their identity using either biometric authentication or a one-time password (OTP) before exit is approved.

---

## Features

### Security
- Pickup requests require the parent's GPS location to be within 1,500 meters of the school.
- Exit approval is granted only after successful OTP or biometric authentication.

### Congestion Monitoring
- Parents can view real-time traffic and congestion levels near the school via the HERE API, which provides geolocation data based on the school's longitude and latitude. This allows parents to choose the most optimal time for pickup.

### NFC Reader Integration
- The system integrates with the **ACR122U** hardware NFC reader. When a student taps their NFC tag, the reader captures the interaction and transmits the data to the operating system via the `javax.smartcardio` library. This allows the API to extract the NFC tag's UID and process the pickup request accordingly.

## Attendance & Records 
- Parents have access to a complete log of their child's school entry and exit history, providing full visibility into daily attendance through the SafePickup mobile application.

---

## Tech Stack

| Category   | Technologies                     |
|------------|----------------------------------|
| Languages  | TypeScript, Java                 |
| Frameworks | React Native, Spring Boot        |
| Database   | MySQL                            |
| Tools      | Expo Go, Postman, GitHub, Linear |

---
# Team Members

**Supervisor:** Dr. Mohammed Fayez Al-Khatib  
**Group No.** M19

| Member | LinkedIn |
|------------------------|----------|
| Ahmed Abdullah Al-Zaid | [linkedin](https://linkedin.com/in/ahmed-alzaid1) |
| Faisal Abdulrahman Al-Hassoun | [linkedin](https://linkedin.com/in/faisal-alhassoun-763774341) |
| Yasir Fahad Al-Ateeq | [linkedin](https://linkedin.com/in/yasir-alateeq-944188307) |
| Yaser Badr Al-Rashid | [linkedin](https://linkedin.com/in/yaser-al-rashid-4b3a83398) |

