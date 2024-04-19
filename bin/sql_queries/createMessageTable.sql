CREATE TABLE `Message` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `subject` VARCHAR(255),
  `content` TEXT,
  `date` DATETIME,
  `patientID` BIGINT,
  FOREIGN KEY (`patientID`) REFERENCES `User` (`id`)
);