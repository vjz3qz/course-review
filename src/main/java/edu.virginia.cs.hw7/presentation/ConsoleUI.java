package edu.virginia.cs.hw7.presentation;

import edu.virginia.cs.hw7.businesslogic.Service;
import edu.virginia.cs.hw7.data.Database;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Student;

import java.util.Scanner;

public class ConsoleUI {

    private static Scanner scanner;


    public static void main(String[] args) {
        Service service = new Service();

        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("*".repeat(50));
            printLoginOptions();

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            String name;
            String password;
            System.out.println("*".repeat(50));
            switch (choice) {
                case 1 -> {
                    name = getName();
                    password = getPassword();
                    Student student = service.getStudentByLogin(name, password);
                    if (student == null)
                        break;

                    boolean logout = false;
                    System.out.println("Welcome " + student.getName() + "!");

                    while (!logout) {
                        System.out.println("*".repeat(50));
                        printMainMenuOptions();

                        choice = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        String[] courseName;
                        String department;
                        int catalogNumber;

                        switch (choice) {
                            case 1 -> service.printAllCourses();
                            case 2 -> { // print reviews for course
                                courseName = getCourse();
                                department = courseName[0];
                                catalogNumber = Integer.parseInt(courseName[1]);
                                service.printAllReviews(department, catalogNumber);
                            }
                            case 3 -> { // add a review
                                courseName = getCourse();
                                department = courseName[0];
                                catalogNumber = Integer.parseInt(courseName[1]);
                                Course course = service.getCourse(department, catalogNumber);
                                String message = getMessage();
                                int rating = getRating();
                                service.addReview(student, course, message, rating);
                            }
                            case 4 -> logout = true; // logout
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                }
                case 2 -> {
                    name = getName();
                    password = getPassword();
                    System.out.print("To confirm, ");
                    String confirmed = getPassword();
                    service.registerUser(name, password, confirmed);
                }
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.out.println("*".repeat(50));
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice.");
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
    private static String[] getCourse() {
        System.out.println("Enter Course:");
        String input = scanner.nextLine();
        String[] tokens = input.split(" ");
        while (tokens.length != 2 || !isValidDepartment(tokens[0]) || !isValidCatalogNumber(tokens[1])) {
            System.out.println("Invalid course format. Enter course (e.g. CS 3140): ");
            input = scanner.nextLine();
            tokens = input.split(" ");
        }
        tokens[0] = tokens[0].toUpperCase();
        return tokens;
    }

    private static boolean isValidCatalogNumber(String token) {
        while (token.isEmpty()) {
            System.out.println("Catalog number cannot be empty.");
            return false;
        }
        int catalogNumber = Integer.parseInt(token);
        while (catalogNumber < 1000 || catalogNumber > 9999) {
            System.out.println("Catalog number should be an integer between 1000 and 9999.");
            return false;
        }
        return true;
    }

    private static boolean isValidDepartment(String token) {
        if (token.isEmpty()) {
            System.out.println("Department cannot be empty.");
            return false;
        }
        if (token.length() != 2) {
            System.out.println("Department must be 2 letters.");
            return false;
        }
        return true;
    }
}
