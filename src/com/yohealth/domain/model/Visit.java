package com.yohealth.domain.model;

import java.util.Date;

public class Visit {
    public long id;
    public Date date;
    public String reason;
    public String summary;
    public String evaluation;
    public long patientID;
    public long doctorID;
    public float weightInKg;
    public float heightInCm;
    public float temperatureInF;
    public int bloodPressureSystolic;
    public int bloodPressureDiastolic;
}
