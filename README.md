# Student Record Management System

This is a JavaFX desktop application for managing student records in a PostgreSQL database. It supports adding, updating, deleting, and viewing student data through a simple form and table interface.

## Features

- Add student records
- Update selected student records
- Delete selected student records
- View all saved students in a table
- Select a year level from a dropdown

## Tech Stack

- Java 21
- JavaFX 21
- Maven
- PostgreSQL

## Project Structure

- `src/main/java/com/student/MainApp.java` - application entry point
- `src/main/java/com/student/Controller.java` - UI behavior and database actions
- `src/main/java/com/student/DBConnection.java` - PostgreSQL connection setup
- `src/main/java/com/student/Student.java` - student model
- `src/main/java/com/student/YearLevel.java` - year level enum
- `src/main/resources/main.fxml` - JavaFX layout

## Requirements

- JDK 21
- Maven
- PostgreSQL installed and running
- A database named `studentdb`

## Database Setup

Create a `students` table before running the app:

```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    year_level VARCHAR(20) NOT NULL
);
```

If needed, update the connection details in `src/main/java/com/student/DBConnection.java` to match your local PostgreSQL setup.

## Run the Application

From the project root:

```bash
mvn clean javafx:run
```

## Usage

1. Enter the student name.
2. Enter the course.
3. Choose a year level.
4. Click `Add` to save the record.
5. Select a row in the table to edit or delete it.

## Notes

- The app loads data from PostgreSQL when it starts.
- If the database connection fails, the app will show an error dialog.
- The year level dropdown uses the values defined in `YearLevel.java`.

