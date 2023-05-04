package edu.virginia.cs.hw7.presentation;

import edu.virginia.cs.hw7.data.Database;

import java.util.List;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;

public class Main {

    private static final Database database = new Database();
    public static void main(String[] args) {
        database.connect();
        database.createTables();

    }
}
