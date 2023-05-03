package edu.virginia.cs.hw7.presentation;

import edu.virginia.cs.hw7.data.Database;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private static final Database database = new Database();

    public static void main(String[] args) {
        database.connect();
        database.createTables();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to Course Review System!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter your username:");
                    String name = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String password = scanner.nextLine();

                    Student student = database.getStudentByLogin(name, password);
                    if (student == null) {
                        System.out.println("Invalid login credentials.");
                        break;
                    }

                    boolean choice4 = false;
                    System.out.println("Welcome " + student.getName() + "!");

                    while(!choice4) {
                        System.out.println("1. View courses");
                        System.out.println("2. View course reviews");
                        System.out.println("3. Add course review");
                        System.out.println("4. Logout");

                        choice = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        switch (choice) {
                            case 1:
                                List<Course> courses = database.getAllCourses();
                                for (Course course : courses) {
                                    System.out.println(course.getDepartment() + " " + course.getCatalogNumber() + " - " + database.getCourseReviews(course.getDepartment(), course.getCatalogNumber()).size() + " reviews");
                                }
                                break;
                            case 2:
                                System.out.println("Enter department:");
                                String dep = scanner.nextLine();
                                System.out.println("Enter catalog number:");
                                int catNum = scanner.nextInt();
                                List<Review> reviews = database.getCourseReviews(dep, catNum);
                                for (Review review : reviews) {
                                    System.out.println(dep + " " + catNum + ": (" + review.getRating() + ") " + review.getMessage());
                                }
                                break;
                            case 3:
                                System.out.println("Enter department:");
                                String department = scanner.nextLine();
                                System.out.println("Enter catalog number:");
                                int catalogNumber = scanner.nextInt();
                                scanner.nextLine(); // consume newline
                                System.out.println("Enter your review message:");
                                String message = scanner.nextLine();
                                System.out.println("Enter your rating (1-5):");
                                int rating = scanner.nextInt();

                                Course course = database.getCourseByDepartmentAndCatalogNumber(department, String.valueOf(catalogNumber));
                                if (course == null) {
                                    System.out.println("Invalid course.");
                                    break;
                                }

                                Review review = new Review(student, course, message, rating);
                                database.addReview(review);
                                System.out.println("Review added successfully!");
                                break;
                            case 4:
                                choice4 = true;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter your username:");
                    name = scanner.nextLine();
                    System.out.println("Enter your password:");
                    password = scanner.nextLine();

                    student = new Student(name, password);
                    database.addStudent(student);
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
}
