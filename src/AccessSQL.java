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
                boolean room = rs.getBoolean("isEmpty");// +  "\t" +
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

        //add the diagnosis to Diagnosis table
        String sql1 = "INSERT INTO Diagnosis(disease) VALUES (?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[11]); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Inpatient Diagnosis");
        }

        //System.out.println(data.length);
        //IF the Inpatient does not have a discharge data, the room must be checked/marked.
        if (data.length < 14) {
            //Check if the room is empty
            try {
                String roomCheck = "SELECT isEmpty FROM Room WHERE roomNumber = " + data[5] + ";";
                boolean available = this.checkRoom(roomCheck);
                if (available) {
                    System.out.println("Patient " + data[2] + " not admitted, room " + data[5] +
                            " is occupied.");
                    //If room is full, we abort this Inpatient
                    return;
                }
            } catch (Exception e) {
                System.out.println(e + " checking room avail for Inpatient");
            }

            String sql = "UPDATE Room SET isEmpty = true WHERE roomNumber = " + data[5] + ";";

            try (Connection conn = this.connect();) {

                PreparedStatement ps = conn.prepareStatement(sql);
                //ps.setString(1, data[11]); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "Attempting to Mark Room as full");
            }

            sql1 = "INSERT INTO InPatient(patientID, lastName, primDoc, emergencyContactName, emergencyContactNumber, " +
                    "diagnosis, room, admitDate) VALUES (?,?,?,?,?,?,?,?);";

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


                ps.executeUpdate();

                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "In Inpatient Diagnosis");
            }


        }
        else {

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

    }

    public void insertOutpatient(String[] data) {

        //add the diagnosis
        String sql1 = "INSERT INTO Diagnosis(disease) VALUES (?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[11]); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Outpatient Diagnosis");
        }



        sql1 = "INSERT INTO OutPatient(patientID, lastName, firstName, primDoc, emergencyContactName, emergencyContactNumber, diagnosis) VALUES (?,?,?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[4]);
            ps.setString(2, data[2]);
            ps.setString(3, data[1]);
            ps.setString(4, data[10]);
            ps.setString(5, data[6]);
            ps.setString(6, data[7]);
            ps.setString(7, data[11]);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Inpatient Diagnosis");
        }

    }

    public void addDoctor(String[] data) {

        for (String a: data
             ) {
            System.out.println(a);
        }


        String sql1 = "INSERT INTO AssignDoctor(patient, additionalDoctor) VALUES (?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[0]);
            ps.setString(2, data[1]);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In Add Doctor");
        }

    }
    public void addTreatment(String[] data) {

        //ADD THE MEDICATION / PROCEDURE
        if( data[2].equals("M"))    {
            String sql1 = "INSERT INTO Medication(medName) VALUES (?);";
            try (Connection conn = this.connect();) {

                PreparedStatement ps = conn.prepareStatement(sql1);
                ps.setString(1, data[3]); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "In addTreatment: Medication");
            }
        }
        else   {
            String sql1 = "INSERT INTO Procedure(procedureName) VALUES (?);";
            try (Connection conn = this.connect();) {

                PreparedStatement ps = conn.prepareStatement(sql1);
                ps.setString(1, data[3]); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "In addTreatment: Procedure");
            }
        }

        //ADD THE TREATMENT
        String sql1 = "INSERT INTO Treatment(docRequest, patientName, medName, procedureName, time) VALUES (?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[1]);
            ps.setString(2, data[0]);
            //MEDS OR PROCEDURE??
            if(data[2].equals("M")) {
                ps.setString(3, data[3]);
                ps.setString(4, null);

            }
            else {
                ps.setString(4, data[3]);
                ps.setString(3, null);
            }

            ps.setString(5, data[4]);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + " In AddTreatment");
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