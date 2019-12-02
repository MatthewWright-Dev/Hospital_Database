import java.io.IOException;
import java.util.Scanner;

public class DatabaseDriver {
    public static void main(String[] args) throws IOException {

        //DEFAULT FILE PATHS
        String databasePath = "jdbc:sqlite:C:/SQLite/HospitalDB.db";
        String filePathPerson = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/PersonDataFile1.txt";
        String fileAddDoctor = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/AdditionalDoctor1.txt";
        String fileTreatment = "C:/users/Matt/Dropbox/Java_Code/Database 2 Project/src/TreatmentData.txt";

        //Initialize Scanner that will receive input with spacing
        Scanner user = new Scanner(System.in).useDelimiter("\n");

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
        if (!(customFile.isEmpty()))    {
            databasePath = customFile;
        }

        //Person Data File Selection
        System.out.println("PERSON DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (!(customFile.isEmpty()))    {
            filePathPerson = customFile;
        }

        //Additional Doctor Data File Selection
        System.out.println("ADDITIONAL DOCTOR DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (!(customFile.isEmpty()))    {
            fileAddDoctor = customFile;
        }

        //Additional Treatment Data File Selection
        System.out.println("ADDITIONAL TREATMENT DATA FILE PATH SELECTION:");
        customFile = DatabaseDriver.customPath();
        if (!(customFile.isEmpty()))    {
            fileTreatment = customFile;
        }

        //Import 3 files to database....
        System.out.println("\nIMPORTING THE FOLLOWING FILES:" +
                "\n" + filePathPerson + "\n" + fileAddDoctor + "\n" + fileTreatment);

        DataParser data = new DataParser(filePathPerson, fileAddDoctor, fileTreatment, databasePath);
        data.personData(filePathPerson);
        data.doctorData(fileAddDoctor);
        data.treatmentData(fileTreatment);


        DatabaseDriver.querieMenu(databasePath);


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
        user.close();
        return result;
    }

    public static void querieMenu(String databasePath) {
        Scanner user = new Scanner(System.in);
        //Print the Menu Header


        /*
        AccessSQL printQuerie = new AccessSQL(databasePath);
        String[] queries = DatabaseDriver.queryListGen();
        for (int i = 0; i < 27; i++)    {
            printQuerie.sqlQuery(queries[i]);
        }

         */


    }
    public static String[] queryListGen()   {
        String[] queries = new String[27];
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
                "WHERE admitDate > '2019-05-01'\n" +
                "AND disDate < '2019-07-01';";
        queries[6] ="SELECT patientID, firstName, lastName\n" +
                "FROM InPatient\n" +
                "WHERE disDate BETWEEN '2019-05-01' AND '2019-07-01';";
        queries[7] ="SELECT patientID, firstName, lastName \n" +
                "FROM OutPatient;";
        queries[8] ="SELECT patientid , firstName, lastName\n" +
                "FROM outpatient left join treatment\n" +
                "WHERE outpatient.lastname = treatment.patientname\n" +
                "AND treatment.time > '2019-05-10'\n" +
                "AND treatment.time < '2019-07-22' ;";
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
