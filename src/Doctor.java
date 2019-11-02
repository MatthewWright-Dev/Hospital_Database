public class Doctor extends Employee{
    boolean admitting;
    boolean consulting;

    public Doctor(){}

    public Doctor(String f, String l, boolean ad, boolean con)  {
        admitting = ad;
        consulting = con;
        firstName = f;
        lastName = l;
    }

}
