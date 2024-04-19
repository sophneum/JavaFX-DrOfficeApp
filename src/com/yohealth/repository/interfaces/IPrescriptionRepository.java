package com.yohealth.repository.interfaces;

import com.yohealth.domain.model.Prescription;
import java.util.Collection;

public interface IPrescriptionRepository {
    int createPrescription(Prescription prescription);
    Prescription getPrescriptionById(long prescriptionId);
    Collection<Prescription> getAllPrescriptionsByPatientId(long userId);
    int updatePrescription(Prescription prescription);
}
