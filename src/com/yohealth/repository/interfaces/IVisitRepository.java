package com.yohealth.repository.interfaces;

import com.yohealth.domain.model.Visit;
import java.util.Collection;

public interface IVisitRepository { //defines interface
    int createVisit(Visit visit);
    Visit getVisitByID(long userID);
    Collection<Visit> getAllVisitsByPatientID(long userID);
    int updateVisit(Visit visit);
}