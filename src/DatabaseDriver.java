import java.io.IOException;

public class DatabaseDriver {
    public static void main(String[] args) throws IOException {

        String databasePath = "jdbc:sqlite:C:/users/Matt/Databases/hospTest2.db";
        String filePathPerson = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/data1.txt";
        DataParser data = new DataParser(filePathPerson, databasePath);
        data.personData(filePathPerson);

        //AccessSQL app = new AccessSQL();

        //app.insertDoctor("445434445", "Michael", "Holl", "M", "1995", "12", "1");
        //app.selectSample();
        //System.out.println("After Insert");
        //app.selectSample();
    }
}
