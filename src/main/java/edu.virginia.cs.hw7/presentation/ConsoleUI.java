package edu.virginia.cs.hw7.presentation;

import edu.virginia.cs.hw7.data.Database;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private static final Database database = new Database();
    private static Scanner scanner;


    public static void main(String[] args) {
        database.connect();
        database.createTables();

        scanner = new Scanner(System.in);
        while (true) {
            printLoginOptions();

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    String name;
                    String password;
                    name = getName();
                    password = getPassword();
                    Student student = database.getStudentByLogin(name, password);
                    // todo check if student exists
                    // if exists, print this
                    if (student == null) {
                        System.out.println("Invalid login credentials.");
                        break;
                    }

                    // todo if not exists, say user does not exist

                    boolean logout = false;
                    System.out.println("Welcome " + student.getName() + "!");

                    while (!logout) {
                        printMainMenuOptions();

                        choice = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        String department;
                        int catalogNumber;

                        switch (choice) {
                            case 1 -> { //print all courses
                                // TODO add to business logic
                                List<Course> courses = database.getAllCourses();
                                for (Course course : courses) {
                                    System.out.println(course);
                                }
                            }
                            case 2 -> { // print reviews for course
                                department = getDepartment();
                                catalogNumber = getCatalogNumber();
                                // TODO add to business logic
                                List<Review> reviews = database.getCourseReviews(department, catalogNumber);
                                for (Review review : reviews) {
                                    System.out.println(review);
                                }
                            }
                            case 3 -> { // add a review
                                department = getDepartment();
                                catalogNumber = getCatalogNumber();
                                Course course = database.getCourseByDepartmentAndCatalogNumber(department, catalogNumber);
                                if (course == null) {
                                    Course newCourse = new Course(department, catalogNumber);
                                    // TODO add to business logic
                                    database.addCourse(newCourse);
                                    System.out.println("New course added successfully!");
                                    course = newCourse;
                                }
                                String message = getMessage();
                                int rating = getRating();
                                Review review = new Review(student, course, message, rating);
                                // TODO add to business logic
                                database.addReview(review);
                                System.out.println("Review added successfully!");
                            }
                            case 4 -> logout = true; // logout
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                    break;
                case 2:
                    name = getName();
                    password = getPassword();
                    System.out.print("To confirm, ");
                    String confirmed = getPassword();
                    if (!confirmed.equals(password)) {
                        System.out.println("Passwords do not match.");
                        break;
                    }
                    // TODO add to business logic
                    database.addStudent(new Student(name, password));
                    System.out.println("Registration successful!");
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }


    }

    private static void printMainMenuOptions() {
        System.out.println("1. View courses");
        System.out.println("2. View course reviews");
        System.out.println("3. Add course review");
        System.out.println("4. Logout");
    }

    private static void printLoginOptions() {
        System.out.println("Welcome to Course Review System!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
    }

    private static String getPassword() {
        String password;
        System.out.println("Enter your password:");
        password = scanner.nextLine();
        while (password.isEmpty()) {
            System.out.println("Password cannot be empty. Please enter your password:");
            password = scanner.nextLine();
        }
        while (password.length() > 200) {
            System.out.println("Password is too long (>200 characters). Please enter your password:");
            password = scanner.nextLine();
        }
        return password;
    }

    private static String getName() {
        String name;
        System.out.println("Enter your name:");
        name = scanner.nextLine();
        while (name.isEmpty()) {
            System.out.println("Name cannot be empty. Please enter your name:");
            name = scanner.nextLine();
        }
        while (name.length() > 200) {
            System.out.println("Name is too long (>200 characters). Please enter your name:");
            name = scanner.nextLine();
        }
        return name;
    }

    private static int getRating() {
        System.out.println("Enter your rating (1-5):");
        String ratingStr = scanner.nextLine();
        while (ratingStr.isEmpty()) {
            System.out.println("Rating cannot be empty. Please enter your rating:");
            ratingStr = scanner.nextLine();
        }
        int rating = Integer.parseInt(ratingStr);
        while (rating < 1 || rating > 5) {
            System.out.println("Rating should be an integer between 1 and 5. Please enter your rating:");
            ratingStr = scanner.nextLine();
            rating = Integer.parseInt(ratingStr);
        }
        return rating;
    }

    private static String getMessage() {
        System.out.println("Enter your review message:");
        String message = scanner.nextLine();
        while (message.isEmpty()) {
            System.out.println("Review message cannot be empty. Please enter your review message:");
            message = scanner.nextLine();
        }
        while (message.length() > 200) {
            System.out.println("Review message is too long (>200 characters). Please enter your review message:");
            message = scanner.nextLine();
        }
        return message;
    }

    private static int getCatalogNumber() {
        System.out.println("Enter catalog number:");
        String catalogNumberStr = scanner.nextLine();
        while (catalogNumberStr.isEmpty()) {
            System.out.println("Catalog number cannot be empty. Please enter catalog number:");
            catalogNumberStr = scanner.nextLine();
        }
        int catalogNumber = Integer.parseInt(catalogNumberStr);
        while (catalogNumber < 1000 || catalogNumber > 9999) {
            System.out.println("Catalog number should be an integer between 1000 and 9999. Please enter Catalog number:");
            catalogNumberStr = scanner.nextLine();
            catalogNumber = Integer.parseInt(catalogNumberStr);
        }
        return catalogNumber;
    }

    private static String getDepartment() {
        System.out.println("Enter department:");
        String department = scanner.nextLine();
        while (department.isEmpty()) {
            System.out.println("Department cannot be empty. Please enter department:");
            department = scanner.nextLine();
        }
        while (department.length() != 2) {
            System.out.println("Department must be 2 letters. Please enter department:");
            department = scanner.nextLine();
        }
        return department.toUpperCase();
    }
}
