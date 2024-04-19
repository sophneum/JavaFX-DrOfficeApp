INSERT INTO `User` (
    `name`, `username`, `roleID`, `email`, `password`,
    `phone`, `emergencyContactPhone`, `mailingAddress`, `billingAddress`, 
    `dob`, `insuranceInfo`, `pharmacyInfo`, `allergies`, `immunizations`
)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);