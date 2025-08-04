-- ===============================================
-- ThaiboxingDatabase - Complete Setup Script
-- ===============================================

-- Create database if it doesn't exist
DROP DATABASE IF EXISTS thaiboxingdatabase;
CREATE DATABASE thaiboxingdatabase;
USE thaiboxingdatabase;

-- Set MySQL configuration
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- ===============================================
-- Table structure for table `country`
-- ===============================================

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `codeCountry` varchar(3) NOT NULL,
  `nameCountry` varchar(45) DEFAULT NULL,
  `populationCountry` bigint DEFAULT NULL,
  `surfaceAreaCountry` bigint DEFAULT NULL,
  PRIMARY KEY (`codeCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===============================================
-- Table structure for table `city`
-- ===============================================

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `codeCity` varchar(3) NOT NULL,
  `nameCity` varchar(45) DEFAULT NULL,
  `populationCity` bigint DEFAULT NULL,
  `surfaceAreaCity` bigint DEFAULT NULL,
  `codeCountry` varchar(3) NOT NULL,
  PRIMARY KEY (`codeCity`),
  KEY `fk_city_country` (`codeCountry`),
  CONSTRAINT `fk_city_country` FOREIGN KEY (`codeCountry`) REFERENCES `country` (`codeCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===============================================
-- Table structure for table `thaiboxingclub`
-- ===============================================

DROP TABLE IF EXISTS `thaiboxingclub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thaiboxingclub` (
  `idClub` int NOT NULL AUTO_INCREMENT,
  `clubName` varchar(45) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `EstablishedYear` year DEFAULT NULL,
  `Owner` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idClub`),
  UNIQUE KEY `unique_club` (`clubName`,`address`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===============================================
-- Table structure for table `clubs_in_cities`
-- ===============================================

DROP TABLE IF EXISTS `clubs_in_cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clubs_in_cities` (
  `idClub` int NOT NULL,
  `codeCity` varchar(45) NOT NULL,
  PRIMARY KEY (`codeCity`,`idClub`),
  KEY `idClub_FK_idx` (`idClub`),
  CONSTRAINT `codeCity_FK` FOREIGN KEY (`codeCity`) REFERENCES `city` (`codeCity`),
  CONSTRAINT `idClub_FK` FOREIGN KEY (`idClub`) REFERENCES `thaiboxingclub` (`idClub`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===============================================
-- Table structure for table `fighter`
-- ===============================================

DROP TABLE IF EXISTS `fighter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fighter` (
  `id_Fighter` int NOT NULL AUTO_INCREMENT,
  `IdClub` int NOT NULL,
  `fighterName` varchar(80) NOT NULL,
  `fighterStyle` varchar(45) DEFAULT NULL,
  `height_in_cm` int DEFAULT NULL,
  `weight_in_kg` int DEFAULT NULL,
  `age` date DEFAULT NULL,
  `codeCountry` varchar(3) NOT NULL,
  PRIMARY KEY (`id_Fighter`),
  UNIQUE KEY `unique_fighter_name_age` (`fighterName`,`age`),
  KEY `fk_Fighter_ThaiboxingClub1_idx` (`IdClub`),
  KEY `fk_fighter_country` (`codeCountry`),
  CONSTRAINT `fk_fighter_country` FOREIGN KEY (`codeCountry`) REFERENCES `country` (`codeCountry`),
  CONSTRAINT `fk_Fighter_ThaiboxingClub1` FOREIGN KEY (`IdClub`) REFERENCES `thaiboxingclub` (`idClub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

-- ===============================================
-- Restore MySQL settings
-- ===============================================

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Script completed successfully!
-- Database 'thaiboxingdatabase' created with:
-- - Fighter IDs starting from 1
-- - Club IDs starting from 1000
-- - All foreign key relationships intact
