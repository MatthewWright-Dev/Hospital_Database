import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BuildDB {
    String dbText;
    String dbName;

    public BuildDB(String txt, String name) {
        dbText = txt;
        dbName = name;
    }

    public static void createNewDatabase(String filename) {
        String url = "jdbc:sqlite:C:/sqlite/" + filename;

        try (Connection conn = DriverManager.getConnection(url))    {
            if (conn != null)   {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        }
        catch (SQLException e)  {
            System.out.println(e.getMessage());
        }


    }

    public static void populateDatabase() {

    }

}
