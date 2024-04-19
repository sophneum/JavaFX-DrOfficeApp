INSERT INTO `Visit` (
    `date`, `reason`, `summary`, `evaluation`,
    `patientID`, `doctorID`, `weightInKg`, `heightInCm`, 
    `temperatureInF`, `bloodPressureSystolic`, `bloodPressureDiastolic`
)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);