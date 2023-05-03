package edu.virginia.cs.hw7.data;

import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.*;

public class Database {

    Connection connection;
    String url = "jdbc:sqlite:";

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
                // create statement
                Statement statement = connection.createStatement();
                // create students table
                // create sql string
                String createStudentsSql = "CREATE TABLE Students (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR(100) NOT NULL," +
                        "password VARCHAR(100) NOT NULL" +
                        ");";

                // execute statement
                statement.executeUpdate(createStudentsSql);
                // create courses table
                // create sql string
                String createCoursesSql = "CREATE TABLE Courses (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "department VARCHAR(4) NOT NULL," +
                        "catalogNumber VARCHAR(4) NOT NULL" +
                        ");";

                // execute statement
                statement.executeUpdate(createCoursesSql);

                //create reviews
                String createReviewsSql = "CREATE TABLE Reviews (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "studentID INTEGER," +
                        "courseID INTEGER," +
                        "message VARCHAR(500) NOT NULL," +
                        "rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5)," +
                        "FOREIGN KEY(studentID) REFERENCES Students(id) ON DELETE CASCADE," +
                        "FOREIGN KEY(courseID) REFERENCES Courses(id) ON DELETE CASCADE" +
                        ");";


                // execute statement
                statement.executeUpdate(createReviewsSql);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    public void clear() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            } else {
                Statement statement = connection.createStatement();
                if (!tableExists("Stops"))
                    throw new IllegalStateException("Table 'Stops' does not exist");
                if (!tableExists("BusLines"))
                    throw new IllegalStateException("Table 'BusLines' does not exist");
                if (!tableExists("Routes"))
                    throw new IllegalStateException("Table 'Routes' does not exist");
                statement.executeUpdate("DELETE FROM Routes");
                statement.executeUpdate("DELETE FROM Stops");
                statement.executeUpdate("DELETE FROM BusLines");
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName){
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            boolean next = resultSet.next();
            resultSet.close();
            return next;

        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }


    public void deleteTables() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Manager is not connected");
            } else {
                Statement statement = connection.createStatement();
                String[] tableNames = {"Students", "Courses", "Reviews"};

                for (String tableName : tableNames) {
                    if (!tableExists(tableName)) {
                        String message = "Table '" + tableName + "' does not exist";
                        throw new IllegalStateException(message);
                    }
                }
                try {
                    statement.execute("DROP TABLE Students");
                } catch (SQLiteException e) {
                    String message = "Table 'Students' does not exist";
                    throw new IllegalStateException(message);

                } try {
                    statement.execute("DROP TABLE Courses");
                } catch (SQLiteException e) {
                    String message = "Table 'Courses' does not exist";
                    throw new IllegalStateException(message);
                } try {
                    statement.execute("DROP TABLE Reviews");
                } catch (SQLiteException e) {
                    String message = "Table 'Reviews' does not exist";
                    throw new IllegalStateException(message);
                }
                statement.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error executing SQL statements: " + e.getMessage());
        }
    }

    //todo
    // get student list, get course list
    // students: add all, get all, find single
    // courses: add all, get all, find single
    // look at reviews
}
