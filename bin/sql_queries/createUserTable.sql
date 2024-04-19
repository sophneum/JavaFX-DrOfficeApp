CREATE TABLE `User` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `roleID` BIGINT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255),
  `emergencyContactPhone` VARCHAR(255),
  `password` VARCHAR(255) NOT NULL,
  `mailingAddress` VARCHAR(255),
  `billingAddress` VARCHAR(255),
  `dob` DATETIME,
  `insuranceInfo` VARCHAR(255),
  `pharmacyInfo` VARCHAR(255),
  `allergies` VARCHAR(255),
  `immunizations` VARCHAR(255),
  FOREIGN KEY (`roleID`) REFERENCES `Role` (`id`)
);