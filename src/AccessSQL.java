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

    public void sqlQuery(String sql) {
        try (Connection conn = this.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)){

            printResult(rs);
            //return true;

        }catch (SQLException s) {
            System.out.println(s + " tried sqlQuerie... whooooop");
            //return false;
        }
    }

    public static void printResult(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();
            int headerChars = 0;
            int rows = 0;

            //Print the Column Names as Header
            for (int i = 1; i <= colCount; i++) {
                String col_name = md.getColumnName(i);
                System.out.print(String.format("%-24s|", col_name));
                //System.out.print(col_name + "  |  ");
                headerChars += col_name.length();
            }

            //Print line under column names
            System.out.println();
            int e = md.getColumnCount() * 25;
            //e = (e * 5) + headerChars;
            for(int i = 0; i < e; i++)  {
                System.out.print("-");
            }
            System.out.println();

            //Retrieve the number of rows in this query result
            //PROB NOT NECESSARY
            /*
            if(rs.last()){
                rows = rs.getRow();
                rs.beforeFirst();
            }
            else    {
                System.out.println("Result Set is Empty. Sorry....");
                return;
            }
             */


            //print each line of the table
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++)  {
                    System.out.print(String.format("%-24s ", rs.getString(i)));
                }
                System.out.println();
            }


        } catch (SQLException e) {
            System.out.println(e + "tried to print column name result in printResult");
        }
    }

    private boolean checkRoom(String sql){
        //String sql = "SELECT snum, sname FROM suppliers;";
        //String roomCheck = "SELECT isEmpty FROM Room WHERE roomNumber = " + data[5] + ";";

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

    private boolean isInpatient(String sql){
        //String sql = "SELECT snum, sname FROM suppliers;";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))   {

            // extract the boolean from the result set
            while (rs.next()) {
                boolean name = rs.getBoolean("inHospital");// +  "\t" +
                return name;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "In isInpatient");
        }
        return false;
    }

    /*
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


     */


    public void insertEmployee(String[] data) {

        String sql1 = "INSERT INTO Employee(firstName, lastName, title, admitting) VALUES (?,?,?,?);";

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
        else if (data[0].equals("D"))    {
            job = "Doctor";
        }
        else {
            job = "N/A";
        }

        int admit = 0;
        if (data.length > 3 && data[3].equals("A"))    {
            admit = 1;
        }

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[1]); //First ? in sql
            ps.setString(2, data[2]); //Second ? in sql
            ps.setString(3, job); //Second ? in sql
            ps.setInt(4, admit);
            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addDiagnosis(String[] data) {
        //add the diagnosis to Diagnosis table
        String sql1 = "INSERT INTO Diagnosis(disease) VALUES (?);";
        boolean notNew = false;
        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[11]); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            //System.out.println(e.getMessage() + "In Inpatient Diagnosis");
            notNew = true;

        }

        //INCREMENT THE DIAGNOSIS COUNT
        if (notNew) {

            try (Connection conn = this.connect();) {
                String sql = "UPDATE Diagnosis SET cases = cases + 1 WHERE disease = '" + data[11] + "'";
                PreparedStatement ps = conn.prepareStatement(sql);
                //ps.executeUpdate(sql); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException a) {
                System.out.println("attempted to INC Diagnosis Count");
            }
        }
    }

    private void diagnose(String[] data) {
        //add the diagnosis to Diagnosis table
        String sql1 = "INSERT INTO Diagnose(doctor, disease) VALUES (?,?);";
        boolean notNew = false;
        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[10]); //First ? in sql
            ps.setString(2, data[11]); //First ? in sql
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            //System.out.println(e.getMessage() + "In Diagnose (initial)");
            notNew = true;
        }

        //INCREMENT THE DIAGNOSE COUNT
        if (notNew) {

            try (Connection conn = this.connect();) {
                String sql = "UPDATE Diagnose SET cases = cases + 1 WHERE doctor = '" + data[10] + "' AND disease = '" + data[11] + "';";
                PreparedStatement ps = conn.prepareStatement(sql);
                //ps.executeUpdate(sql); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException a) {
                System.out.println("attempted to INC Diagnose Count");
            }
        }
    }

    public void insertInpatient(String[] data) {


        addDiagnosis(data);
        diagnose(data);

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

            String sql1 = "INSERT INTO InPatient(patientID, lastName, firstName, primDoc, emergencyContactName, emergencyContactNumber, " +
                    "insNum, insName, diagnosis, room, admitDate, inHospital) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

            try (Connection conn = this.connect();) {

                PreparedStatement ps = conn.prepareStatement(sql1);

                ps.setString(1, data[4]);
                ps.setString(1, data[4]);
                ps.setString(2, data[2]);
                ps.setString(3, data[1]);
                ps.setString(4, data[10]);
                ps.setString(5, data[6]);
                ps.setString(6, data[7]);
                ps.setString(7, data[8]);
                ps.setString(8, data[9]);
                ps.setString(9, data[11]);
                ps.setString(10, data[5]);
                ps.setString(11, data[12]);
                ps.setString(12, "1");


                ps.executeUpdate();

                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "Adding past Impatient");
            }


        }
        else {

            String sql1 = "INSERT INTO InPatient(patientID, lastName, firstName, primDoc, emergencyContactName, emergencyContactNumber, " +
                    "insNum, insName, diagnosis, room, admitDate, disDate, inHospital) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

            try (Connection conn = this.connect();) {

                PreparedStatement ps = conn.prepareStatement(sql1);

                ps.setString(1, data[4]);
                ps.setString(2, data[2]);
                ps.setString(3, data[1]);
                ps.setString(4, data[10]);
                ps.setString(5, data[6]);
                ps.setString(6, data[7]);
                ps.setString(7, data[8]);
                ps.setString(8, data[9]);
                ps.setString(9, data[11]);
                ps.setString(10, data[5]);
                ps.setString(11, data[12]);
                ps.setString(12, data[13]);
                ps.setString(13, "0");

                ps.executeUpdate();

                ps.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage() + "Adding Past Inpatient");
            }
        }

    }

    public void insertOutpatient(String[] data) {

        //ADD THE DIAGNOSIS
        addDiagnosis(data);
        diagnose(data);


        String sql1 = "INSERT INTO OutPatient(patientID, lastName, firstName, primDoc, emergencyContactName, emergencyContactNumber, insNum, insName, diagnosis) VALUES (?,?,?,?,?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[4]);
            ps.setString(2, data[2]);
            ps.setString(3, data[1]);
            ps.setString(4, data[10]);
            ps.setString(5, data[6]);
            ps.setString(6, data[7]);
            ps.setString(7, data[8]);
            ps.setString(8, data[9]);
            ps.setString(9, data[11]);

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "Adding Outpatient");
        }

    }

    public void addDoctor(String[] data) {


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


        String sql1 = "INSERT INTO Procedure(procedureName, type) VALUES (?,?);";
        boolean proc = false;
        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setString(1, data[3]); //First ? in sql
            ps.setString(2, data[2]);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
                //System.out.println(e.getMessage() + "\nDuplicate not added, procedure already exists in DB");
                proc = true;
        }

        if(proc) {
            try (Connection conn = this.connect();) {
                String sql = "UPDATE Procedure SET cases = cases + 1 WHERE procedureName = '" + data[3] + "'";
                PreparedStatement ps = conn.prepareStatement(sql);
                //ps.executeUpdate(sql); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException a) {
                System.out.println("attempted to INC Procedure Count");
            }
        }
        //CHECK IF CURRENT INPATIENT
        String name = "SELECT inHospital FROM InPatient WHERE lastName = '" + data[0] + "';";
        boolean result = isInpatient(name);

        //ADD THE TREATMENT
        sql1 = "INSERT INTO Treatment(patientName, docRequest, treatType, treatment, time, isInpatient) VALUES (?,?,?,?,?,?);";

        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[0]);
            ps.setString(2, data[1]);
            ps.setString(3, data[2]);
            ps.setString(4, data[3]);
            ps.setString(5, data[4]);
            //Inpatient currently in hospital??
            if(result) {
                ps.setString(6, "1");
            }
            else {
                ps.setString(6, "0");
            }

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + " In AddTreatment");
        }

        //ADD TO Treats TABLE
        boolean treated =  false;
        sql1 = "INSERT INTO Treat(doctor, treatment) VALUES (?,?);";
        try (Connection conn = this.connect();) {

            PreparedStatement ps = conn.prepareStatement(sql1);

            ps.setString(1, data[1]);
            ps.setString(2, data[3]);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            //System.out.println(e.getMessage() + " In Add to Treats under Treatment");
            treated = true;
        }

        //INCREMENT THE TREATMENT
        if (treated)    {
            try (Connection conn = this.connect();) {
                String sql = "UPDATE Treat SET cases = cases + 1 WHERE doctor = '" + data[1] + "' AND treatment = '" + data[3] + "';";
                PreparedStatement ps = conn.prepareStatement(sql);
                //ps.executeUpdate(sql); //First ? in sql
                ps.executeUpdate();
                ps.close();

            } catch (SQLException a) {
                System.out.println("attempted to INC Treat Count");
            }

        }

    }



}