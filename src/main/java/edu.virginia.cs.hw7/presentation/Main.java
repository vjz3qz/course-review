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
