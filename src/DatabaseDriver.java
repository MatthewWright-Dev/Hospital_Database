import java.io.IOException;

public class DatabaseDriver {
    public static void main(String[] args) throws IOException {

        //String databasePath = "jdbc:sqlite:C:/users/Matt/Databases/hospTest2.db";
        /*
        BuildDB newDB = new BuildDB("C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/Tables.txt" , "test.sq3");
        newDB.createNewDatabase(newDB.dbName);


         */

        String databasePath = "jdbc:sqlite:C:/SQLite/HospitalDB.db";
        String filePathPerson = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/PersonDataFile1.txt";
        String fileAddDoctor = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/AdditionalDoctor1.txt";
        String fileTreatment = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/TreatmentData.txt";

        DataParser data = new DataParser(filePathPerson, fileAddDoctor, fileTreatment, databasePath);
        data.personData(filePathPerson);
        data.doctorData(fileAddDoctor);
        data.treatmentData(fileTreatment);

        AccessSQL queries = new AccessSQL(databasePath);
        String testQ = "SELECT * FROM InPatient;";
        queries.sqlQuery(testQ);


    }
}
