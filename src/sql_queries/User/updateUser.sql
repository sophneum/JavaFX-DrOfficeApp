UPDATE `User`
SET
    `name` = ?,
    `username` = ?,
    `roleID` = ?,
    `email` = ?,
    `phone` = ?,
    `emergencyContactPhone` = ?,
    `password` = ?,
    `mailingAddress` = ?,
    `billingAddress` = ?,
    `dob` = ?,
    `insuranceInfo` = ?,
    `pharmacyInfo` = ?,
    `allergies` = ?,
    `immunizations` = ?
WHERE `id` = ?;