import java.io.IOException;
import java.util.Scanner;

public class DatabaseDriver {

    //DEFAULT FILE PATHS
    private static String databasePath = "jdbc:sqlite:C:/SQLite/HospitalDB.db";
    private static String filePathPerson = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/PersonDataFile1.txt";
    private static String fileAddDoctor = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/AdditionalDoctor1.txt";
    private static String fileTreatment = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/TreatmentData.txt";

    //QUERIES
    private static String[] queries = new String[27];


    public static void main(String[] args) throws IOException {

        //Populate the SQL Query Array
        queries = DatabaseDriver.queryListGen();

        //Initialize Scanner that will receive input with spacing
        //Scanner user = new Scanner(System.in).useDelimiter("\n");

        //Print the Menu Header
        for (int i = 0; i < 30; i++)    {
            System.out.print("=");
        }
        System.out.println();
        System.out.println("HOSPITAL DATABASE ACCESS");
        for (int i = 0; i < 30; i++)    {
            System.out.print("=");
        }
        System.out.println();

        System.out.println("*****  Phase 1: DATA IMPORT *****" +
                "\n\nYou will now be prompted to choose DEFAULT or CUSTOM file paths for the data import:\n");

        //Database File Selection
        System.out.println("DATABASE FILE PATH SELECTION:");
        String customFile = DatabaseDriver.customPath();
        if (customFile != null)    {
            databasePath = customFile;
        }

        //Person Data File Selection
        System.out.println("PERSON DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (customFile != null)    {
            filePathPerson = customFile;
        }

        //Additional Doctor Data File Selection
        System.out.println("ADDITIONAL DOCTOR DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (customFile != null)    {
            fileAddDoctor = customFile;
        }

        //Additional Treatment Data File Selection
        System.out.println("ADDITIONAL TREATMENT DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (customFile != null)    {
            fileTreatment = customFile;
        }

        //Import 3 files to database....
        System.out.println("\nIMPORTING THE FOLLOWING FILES:" +
                "\n" + filePathPerson + "\n" + fileAddDoctor + "\n" + fileTreatment);

        DataParser data = new DataParser(filePathPerson, fileAddDoctor, fileTreatment, databasePath);
        data.personData(filePathPerson);
        data.doctorData(fileAddDoctor);
        data.treatmentData(fileTreatment);

        //Move to the Query Phase
        DatabaseDriver.queryMenu();


    }

    public static String customPath()   {
        //Initialize Scanner that will receive input with spacing
        Scanner user = new Scanner(System.in).useDelimiter("\n");
        boolean loop = true;
        String result = null;
        int userIn = 0;
        while (loop) {
            loop = false;
            System.out.println("Press 1 for DEFAULT path, 2 for CUSTOM");
            userIn = user.nextInt();
            if (userIn != 1 && userIn != 2) {
                loop = true;
                System.out.println("Error: only 1 or 2 are valid responses");
            }
        }
        if (userIn == 2 ) {
            System.out.println("Input Custom File Path:");
            result = user.next();
        }
        //user.close();
        return result;
    }

    public static void queryMenu() {
        //Print the Menu Header
        boolean cont = true;
        System.out.println("\n*****  Phase 2: DATABASE QUERY *****");
        while (cont) {
            System.out.println("====================================");
            System.out.println("Main Menu");
            System.out.println("=============");
            System.out.println("1 = Room Utilization\n" +
                    "2 = Patient Information\n" +
                    "3 = Diagnosis and Treatment Information\n" +
                    "4 = Employee Information\n" +
                    "5 = Exit Hospital Database System\n" +
                    "=============\n" +
                    "Please Make a Selection:");
            int selection = menuChoice();
            switch (selection) {
                case 1:
                    menuRoom();
                    break;
                case 2:
                    menuPatient();
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                case 5:
                    System.out.println("============\n\n" +
                            "Goodbye........");
                    System.exit(0);
                default:
                    System.out.println("Invalid Input.....");
            }
        }

        /*
        AccessSQL printQuerie = new AccessSQL(databasePath);
        String[] queries = DatabaseDriver.queryListGen();
        for (int i = 0; i < 27; i++)    {
            printQuerie.sqlQuery(queries[i]);
        }

         */
    }

    public static void menuRoom()  {
        while (true) {
            System.out.println("====================================");
            System.out.println("Room Query Menu");
            System.out.println("=============");
            System.out.println("1 = List of Occupied Rooms\n" +
                    "2 = List of Unoccupied Rooms\n" +
                    "3 = List of All Rooms with Patient Names and Admission Dates\n" +
                    "4 = Main Menu\n" +
                    "=============\n" +
                    "Please Make a Selection:");
            int selection = menuChoice();
            switch (selection) {
                case 1:
                    simpleQuery(queries[0]);
                    break;
                case 2:
                    simpleQuery(queries[1]);
                    break;
                case 3:
                    simpleQuery(queries[2]);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Input.....");
            }
        }
    }

    public static void menuPatient ()   {
        while (true) {
            System.out.println("====================================");
            System.out.println("Patient Information Query Menu");
            System.out.println("=============");
            System.out.println("1 = List of Patients\n" +
                    "2 = List of Patients Currently Admitted\n" +
                    "3 = List of Inpatients Within Date Range\n" +
                    "4 = List of Patients Discharged Within Date Range\n" +
                    "5 = Current Outpatients\n" +
                    "6 = Outpatients Within Date Range\n" +
                    "7 = Specific Inpatient/Diagnosis History\n" +
                    "8 = Specific Patient Treatments\n" +
                    "9 = History of Patients admitted Within 30 days of Last Discharge\n" +
                    "10 = Complete Patient History with Admission Details\n" +
                    "11 = Main Menu\n" +
                    "=============\n" +
                    "Please Make a Selection:\n");
            int selection = menuChoice();
            switch (selection) {
                case 1:
                    simpleQuery(queries[3]);
                    break;
                case 2:
                    simpleQuery(queries[4]);
                    break;
                case 3:
                    dateQuery(queries[5]);
                    break;
                case 4:
                    dateQuery(queries[6]);
                    break;
                case 5:
                    simpleQuery(queries[7]);
                    break;
                case 6:
                    dateQuery(queries[8]);
                    break;
                case 7:
                    patientAdmissions();
                    //simpleQuery(queries[9]);
                    break;
                case 8:
                    simpleQuery(queries[10]);
                    break;
                case 9:
                    simpleQuery(queries[11]);
                    break;
                case 10:
                    simpleQuery(queries[12]);
                    break;
                case 11:
                    return;

                default:
                    System.out.println("Invalid Input.....");
            }
        }
    }

    public static void simpleQuery(String sql) {
        AccessSQL printQuerie = new AccessSQL(databasePath);
        printQuerie.sqlQuery(sql);
        System.out.println("Press 1 to Continue,  2 to Exit");
        if (menuChoice() == 2)  {
            System.out.println("\n\nGoodbye........");
            System.exit(0);
        }
    }

    public static void dateQuery(String sql)    {
        boolean loop = true;
        String dateStart = "";
        String dateEnd = "";

        while (loop) {
            loop = false;
            System.out.println("Date Format MUST be YEAR-MO-DA, example 2019-07-01");
            System.out.println("Enter the beginning date of date range:");
            dateStart = getUserString();
            System.out.println("Enter the end date of date range:");
            dateEnd = getUserString();
            if (dateStart.length() != 10 || dateEnd.length() != 10) {
                System.out.println("INCORRECT DATE FORMAT RECEIVED, PLEASE RETRY");
                loop = true;
            }
        }
        sql = sql.replaceAll("DATE1", dateStart);
        sql = sql.replaceAll("DATE2", dateEnd);
        simpleQuery(sql);

    }

    public static void patientAdmissions()   {
        boolean loop = true;
        int selection = 0;
        while (loop)    {
            loop = false;
            System.out.println("1 = Search Patient by Name\n" +
                    "2 = Search Patient by ID Number");
            selection = menuChoice();
            if (selection != 1 && selection != 2)   {
                System.out.println("Invalid Input, Must select 1 or 2");
                loop = true;
            }
        }

        if (selection == 1) {
            System.out.println("Please Enter Patient's Last Name:");
            String temp = getUserString();
            temp = temp.toUpperCase();
            String sql = "SELECT admitDate, diagnosis\n" +
                    "FROM InPatient\n" +
                    "WHERE lastName = '" + temp + "';";
            simpleQuery(sql);
            return;
        }
        else    {
            System.out.println("Please Enter Patient ID Number:");
            String temp = getUserString();
            String sql = "SELECT admitDate, diagnosis\n" +
                    "FROM InPatient\n" +
                    "WHERE patientID = '" + temp + "';";
            simpleQuery(sql);
            return;
        }
    }

    public static String getUserString()  {
        Scanner user = new Scanner(System.in).useDelimiter("\n");
        return user.next();
    }

    public static int menuChoice()    {
        Scanner user = new Scanner(System.in);
        return user.nextInt();
    }

    public static String[] queryListGen()   {
        //String[] queries = new String[27];
        // 1. Room Utilization
        queries[0] = "SELECT room, firstName, lastName, admitDate FROM InPatient LEFT JOIN Room ON Room.roomNumber=InPatient.room WHERE Room.isEmpty=1;";
        queries[1] = "SELECT * FROM Room WHERE isEmpty = 0;";
        queries[2] = "SELECT room.roomnumber, inpatient.lastname FROM room LEFT JOIN inpatient WHERE room.roomnumber = inpatient.room AND room.isempty=1\n" +
                "UNION\n" +
                "SELECT room.roomnumber, room.isempty\n" +
                "FROM room;";
        // 2. Patient Information
        queries[3] = "SELECT patientID, firstName, lastName, primDoc, emergencyContactName, emergencyContactNumber, diagnosis \n" +
                "FROM InPatient\n" +
                "UNION\n" +
                "SELECT patientID, firstName, lastName, primDoc, emergencyContactName, emergencyContactNumber, diagnosis \n" +
                "FROM OutPatient;";
        queries[4] = "SELECT patientID, firstName, lastName\n" +
                "FROM InPatient\n" +
                "WHERE inHospital = 1;";
        queries[5] ="SELECT patientID, firstName, lastName\n" +
                "FROM InPatient\n" +
                "WHERE admitDate > 'DATE1'\n" +
                "AND disDate < 'DATE2';";
        queries[6] ="SELECT patientID, firstName, lastName\n" +
                "FROM InPatient\n" +
                "WHERE disDate BETWEEN 'DATE1' AND 'DATE2';";
        queries[7] ="SELECT patientID, firstName, lastName \n" +
                "FROM OutPatient;";
        queries[8] ="SELECT patientid , firstName, lastName\n" +
                "FROM outpatient left join treatment\n" +
                "WHERE outpatient.lastname = treatment.patientname\n" +
                "AND treatment.time > 'DATE1'\n" +
                "AND treatment.time < 'DATE2' ;";
        queries[9] ="SELECT admitDate, diagnosis\n" +
                "FROM InPatient\n" +
                "WHERE lastName = 'BONES'\n" +
                "OR patientID = 5212;";
        queries[10] ="SELECT InPatient.admitDate, Treatment.patientName, Treatment.docRequest, Treatment.treatType, Treatment.treatment, Treatment.time, Treatment.treatID\n" +
                "FROM Treatment LEFT JOIN InPatient \n" +
                "WHERE InPatient.lastName = Treatment.patientName\n" +
                "GROUP BY InPatient.admitDate\n" +
                "ORDER BY InPatient.admitDate DESC, Treatment.time ASC ;";
        queries[11] ="SELECT patientID, lastName, diagnosis, primDoc\n" +
                "FROM InPatient\n" +
                "WHERE disDate - admitDate < 30;";
        queries[12] ="SELECT lastname, COUNT(lastName)\n" +
                "FROM inPatient\n" +
                "GROUP BY lastName;";

        //3. Diagnosis and Treatment Information
        queries[13] ="SELECT DISTINCT disease, cases, dID\n" +
                "FROM Diagnosis\n" +
                "LEFT JOIN InPatient\n" +
                "WHERE InPatient.diagnosis = Diagnosis.disease\n" +
                "ORDER BY cases DESC; ";
        queries[14] ="SELECT DISTINCT disease, cases, dID\n" +
                "FROM Diagnosis\n" +
                "LEFT JOIN OutPatient\n" +
                "WHERE OutPatient.diagnosis = Diagnosis.disease\n" +
                "ORDER BY cases DESC;";
        queries[15] ="SELECT * FROM Diagnosis\n" +
                "ORDER BY cases DESC;";
        queries[16] ="SELECT Treatment.treatID, Treatment.treatment, Procedure.cases\n" +
                "FROM Treatment LEFT JOIN procedure\n" +
                "WHERE Treatment.treatment = Procedure.procedureName\n" +
                "ORDER BY Procedure.cases DESC;";
        queries[17] ="SELECT Treatment.treatID, Treatment.treatment, Procedure.cases\n" +
                "FROM Treatment LEFT JOIN procedure\n" +
                "WHERE Treatment.treatment = Procedure.procedureName\n" +
                "AND Treatment.isInpatient = 1\n" +
                "ORDER BY Procedure.cases DESC;";
        queries[18] ="SELECT Treatment.treatID, Treatment.treatment, Procedure.cases\n" +
                "FROM Treatment LEFT JOIN procedure\n" +
                "WHERE Treatment.treatment = Procedure.procedureName\n" +
                "ORDER BY Procedure.cases DESC;";
        queries[19] = "SELECT Diagnose.disease, Diagnose.cases, COUNT(InPatient.lastName)\n" +
                "FROM Diagnose LEFT JOIN InPatient\n" +
                "WHERE Diagnose.doctor = InPatient.primDoc\n" +
                "GROUP BY Diagnose.disease;";
        queries[20] = "SELECT treatID, treatment, patientName, docRequest\n" +
                "FROM Treatment;";

        // 4. Employee Information
        queries[21] ="SELECT lastName, firstName, title\n" +
                "FROM Employee\n" +
                "ORDER BY lastName ASC;";
        queries[22] ="SELECT primDoc, COUNT(primDoc)\n" +
                "FROM InPatient\n" +
                "GROUP BY primDoc;";
        queries[23] ="SELECT doctor, disease, cases\n" +
                "FROM diagnose\n" +
                "WHERE doctor = 'SANTOS'\n" +
                "ORDER BY cases DESC;";
        queries[24] = "SELECT doctor, treatment, cases\n" +
                "FROM Treat\n" +
                "WHERE doctor = 'SANTOS'\n" +
                "ORDER BY cases DESC;";
        queries[25] ="SELECT doctor, treatment, cases\n" +
                "FROM Treat\n" +
                "WHERE doctor = 'SANTOS'\n" +
                "ORDER BY cases DESC;";
        queries[26] = "SELECT Treatment.patientName, Treatment.docRequest, Employee.firstName, Employee.title\n" +
                "FROM Treatment LEFT JOIN Employee\n" +
                "WHERE Treatment.docRequest = Employee.lastName;";


        return queries;
    }
}
