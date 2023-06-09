package edu.virginia.cs.hw7.businesslogic;

import edu.virginia.cs.hw7.data.Database;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;

import java.util.List;

public class Service {
    private static Database database;

    public Service() {
        database = new Database();
        database.connect();
        database.createTables();
    }
    public Student getStudentByLogin(String name, String password) {
        Student student = database.getStudentByName(name);
        if(student == null) {
            System.out.println("User does not exist.");
            return null;
        }
        student = database.getStudentByLogin(name, password);
        if(student == null) {
            System.out.println("User does not exist.");
            return null;
        }
        return student;
    }

    public void printAllStudents() {
        List<Student> students = database.getAllStudents();
        if(students.size() == 0) {
            System.out.println("no students.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void printAllCourses() {
        List<Course> courses = database.getAllCourses();
        if(courses.size() == 0) {
            System.out.println("no courses.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }
    public void printAllReviews(Course course) {
        List<Review> reviews = database.getCourseReviews(course.getDepartment(), course.getCatalogNumber());
        if(reviews.size() == 0) {
            System.out.println("no reviews.");
            return;
        }
        // Calculate the average rating
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        double averageRating = 0;
        if (!reviews.isEmpty()) {
            averageRating = (double) totalRating / reviews.size();
        }
        // Print the average rating
        System.out.println(course+" Review | Course Average: " + String.format("%.2f", averageRating) + "/5");

        for (Review review : reviews) {
            totalRating += review.getRating();
            System.out.println(review);
        }
    }

    public void printAllReviews() {
        List<Review> reviews = database.getAllReviews();
        if(reviews.size() == 0) {
            System.out.println("no reviews.");
            return;
        }
        for (Review review : reviews) {
            System.out.println(review);
        }
    }

    public Course getCourse(String department, int catalogNumber) {
        Course course = database.getCourseByDepartmentAndCatalogNumber(department, catalogNumber);
        if (course == null) {
            Course newCourse = new Course(department, catalogNumber);
            database.addCourse(newCourse);
            System.out.println("New course added successfully!");
            course = newCourse;
        }
        return course;
    }

    public void addReview(Student student, Course course, String message, int rating) {
        Review review = new Review(student, course, message, rating);
        database.addReview(review);
        System.out.println("Review added successfully!");
    }

    public boolean reviewExists(Student student, Course course) {
        if (database.reviewExists(student, course)) {
            System.out.println("User has already reviewed this course.");
            return true;
        }
        return false;
    }

    public void registerUser(String name, String password, String confirmed) {
        if (!confirmed.equals(password)) {
            System.out.println("Passwords do not match.");
            return;
        }
        database.addStudent(new Student(name, password));
        System.out.println("Registration successful!");
    }
}
