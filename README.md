# Course Reviews System

This is a Course Reviews System, a command-line user interface program that allows students to write course reviews for UVA courses, view reviews for UVA courses, and generate an output JSON file of all UVA courses.

## Requirements

To run this program, you will need the following:

- Java 8 or higher
- SQLite JDBC driver (version 3.36.0.3)

## Getting Started

1. Clone the repository to your local machine.
2. Compile the program using `javac`.
3. Run the program using `java`.

## Design

This program follows a 3-tier architecture with a separate Presentation Layer, Business Logic Layer, and Data Layer. The Presentation layer interacts with the Business Logic Layer, which handles collecting, filtering, and returning the relevant data to the user and sends new data to the Data Layer. The Data layer is the only layer that directly interacts with the database and writes the dump file.

## Database

The program uses an SQLite Database stored in the root directory named "Reviews.sqlite3". When the program starts, it checks if the database file exists. If it doesn't exist, the program creates the database file and runs the necessary "CREATE TABLE" queries to put the three required tables into the database.

The database has three tables:

- Students containing id number, name used for login, and password
- Courses containing id number, department, and catalog number
- Reviews containing id number, student id (foreign key to Students table), course id (foreign key to Courses table), review message, and rating (1-5)

The tables have appropriate constraints and foreign keys with "ON DELETE CASCADE" options.

## User Interface

The program satisfies the following UI requirements:

- Login: Users can login with their username and password or create a new user with a unique username and password.
- Main Menu: Once logged in, the user sees the main menu with the following options:
    - Submit a review for a course: Users can write a review message and rate a course (1-5), and the review is saved to the database. Users can only review a course once.
    - See reviews for course: Users can view all review messages and the average rating (out of 5) for a course.
    - Logout: Users can logout and return to the login screen.

## Usage

1. Start the program by running the compiled .class file with `java` command.
2. Login or create a new user.
3. Select an option from the main menu to submit a review or see reviews for a course.
4. Logout to exit the program.

## Future work

The program will be implemented as a JavaFX GUI application and use the Hibernate library.
