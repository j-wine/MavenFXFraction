# FractionFX Application

A simple demonstration of a JavaFX-based desktop application utilizing the Model-View-Controller (MVC) design pattern.

## Features

### **Arithmetic Operations on Fractions**
- Supports addition, subtraction, multiplication, and division of fractions.
- Ensures user input validation to avoid invalid operations (e.g., zero denominators).

### **CSV Import/Export**
- **Export to CSV**: Save logs of fraction calculations in a structured `.csv` format for external analysis.
- **Import from CSV**: Load existing logs from `.csv` files to continue working or review historical data.

### **Concurrency and Task Management**
- Uses **JavaFX `Task` API** to support concurrency for operations:
  - Fraction calculations with progress tracking.
  - Exporting large datasets to CSV.
  - Importing and parsing complex CSV files.
- Uses an **ExecutorService** for efficient multi-threaded task execution.
- Provides a responsive UI by running tasks asynchronously while avoiding blocking operations.

### **JavaFX UI Features**
- **FXML-Based Interface**: Cleanly separates UI design from application logic.
- **Interactive Controls**:
  - Buttons for arithmetic operations.
  - Text fields with input validation for fractions.
  - List views for managing logs.
- **Real-Time Feedback**:
  - Progress bar updates during lengthy operations.
  - Alerts for user errors like invalid inputs or failed tasks.
- **Dynamic Log Visualization**: Logs are displayed in a customized list view, with options to edit or remove individual entries.

## How to Run

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd FractionFX
   ```
2. Build the project with Maven:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   java -jar target/FractionFX.jar
   ```

## Dependencies

- **JavaFX**: For the graphical user interface.
- **SLF4J**: For robust logging and debugging.
- **Java ExecutorService**: For efficient multi-threaded operations.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
