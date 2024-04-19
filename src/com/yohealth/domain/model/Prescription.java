package com.yohealth.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import com.yohealth.utils.DateUtils;

public class Prescription {
    public String name;
    public String dose;
    public Date startDate;
    public long id;
    public long patientID;

    public Prescription(String name, String dose, Date startDate, long id, long patientID)
    {
        this.name = name;
        this.dose = dose;
        this.startDate = startDate;
        this.id = id;
        this.patientID = patientID;
    }
    public Prescription(String name, String dose, Date startDate)
    {
        this.name = name;
        this.dose = dose;
        this.startDate = startDate; 
    }
    public Prescription(){
        // empty constructor :)
    }

     public static Collection<Prescription> niceStringToPrescriptions(String niceString) {
        Collection<Prescription> prescriptions = new ArrayList<>();

        // Split the niceString by line breaks
        String[] lines = niceString.split("\\r?\\n");

        // Iterate over each line
        for (int i = 0; i < lines.length; i += 3) {
            // Ensure that there are enough lines for a complete prescription
            if (i + 2 < lines.length) {
                String name = lines[i];// Extract name
                String dose = lines[i + 1].substring("Dose: ".length());  // Extract dose
                String startDate = lines[i + 2].substring("Start Date: ".length());  // Extract start date
                Date date = DateUtils.prettyStringToDate(startDate);

                // Create a new Prescription object and add it to the collection
                Prescription prescription = new Prescription(name, dose, date);
                prescriptions.add(prescription);
            }
        }

        return prescriptions;
    }

    public static String prescriptionsToNiceString(Collection<Prescription> prescriptions) {
        String niceString = "";
    for(Prescription prescription: prescriptions) { // iterate over each prescription
            
            niceString +=  prescription.name + "\n |";
            niceString += " Dose: " + prescription.dose + "\n |";
            niceString += " Start Date: " + prescription.startDate + "\n";
        }

        return niceString;
}
}
