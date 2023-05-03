package edu.virginia.cs.hw7.data;

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

    public void addStudent(Student student) {
        String sql = String.format("""
            insert into Students(id, name, password) values(%d, %s", "%s");""",
                student.getId(), student.getName(), student.getPassword());
        try {
            String checkExistsSql = "SELECT * FROM Students WHERE id= " + student.getId() + " ";
            Statement statement = connection.createStatement();
            ResultSet checkExists = statement.executeQuery(checkExistsSql);
            if (checkExists.next()) {
                throw new IllegalArgumentException("Student already exists!");
            } else {
                statement.executeUpdate(sql);
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
    // courses: add one, get all, find single
    // START TO look at reviews

    //TODO rename all database columns
}
