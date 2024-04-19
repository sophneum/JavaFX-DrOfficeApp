UPDATE `Prescription`
SET 
    `name` = ?,
    `dose` = ?,
    `startDate` = ?,
    `patientID` = ?
WHERE `id` = ?;