import java.io.IOException;

public class DatabaseDriver {
    public static void main(String[] args) throws IOException {

        //String databasePath = "jdbc:sqlite:C:/users/Matt/Databases/hospTest2.db";
        String databasePath = "jdbc:sqlite:C:/SQLite/HospitalDB.sl3";
        String filePathPerson = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/PersonDataFile1.txt";
        String fileAddDoctor = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/AdditionalDoctor1.txt";
        String fileTreatment = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/TreatmentData.txt";

        DataParser data = new DataParser(filePathPerson, fileAddDoctor, fileTreatment, databasePath);
        data.personData(filePathPerson);
        data.doctorData(fileAddDoctor);
        data.treatmentData(fileTreatment);

        //AccessSQL app = new AccessSQL();

        //app.insertDoctor("445434445", "Michael", "Holl", "M", "1995", "12", "1");
        //app.selectSample();
        //System.out.println("After Insert");
        //app.selectSample();
    }
}
