import edu.virginia.cs.hw7.data.Database;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello");

        Database db = new Database();
        // create a sqlite db file if it does not exist
        db.connect();
        db.createTables();


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
