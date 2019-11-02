import java.sql.*;

public class AccessSQL {

    String databasePath;

    public AccessSQL(String fp)   {
        databasePath = fp;
    }

    private Connection connect() {
        // SQLite connection string is attached to the AccessSQL object

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(databasePath);

            //This will turn on foreign keys
            //by default SQLite turns them off
            conn.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }


    private boolean checkRoom(String sql){
        //String sql = "SELECT snum, sname FROM suppliers;";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))   {

            // extract the boolean from the result set
            while (rs.next()) {
                boolean room = rs.getBoolean("isOccupied");// +  "\t" +
                return room;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }


    public void insertDoctor(String[] data) {

        String sql1 = "INSERT INTO Doctor(firstName, lastName, admitting) VALUES (?,?,?);";

        // Provide the correct 'boolean' value for Admitting field in data[]
        if (data[3].equals("C") || data[3].equals("c") )    {
            data[3] = "false";
        }
        else if (data[3].equals("A") || data[3].equals("a") )    {
            data[3] = "true";
        }
        else    {
            data[3] = null;
        }

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[1]); //First ? in sql
            ps.setString(2, data[2]); //Second ? in sql
            ps.setString(3, data[3]); //Second ? in sql
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void insertEmployee(String[] data) {

        String sql1 = "INSERT INTO Employee(firstName, lastName, title) VALUES (?,?,?);";

        String job;
        // Provide the correct job title
        if (data[0].equals("V"))    {
            job = "Volunteer";
        }
        else if (data[0].equals("A"))    {
            job = "Administrator";
        }
        else if (data[0].equals("N"))    {
            job = "Nurse";
        }
        else if (data[0].equals("T"))    {
            job = "Technician";
        }
        else {
            job = "N/A";
        }

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[1]); //First ? in sql
            ps.setString(2, data[2]); //Second ? in sql
            ps.setString(3, job); //Second ? in sql
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void insertInpatient(String[] data) {

        //add the diagnosis
        String sql1 = "INSERT INTO Diagnosis(disease) VALUES (?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[11]); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Inpatient Diagnosis");
        }

        //Check if the room is empty
        try {
            String roomCheck = "SELECT isOccupied FROM Room WHERE roomNumber = " + data[5] + ";";
            //String roomCheck = "SELECT roomNumber, isOccupied FROM Room;";

            boolean available = this.checkRoom(roomCheck);
            System.out.println(available);
            if(available)  {
                System.out.println("Patient " + data[2] + "not admitted, room " + data[5] +
                "is occupied.");
                return;
            }
        } catch (Exception e)    {
            System.out.println(e + " checking room avail for Inpatient");
        }



        sql1 = "INSERT INTO InPatient(patientID, lastName, primDoc, emergencyContactName, emergencyContactNumber, " +
                "diagnosis, room, admitDate, disDate) VALUES (?,?,?,?,?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[4]);
            ps.setString(2, data[2]);
            ps.setString(3, data[10]);
            ps.setString(4, data[6]);
            ps.setString(5, data[8]);
            ps.setString(6, data[11]);
            ps.setString(7, data[5]);
            ps.setString(8, data[12]);
            ps.setString(9, data[13]);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Inpatient Diagnosis");
        }

    }
/*
    public void insertDoctor(String ssn, String firstName, String lastName, String gender, String year, String month, String day) {

        //This is BAD!  Do NOT use!
        //This allows SQL injection attacks!
        //String BADsql = "INSERT INTO Suppliers(snum, sname, status, city) VALUES (" + snum + ","+ sname + ","+ status + "," + city + ");";

        //This is much better!
        String sql = "INSERT INTO Person(ssn, firstName, lastName, gender, year, month, day) VALUES (?,?,?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ssn); //First ? in sql
            ps.setString(2, firstName); //Second ? in sql
            ps.setString(3, lastName); //Third ? in sql
            ps.setString(4, gender); //Fourth ? in sql
            ps.setString(5, year); //First ? in sql
            ps.setString(6, month); //First ? in sql
            ps.setString(7, day); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


 */


}