CREATE TABLE `Visit` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `date` DATETIME,
  `reason` VARCHAR(255),
  `summary` TEXT,
  `evaluation` TEXT,
  `patientID` BIGINT,
  `doctorID` BIGINT,
  `weightInKg` FLOAT,
  `heightInCm` FLOAT,
  `temperatureInF` FLOAT,
  `bloodPressureSystolic` INT,
  `bloodPressureDiastolic` INT,
  FOREIGN KEY (`patientID`) REFERENCES `User` (`id`)
);