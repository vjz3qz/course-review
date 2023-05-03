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
                            "name VARCHAR(100) NOT NULL UNIQUE," +
                            "password VARCHAR(100) NOT NULL" +
                            ")";
                    case "Courses" -> "CREATE TABLE Courses (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    department VARCHAR(4) NOT NULL CHECK (LENGTH(department) <= 4),\n" +
                            "    catalogNumber INTEGER(4) NOT NULL CHECK (catalogNumber BETWEEN 1000 AND 9999)\n" +
                            ");";
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

    private int getStudentIDFromStudent(Student student) {
        // TODO check if right
//        if(student.getId() != 0)
//            return student.getId();
        return getStudentByLogin(student.getName(), student.getPassword()).getId();
    }

    public Course getCourseByDepartmentAndCatalogNumber(String department, String catalogNumber) {
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (course.getDepartment().equals(department) && String.valueOf(course.getCatalogNumber()).equals(catalogNumber)) {
                return course;
            }
        }
        return null;
    }


    public void addStudent(Student student) {
        String sql = String.format("""
        insert into Students(name, password) values('%s', '%s');""",
                student.getName(), student.getPassword());
        try {
            String checkExistsSql = String.format("""
                SELECT * FROM Students WHERE name='%s' AND password='%s'
                """, student.getName(), student.getPassword());

            String checkUniqueNameSql = String.format("""
                SELECT * FROM Students WHERE name='%s'
                """, student.getName());

            Statement statement = connection.createStatement();
            ResultSet checkExists = statement.executeQuery(checkExistsSql);
            ResultSet checkUniqueName = statement.executeQuery(checkUniqueNameSql);
            if (checkExists.next()) {
                throw new IllegalArgumentException("Student already exists!");
            } if (checkUniqueName.next()) {
                throw new IllegalArgumentException("Student Name already exists!");
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

    private int getCourseIDByCourse(Course course) {
        // todo check if right
//        if(course.getId() != 0)
//            return course.getId();
        List<Course> courses = getAllCourses();
        for (Course courseCandidate: courses)
            if(courseCandidate.getDepartment().equals(course.getDepartment()) && courseCandidate.getCatalogNumber() == course.getCatalogNumber())
                return courseCandidate.getId();
        // todo add potential error handle for 0
        return 0;
    }

    public void addCourse(Course course) {
        String sql = String.format("""
        insert into Courses(department, catalogNumber) values('%s', '%s');""",
                course.getDepartment(), course.getCatalogNumber());
        try {
            String checkExistsSql = String.format("""
                SELECT * FROM Courses WHERE department='%s' AND catalogNumber='%d'
                """, course.getDepartment(), course.getCatalogNumber());
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

    public List<Review> getCourseReviews(String department, int catalogNumber) {
        List<Review> reviews = new ArrayList<>();

        // Loop through all reviews in the database
        for (Review review: getAllReviews()) {
            // Check if the review matches the given department and catalogNumber
            if (review.getCourse().getDepartment().equals(department) && review.getCourse().getCatalogNumber() == catalogNumber) {
                reviews.add(review);
            }
        }

        return reviews;
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
        // given a student, get an id
        int studentID = getStudentIDFromStudent(review.getStudent());
        // given a course, get an id
        int courseID = getCourseIDByCourse(review.getCourse());
        String sql = String.format("""
        insert into Reviews(studentID, courseID, message, rating) values(%d, %d, '%s', %d);""",
                studentID, courseID, review.getMessage(), review.getRating());
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

    // todo: reviews queries??

    //TODO rename all database columns

    //TODO add sufficient error handling

    //TODO create system for students to have id
}
