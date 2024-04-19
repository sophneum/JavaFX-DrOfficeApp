package com.yohealth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class DateUtils {
    

    public static java.sql.Date convertToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }


    public static String dateToPrettyString(Date date) {
        if(date == null) return null;
        String dateString ="";
        // Create a SimpleDateFormat object with the desired format
        // The input date is a datetime value like Thu Apr 04 22:21:46 PDT 2024
        // The output date is a string value like 2024-04-04
        // Print the input date format for testing
       // System.out.println(date);
       try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
      
             // Use the format() method to convert Date to String
        
            dateString = dateFormat.format(date);
            // print the output date format for testing
            // System.out.println("Format being returned yyyy-MM-dd : " + dateString);
       }
       catch(Exception e) {
       }

        return dateString;
    }

    public static Date prettyStringToDate(String dateString) {
        if (dateString == null) return null;
        
        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        try {
            // Parse the string to obtain a Date object
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing errors if necessary
            e.printStackTrace();
            return null;
        }

    }
}
