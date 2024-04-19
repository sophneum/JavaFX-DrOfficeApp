UPDATE `Visit`
SET
    `date` = ?,
    `reason` = ?,
    `summary` = ?,
    `evaluation` = ?,
    `patientID` = ?,
    `doctorID` = ?,
    `weightInKg` = ?,
    `heightInCm` = ?,
    `temperatureInF` = ?,
    `bloodPressureSystolic` = ?,
    `bloodPressureDiastolic` = ?
WHERE `id` = ?;