package edu.virginia.cs.hw7.data;

import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    Connection connection;
    String url = "jdbc:sqlite:";

    String[] tableNames = {"Students", "Courses", "Reviews"};

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url + ConfigSingleton.getInstance().getDatabaseFilename());
            } else {
                throw new IllegalStateException("Manager is already connected");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTables() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            } else {
                for(String table: tableNames)
                    if(tableDoesNotExists(table))
                        createTable(table);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }
    public void createTable(String tableName) {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            } else {
                Statement statement = connection.createStatement();
                String sql = switch (tableName) {
                    case "Students" -> "CREATE TABLE Students (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name VARCHAR(100) NOT NULL," +
                            "password VARCHAR(100) NOT NULL" +
                            ")";
                    case "Courses" -> "CREATE TABLE Courses (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "department VARCHAR(4) NOT NULL," +
                            "catalogNumber VARCHAR(4) NOT NULL" +
                            ")";
                    case "Reviews" -> "CREATE TABLE Reviews (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "studentID INTEGER," +
                            "courseID INTEGER," +
                            "message VARCHAR(500) NOT NULL," +
                            "rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5)," +
                            "FOREIGN KEY(studentID) REFERENCES Students(id) ON DELETE CASCADE," +
                            "FOREIGN KEY(courseID) REFERENCES Courses(id) ON DELETE CASCADE" +
                            ")";
                    default -> throw new IllegalArgumentException("Invalid table name: " + tableName);
                };
                statement.executeUpdate(sql);
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    private boolean tableDoesNotExists(String tableName){
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            boolean next = resultSet.next();
            resultSet.close();
            return !next;

        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        try{
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            }
            if(tableDoesNotExists("Students")){
                throw new IllegalStateException("Students table does not exist");
            }
            String sql = "SELECT * FROM Students;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Student> students = new ArrayList<>();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                students.add(new Student(id, name, password));
            }
            statement.close();
            return students;
        } catch(SQLException e){
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public Student getStudentByLogin(String name, String password) {
        List<Student> students = getAllStudents();
        for (Student student: students)
            if(student.getName().equals(name) && student.getPassword().equals(password))
                return student;
        return null;
    }

    private Student getStudentByID(int studentID) {
        List<Student> students = getAllStudents();
        for (Student student: students)
            if(student.getId() == studentID)
                return student;
        return null;
    }

    public void addStudent(Student student) {
        String sql = String.format("""
        insert into Students(id, name, password) values(%d, '%s', '%s');""",
                student.getId(), student.getName(), student.getPassword());
        try {
            String checkExistsSql = "SELECT * FROM Students WHERE id= " + student.getId() + " ";
            Statement statement = connection.createStatement();
            ResultSet checkExists = statement.executeQuery(checkExistsSql);
            if (checkExists.next()) {
                throw new IllegalArgumentException("Student already exists!");
            } else {
                statement.executeUpdate(sql);
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        try{
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            }
            if(tableDoesNotExists("Courses")){
                throw new IllegalStateException("Courses table does not exist");
            }
            String sql = "SELECT * FROM Courses;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Course> courses = new ArrayList<>();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String department = resultSet.getString("department");
                int catalogNumber = resultSet.getInt("catalogNumber");
                courses.add(new Course(id, department, catalogNumber));
            }
            statement.close();
            return courses;
        } catch(SQLException e){
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    private Course getCourseByID(int courseID) {
        List<Course> courses = getAllCourses();
        for (Course course: courses)
            if(course.getId() == courseID)
                return course;
        return null;
    }

    public void addCourse(Course course) {
        String sql = String.format("""
        insert into Courses(id, department, catalogNumber) values(%d, '%s', '%s');""",
                course.getId(), course.getDepartment(), course.getCatalogNumber());
        try {
            String checkExistsSql = "SELECT * FROM Courses WHERE id= " + course.getId() + " ";
            Statement statement = connection.createStatement();
            ResultSet checkExists = statement.executeQuery(checkExistsSql);
            if (checkExists.next()) {
                throw new IllegalArgumentException("Course already exists!");
            } else {
                statement.executeUpdate(sql);
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public List<Review> getAllReviews() {
        try{
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            }
            if(tableDoesNotExists("Reviews")){
                throw new IllegalStateException("Reviews table does not exist");
            }
            String sql = "SELECT * FROM Reviews;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Review> reviews = new ArrayList<>();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int studentID = resultSet.getInt("studentID");
                Student student = getStudentByID(studentID);
                int courseID = resultSet.getInt("courseID");
                Course course = getCourseByID(courseID);
                String message = resultSet.getString("message");
                int rating = resultSet.getInt("rating");
                if(student != null) {
                    reviews.add(new Review(id, student, course, message, rating));
                }
            }
            statement.close();
            return reviews;
        } catch(SQLException e){
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public void addReview(Review review) {
        String sql = String.format("""
        insert into Reviews(id, studentID, courseID, message, rating) values(%d, %d, %d, '%s', %d);""",
                review.getId(), review.getStudent().getId(), review.getCourse().getId(), review.getMessage(), review.getRating());
        try {
            String checkExistsSql = "SELECT * FROM Reviews WHERE id= " + review.getId() + " ";
            Statement statement = connection.createStatement();
            ResultSet checkExists = statement.executeQuery(checkExistsSql);
            if (checkExists.next()) {
                throw new IllegalArgumentException("Review already exists!");
            } else {
                statement.executeUpdate(sql);
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            }
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    //todo
    // reviews: queries??

    //TODO rename all database columns
}
