CREATE TABLE `Prescription` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255),
  `dose` VARCHAR(255),
  `startDate` DATETIME,
  `patientID` BIGINT,
  FOREIGN KEY (`patientID`) REFERENCES `User` (`id`)
);