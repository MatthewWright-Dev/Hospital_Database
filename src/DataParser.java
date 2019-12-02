import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataParser {
    String filepathPerson;
    String filepathDoctor;
    String getFilepathTreatment;
    String databasePath;

    public DataParser (String fpp, String fpd, String fpt, String dbp)   {
        filepathPerson = fpp;
        filepathDoctor = fpd;
        getFilepathTreatment = fpt;
        databasePath = dbp;

    }

    public void personData(String fileName) throws IOException {

        AccessSQL app = new AccessSQL(this.databasePath);

        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null)  {
                lineCount++;
                String[] data = line.split(",");

                for (int i = 0; i < data.length; i++)   {
                    data[i] = data[i].trim();
                    data[i] = data[i].toUpperCase();
                }

                char pers = data[0].charAt(0);
                switch (pers)   {
                    case 'D': app.insertEmployee(data);
                        break;
                    case 'V': app.insertEmployee(data);
                        break;
                    case 'A': app.insertEmployee(data);
                        break;
                    case 'N': app.insertEmployee(data);
                        break;
                    case 'T': app.insertEmployee(data);
                        break;
                    case 'I': app.insertInpatient(data);
                        break;
                    case 'O': app.insertOutpatient(data);
                        break;
                    default:
                        System.out.println("The character \"" + pers + "\" at line " + lineCount
                                + " of the Person_Data_File is invalid.");
                        System.out.println("Valid characters and person types are:\n");
                        System.out.println("V for Volunteer\nD for Doctor\nA for Administrator\n" +
                                "N for Nurse\nT for Technician\nI for Inpatient\nO for Outpatient");
                }
            }
        }
        catch (IOException e)   {
            System.out.println(e);
        }
    }

    public void doctorData(String fileName) throws IOException {

        AccessSQL app = new AccessSQL(this.databasePath);

        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null)  {
                lineCount++;
                String[] data = line.split(",");

                //CHECK FOR INVALID NUMBER OF ARGUMENTS
                if(data.length < 2) {
                    System.out.println("AT line " + lineCount + " invalid input: Missing Patient/Doctor Pair...");
                    return;
                }

                for (int i = 0; i < data.length; i++)   {
                    data[i] = data[i].trim();
                    data[i] = data[i].toUpperCase();
                }

                app.addDoctor(data);
            }
        }
        catch (IOException e)   {
            System.out.println(e);
        }
    }

    public void treatmentData(String fileName) throws IOException {

        AccessSQL app = new AccessSQL(this.databasePath);

        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null)  {
                lineCount++;
                String[] data = line.split(",");

                //CHECK FOR INVALID NUMBER OF ARGUMENTS
                if(data[2] == null || data[3] == null) {
                    System.out.println("AT line " + lineCount + " invalid input: Missing Procedure/Medication Pair...");
                    return;
                }

                for (int i = 0; i < data.length; i++)   {
                    data[i] = data[i].trim();
                    data[i] = data[i].toUpperCase();
                }

                app.addTreatment(data);
            }
        }
        catch (IOException e)   {
            System.out.println(e);
        }
    }
}
