UPDATE `Message`
SET
    `subject` = ?,
    `content`= ?,
    `date` = ?,
    `patientID` = ?,
WHERE `id` = ?;