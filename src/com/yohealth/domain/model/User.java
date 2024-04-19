package com.yohealth.domain.model;

import java.util.Collection;
import java.util.Date;

public class User {
    public long id;
    public String name;
    public String username;
    public long roleID;
    public Collection<Message> messages;
    public Collection<Visit> visits;
    public Collection<Prescription> prescriptions;
    public String email;
    public String phone;
    public String emergencyContactPhone;
    public String password;
    public String mailingAddress;
    public String billingAddress;
    public Date dob;
    public String insuranceInfo;
    public String pharmacyInfo;
    public String allergies;
    public String immunizations;
}
