# Toy Language GUI

## Overview

**ToyLanguageGUI** is a JavaFX-based GUI application for interpreting and running a custom **Toy Programming Language**. This project demonstrates core concepts in interpreters, concurrent programming, and JavaFX UI design, aimed at practical understanding through an exam-style implementation.

## Features

- 🧮 **Expression Evaluation**: Supports arithmetic, logical, relational, and heap-reading expressions.
- 📝 **Statements Execution**: Includes assignment, compound, conditional, loop, file I/O, fork, and await statements.
- 📄 **ADT Implementations**: Custom stacks, maps, lists, heaps, and a barrier table for managing program state.
- 🕹️ **GUI Interface**: Built with JavaFX, allows selecting, executing, and debugging toy programs visually.
- ⚙️ **Concurrency Support**: Includes multithreading constructs like `fork`, `await`, and barriers.
- 🧪 **Test Logging**: Execution logs saved in multiple files (e.g., `log1.txt`, `log2.txt`, ...).

## Technologies Used

- **Java 17+**
- **JavaFX**
- **Maven** (Project Management)
- **IntelliJ IDEA** (Recommended IDE)

## Project Structure

```
ToyLanguageGUI/
│
├── src/
│   └── main/java/
│       ├── com.example.toylanguagegui/   # JavaFX UI: Main app and controllers
│       ├── controller/                   # Program controller logic
│       ├── exceptions/                   # Custom exception handling
│       └── model/
│           ├── ADT/                      # Custom data structures
│           ├── expressions/              # Expression evaluation logic
│           ├── statements/               # Statement execution logic
│           └── state/                    # Program state management
│
├── pom.xml                               # Maven configuration
├── logX.txt                              # Execution logs
└── test.in                               # Input file for testing
```

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/ToyLanguageGUI.git
   cd ToyLanguageGUI
   ```

2. **Import in IntelliJ IDEA**:
    - Open IntelliJ.
    - Choose **"Open"** and select the project folder.
    - Let IntelliJ import Maven dependencies automatically.

3. **Run the Application**:
    - Locate `MainApplication.java` in `com.example.toylanguagegui`.
    - Right-click and select **"Run"**.
    - The GUI will launch, allowing you to load and execute toy programs.

## Sample Usage

- Select a predefined toy program from the list.
- Execute step-by-step or run to completion.
- Monitor program state via GUI: Stack, Symbol Table, Heap, Output, etc.
- Logs are saved to `logX.txt` for each program run.

