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

    public void printAllCourses() {
        List<Course> courses = database.getAllCourses();
        for (Course course : courses) {
            System.out.println(course);
        }
    }
    public void printAllReviews(String department, int catalogNumber) {
        List<Review> reviews = database.getCourseReviews(department, catalogNumber);
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

    public void registerUser(String name, String password, String confirmed) {
        if (!confirmed.equals(password)) {
            System.out.println("Passwords do not match.");
            return;
        }
        database.addStudent(new Student(name, password));
        System.out.println("Registration successful!");
    }
}
