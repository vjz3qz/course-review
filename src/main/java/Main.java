import edu.virginia.cs.hw7.data.Database;

import java.util.List;
import edu.virginia.cs.hw7.model.Course;
import edu.virginia.cs.hw7.model.Review;
import edu.virginia.cs.hw7.model.Student;

public class Main {
    public static void main(String[] args) {
        Course course = new Course("CS", 140);
        Student student = new Student("vr", "password");
        Review review = new Review(student, course, "great course", 5);
        Database db = new Database();
        // create a sqlite db file if it does not exist
        db.connect();
        db.createTables();
        db.addStudent(student);
        db.addCourse(course);
        db.addReview(review);
        System.out.println(db.getAllStudents());
        System.out.println(db.getAllCourses());
        System.out.println(db.getAllReviews());
        System.out.println(db.getStudentByLogin("vjz3qz","password"));

    }
}



//        //db.deleteTables();
//        db.createTables();
//        db.clear();
//        db.addStops(allStops);
//        db.addBusLines(allLines);
//        db.getBusLineById(4013468);
//        db.getStopByID(4235168);
//        db.getBusLineByLongName("29 North CONNECT");
//        db.getBusLineByShortName("BUCK-E");
//        db.getStopByName("George Welsh Way @ Scott Stadium");
//        db.getBusLines();
//        db.getAllStops();
