# 🛡️ UdaSecurity — Intelligent Home Security Monitoring System

<div align="center">

![Java](https://img.shields.io/badge/Java-11-orange?style=for-the-badge)
![Spring](https://img.shields.io/badge/SpringBoot-Security-green?style=for-the-badge)
![JUnit5](https://img.shields.io/badge/JUnit5-Testing-success?style=for-the-badge)
![Mockito](https://img.shields.io/badge/Mockito-Mocking-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge)

An intelligent home security monitoring application designed using Java, modular Maven architecture, automated testing, and image-based threat detection.

</div>

---

# 📌 Project Overview

UdaSecurity is a smart home security application that monitors sensor activity, processes camera image analysis, and manages alarm state transitions based on real-time system conditions.

The application simulates the behavior of a modern home security platform by integrating:

- motion sensors
- alarm systems
- camera image recognition
- home/away arming modes
- automated alarm triggering logic

This project focuses heavily on:
- scalable software architecture
- modular design
- automated testing
- service abstraction
- Maven build management
- code quality analysis

---

# 🎯 Core Objectives

The major goals of this project were:

✅ Convert project into a modular Maven architecture  
✅ Separate Image Service into reusable independent module  
✅ Improve maintainability using interfaces and dependency injection  
✅ Write comprehensive unit tests for security workflows  
✅ Refactor codebase for testability  
✅ Achieve high code coverage  
✅ Build executable JAR files  
✅ Add static analysis and automated quality checks  

---

# 🏗️ Project Architecture

The application follows a modular layered architecture.

```text
                 ┌────────────────────┐
                 │      GUI Layer     │
                 └─────────┬──────────┘
                           │
                 ┌─────────▼──────────┐
                 │  Security Service  │
                 └─────────┬──────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                                   │
┌────────▼────────┐               ┌──────────▼─────────┐
│ Security Module │               │  Image Module      │
└────────┬────────┘               └──────────┬─────────┘
         │                                   │
┌────────▼────────┐               ┌──────────▼─────────┐
│ Repository Layer│               │ AWS/Fake Services  │
└─────────────────┘               └────────────────────┘
```

---

# 📂 Multi-Module Project Structure

```text
UdaSecurity
│
├── pom.xml
│
├── image-service
│   ├── pom.xml
│   └── src/main/java
│       └── com.udacity.catpoint.service
│
├── security-service
│   ├── pom.xml
│   └── src/main/java
│       └── com.udacity.catpoint
│
└── target
```

---

# 🧠 Architecture Explanation

# 1️⃣ Security Module

The security module contains the core business logic of the application.

### Responsibilities

- alarm state transitions
- sensor monitoring
- arming/disarming system
- image analysis integration
- business rule enforcement

### Main Components

- `SecurityService`
- `Sensor`
- `AlarmStatus`
- `ArmingStatus`
- `SecurityRepository`

---

# 2️⃣ Image Service Module

The image module was separated into its own reusable Maven module.

### Responsibilities

- image analysis
- cat detection
- image recognition abstraction

### Main Components

- `ImageService`
- `AwsImageService`
- `FakeImageService`

### Benefits of Modularization

- reusable across projects
- easier maintenance
- cleaner dependency management
- improved scalability

---

# 3️⃣ Repository Layer

Handles application state persistence.

### Responsibilities

- sensor storage
- alarm status management
- camera image state
- arming state persistence

---

# ⚙️ Technologies Used

| Technology | Purpose |
| --- | --- |
| Java 11 | Core programming language |
| Maven | Build & dependency management |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| SpotBugs | Static code analysis |
| AWS Rekognition | Image recognition |
| Swing GUI | Desktop interface |
| IntelliJ IDEA | Development environment |

---

# 🚀 Major Features

# 🔐 Security Monitoring

- Sensor activity tracking
- Alarm state management
- Pending alarm handling
- Active alarm triggering
- Home/Away arming support

---

# 🐱 Smart Camera Detection

- Detect cats using image analysis
- Trigger alarms automatically
- Process camera inputs
- Integrate image recognition services

---

# 🧪 Automated Testing

- Unit testing using JUnit 5
- Dependency mocking using Mockito
- Branch condition testing
- Parameterized testing
- Security workflow validation

---

# 📦 Maven Modernization

Implemented:
- dependency management
- plugin management
- modular builds
- executable JAR creation
- automated testing lifecycle

---

# 🧩 Core Application Logic

The application follows a state-driven security workflow.

---

# Alarm State Transitions

| Current State | Trigger | Result |
| --- | --- | --- |
| NO_ALARM | Sensor Activated | PENDING_ALARM |
| PENDING_ALARM | Another Activation | ALARM |
| PENDING_ALARM | All Sensors Inactive | NO_ALARM |
| ALARM | Sensor Change | ALARM |

---

# Camera Detection Logic

| Condition | Result |
| --- | --- |
| Cat detected + ARMED_HOME | ALARM |
| No cat + sensors inactive | NO_ALARM |
| System disarmed | NO_ALARM |

---

# 🧪 Unit Testing Coverage

The project includes extensive unit tests for all critical requirements.

### Tested Components

- sensor activation
- pending alarm logic
- alarm transitions
- cat detection behavior
- arming/disarming workflows
- inactive sensor handling
- camera processing logic

---

# 🧠 Testing Techniques Used

- Mock-based testing
- Dependency isolation
- Parameterized tests
- Branch coverage
- Integration-style validation

---

# 📈 Code Quality Improvements

Several improvements were implemented beyond the starter architecture.

## 🔹 Refactoring Improvements

- modularized architecture
- interface abstraction
- dependency injection
- cleaner service separation
- reusable image services

---

## 🔹 Testing Improvements

- fully isolated service tests
- mocked repositories
- mocked image services
- reduced GUI dependency
- high functional coverage

---

## 🔹 Maven Improvements

Added:
- plugin management
- compiler plugin configuration
- executable JAR support
- static analysis plugins
- dependency centralization

---

# 🔍 Static Code Analysis

Integrated:

## SpotBugs Maven Plugin

Used for:
- identifying potential bugs
- detecting risky patterns
- improving code safety
- maintaining code quality

Generated reports located in:

```text
target/site/spotbugs.html
```

---

# 📦 Executable JAR Support

The application can be packaged into an executable JAR.

Build command:

```bash
mvn clean package
```

Run command:

```bash
java -jar target/security-service.jar
```

---

# ▶️ Running the Application

# 1️⃣ Clone Repository

```bash
git clone <repository-url>
```

---

# 2️⃣ Navigate Into Project

```bash
cd UdaSecurity
```

---

# 3️⃣ Build Project

```bash
mvn clean install
```

---

# 4️⃣ Run Application

```bash
java -jar security-service/target/security-service.jar
```

---

# 🧪 Running Tests

Execute:

```bash
mvn test
```

This runs:
- unit tests
- mock-based tests
- service logic validation

---

# 📸 AWS Image Recognition Integration

The project supports AWS Rekognition integration.

### Optional Features

- real image recognition
- cat detection using AWS
- cloud-based image analysis

### Components

- `AwsImageService`
- Rekognition SDK integration

---

# 🔒 Design Principles Followed

The application emphasizes:

- modularity
- clean architecture
- separation of concerns
- dependency abstraction
- scalability
- maintainability
- testability

---

# 📈 Future Improvements

Potential enhancements include:

- REST API support
- Spring Boot migration
- JWT authentication
- cloud deployment
- Docker containerization
- database persistence
- mobile integration
- real IoT sensor connectivity
- notification systems

---

# 👨‍💻 Author

# Jashan Gupta

Software Engineer | Java Backend Developer | Security Systems Enthusiast

📧 Email:
jashangupta125@gmail.com

🔗 LinkedIn:
https://linkedin.com/in/jashan-analyst/

💻 GitHub:
https://github.com/Jashan122005

---

# 📜 License

This project was developed for educational, learning, and portfolio purposes.

---

# ⭐ Final Notes

This project strengthened practical understanding of:

- Java modularization
- Maven multi-module projects
- unit testing architecture
- mock-driven testing
- security workflow systems
- image analysis integration
- service abstraction
- scalable backend engineering
- software quality automation

The implementation focuses on maintainability, modularity, clean architecture, and production-style engineering practices.

```
