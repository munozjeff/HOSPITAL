-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: clinicadb
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `citas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paciente_id` int(11) DEFAULT NULL,
  `medico_id` int(11) DEFAULT NULL,
  `fecha_hora` datetime NOT NULL,
  `motivo` varchar(255) DEFAULT NULL,
  `estado` enum('pendiente','completada','cancelada') DEFAULT 'pendiente',
  PRIMARY KEY (`id`),
  KEY `paciente_id` (`paciente_id`),
  KEY `medico_id` (`medico_id`),
  CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`paciente_id`) REFERENCES `pacientes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `citas_ibfk_2` FOREIGN KEY (`medico_id`) REFERENCES `medicos` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (76,1,1,'2024-11-01 09:00:00','1','completada'),(77,2,2,'2024-11-02 10:30:00','2','pendiente'),(78,3,3,'2024-11-03 11:00:00','3','cancelada'),(79,4,4,'2024-11-04 08:45:00','2','completada'),(80,5,5,'2024-11-05 12:15:00','3','pendiente'),(81,6,6,'2024-11-06 14:00:00','2','completada'),(82,7,7,'2024-11-07 15:30:00','3','cancelada'),(83,8,8,'2024-11-08 10:00:00','2','pendiente'),(84,9,9,'2024-11-09 11:45:00','1','completada'),(85,10,10,'2024-11-10 13:15:00','3','pendiente'),(86,11,11,'2024-11-11 09:00:00','1','completada'),(87,12,12,'2024-11-12 10:30:00','3','pendiente'),(88,13,13,'2024-11-13 08:00:00','2','completada'),(89,14,14,'2024-11-14 12:45:00','3','cancelada'),(90,15,15,'2024-11-15 14:00:00','1','completada'),(91,16,16,'2024-11-16 16:00:00','3','pendiente'),(92,17,17,'2024-11-17 09:15:00','3','completada'),(93,18,18,'2024-11-18 10:45:00','2','cancelada'),(94,19,19,'2024-11-19 13:30:00','3','pendiente'),(95,20,20,'2024-11-20 11:00:00','1','completada'),(96,21,21,'2024-11-21 15:00:00','3','pendiente'),(97,22,22,'2024-11-22 08:45:00','1','completada'),(98,23,23,'2024-11-23 14:30:00','3','pendiente'),(99,24,24,'2024-11-24 09:45:00','3','cancelada'),(100,25,25,'2024-11-25 13:45:00','2','completada'),(125,1,6,'2024-11-01 21:28:00','1','pendiente'),(132,1,21,'2024-11-09 08:04:00','2','pendiente'),(133,1,9,'2024-11-30 10:01:00','3','pendiente');
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosticos`
--

DROP TABLE IF EXISTS `diagnosticos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagnosticos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cita_id` int(11) DEFAULT NULL,
  `diagnostico` text NOT NULL,
  `tratamiento` text DEFAULT NULL,
  `fecha_diagnostico` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `cita_id` (`cita_id`),
  CONSTRAINT `diagnosticos_ibfk_1` FOREIGN KEY (`cita_id`) REFERENCES `citas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosticos`
--

LOCK TABLES `diagnosticos` WRITE;
/*!40000 ALTER TABLE `diagnosticos` DISABLE KEYS */;
INSERT INTO `diagnosticos` VALUES (51,76,'Hipertensi?n arterial','Control de la presi?n arterial','2024-11-05 20:06:30'),(52,77,'Dermatitis at?pica','Aplicaci?n de cremas t?picas','2024-11-05 20:06:30'),(53,78,'Infecci?n respiratoria','Antibi?ticos y descanso','2024-11-05 20:06:30'),(54,79,'Infecci?n urinaria','Antibi?ticos','2024-11-05 20:06:30'),(55,80,'Cataratas','Cirug?a de cataratas','2024-11-05 20:06:30'),(56,81,'Gastritis','Medicamentos anti?cidos','2024-11-05 20:06:30'),(57,82,'Diabetes tipo 2','Control de la glucosa y dieta','2024-11-05 20:06:30'),(58,83,'Neoplasia benigna','Control y seguimiento','2024-11-05 20:06:30'),(59,84,'C?ncer de mama','Quimioterapia','2024-11-05 20:06:30'),(60,85,'Fibromialgia','Terapia f?sica y medicamentos','2024-11-05 20:06:30'),(61,86,'Depresi?n','Terapia psicol?gica y antidepresivos','2024-11-05 20:06:30'),(62,87,'Insuficiencia renal','Di?lisis y tratamiento m?dico','2024-11-05 20:06:30'),(63,88,'Hernia discal','Fisioterapia y medicaci?n','2024-11-05 20:06:30'),(64,89,'Fractura de mu?eca','Inmovilizaci?n y rehabilitaci?n','2024-11-05 20:06:30'),(65,90,'Anemia','Suplementos de hierro y dieta','2024-11-05 20:06:30'),(66,91,'Sinusitis','Descongestionantes y analg?sicos','2024-11-05 20:06:30'),(67,92,'Asma','Inhaladores y control ambiental','2024-11-05 20:06:30'),(68,93,'COVID-19','Aislamiento y tratamiento sintom?tico','2024-11-05 20:06:30'),(69,94,'Alergia alimentaria','Evitar al?rgenos y antihistam?nicos','2024-11-05 20:06:30'),(70,95,'C?lico nefr?tico','Hidrataci?n y analg?sicos','2024-11-05 20:06:30'),(71,96,'Artritis','Medicamentos antiinflamatorios','2024-11-05 20:06:30'),(72,97,'Gripe','Reposo e hidrataci?n','2024-11-05 20:06:30'),(73,98,'Esguince de tobillo','Inmovilizaci?n y reposo','2024-11-05 20:06:30'),(74,99,'C?ncer de pr?stata','Tratamiento hormonal y seguimiento','2024-11-05 20:06:30'),(75,100,'Esclerosis m?ltiple','Tratamiento sintom?tico y seguimiento','2024-11-05 20:06:30');
/*!40000 ALTER TABLE `diagnosticos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `especialidades`
--

DROP TABLE IF EXISTS `especialidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `especialidades` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `especialidades`
--

LOCK TABLES `especialidades` WRITE;
/*!40000 ALTER TABLE `especialidades` DISABLE KEYS */;
INSERT INTO `especialidades` VALUES (1,'Medico General'),(2,'Pediatria'),(3,'Dermatologia');
/*!40000 ALTER TABLE `especialidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicos`
--

DROP TABLE IF EXISTS `medicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `medicos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `fecha_contratacion` date NOT NULL,
  `especialidad_id` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `fk_especialidad` (`especialidad_id`),
  CONSTRAINT `fk_especialidad` FOREIGN KEY (`especialidad_id`) REFERENCES `especialidades` (`id`) ON DELETE CASCADE,
  CONSTRAINT `medicos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicos`
--

LOCK TABLES `medicos` WRITE;
/*!40000 ALTER TABLE `medicos` DISABLE KEYS */;
INSERT INTO `medicos` VALUES (1,26,'5552001','2015-04-15',1),(2,27,'5552002','2017-08-01',2),(3,28,'5552003','2016-02-12',2),(4,29,'5552004','2014-06-20',2),(5,30,'5552005','2018-03-05',2),(6,31,'5552006','2016-07-10',1),(7,32,'5552007','2019-01-22',1),(8,33,'5552008','2013-09-14',1),(9,34,'5552009','2017-05-25',3),(10,35,'5552010','2015-08-30',3),(11,36,'5552011','2012-11-07',3),(12,37,'5552012','2016-12-17',3),(13,38,'5552013','2014-04-12',3),(14,39,'5552014','2018-10-15',1),(15,40,'5552015','2013-06-29',2),(16,41,'5552016','2015-01-11',2),(17,42,'5552017','2017-09-23',1),(18,43,'5552018','2019-02-03',2),(19,44,'5552019','2014-11-08',1),(20,45,'5552020','2016-08-22',3),(21,46,'5552021','2018-12-05',2),(22,47,'5552022','2017-07-27',1),(23,48,'5552023','2012-03-31',1),(24,49,'5552024','2019-06-18',1),(25,50,'5552025','2012-09-30',1);
/*!40000 ALTER TABLE `medicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pacientes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `genero` enum('masculino','femenino','otro') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `pacientes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (1,1,'1985-07-22','Calle 1','5551001','masculino'),(2,2,'1990-03-14','Calle 2','5551002','femenino'),(3,3,'1982-06-01','Calle 3','5551003','masculino'),(4,4,'1989-08-19','Calle 4','5551004','femenino'),(5,5,'1991-11-25','Calle 5','5551005','masculino'),(6,6,'1987-04-30','Calle 6','5551006','femenino'),(7,7,'1984-10-16','Calle 7','5551007','masculino'),(8,8,'1992-02-07','Calle 8','5551008','femenino'),(9,9,'1980-06-13','Calle 9','5551009','masculino'),(10,10,'1983-12-10','Calle 10','5551010','femenino'),(11,11,'1994-05-15','Calle 11','5551011','masculino'),(12,12,'1986-01-24','Calle 12','5551012','femenino'),(13,13,'1988-09-11','Calle 13','5551013','masculino'),(14,14,'1981-07-05','Calle 14','5551014','femenino'),(15,15,'1993-03-28','Calle 15','5551015','masculino'),(16,16,'1985-11-09','Calle 16','5551016','femenino'),(17,17,'1989-04-22','Calle 17','5551017','masculino'),(18,18,'1991-08-18','Calle 18','5551018','femenino'),(19,19,'1987-02-03','Calle 19','5551019','masculino'),(20,20,'1984-10-20','Calle 20','5551020','femenino'),(21,21,'1992-06-30','Calle 21','5551021','masculino'),(22,22,'1983-03-17','Calle 22','5551022','femenino'),(23,23,'1986-12-25','Calle 23','5551023','masculino'),(24,24,'1990-09-14','Calle 24','5551024','femenino'),(25,25,'1980-12-24','Calle 25','5551025','masculino');
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('paciente','medico','administrativo') NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan','Perez','juan.perez@example.com','password1','paciente','2024-11-05 19:43:28'),(2,'Ana','Gomez','ana.gomez@example.com','password2','paciente','2024-11-05 19:43:28'),(3,'Carlos','Sanchez','carlos.sanchez@example.com','password3','paciente','2024-11-05 19:43:28'),(4,'Lucia','Diaz','lucia.diaz@example.com','password4','paciente','2024-11-05 19:43:28'),(5,'Ricardo','Mendez','ricardo.mendez@example.com','password5','paciente','2024-11-05 19:43:28'),(6,'Sara','Lopez','sara.lopez@example.com','password6','paciente','2024-11-05 19:43:28'),(7,'Fernando','Castillo','fernando.castillo@example.com','password7','paciente','2024-11-05 19:43:28'),(8,'Laura','Jimenez','laura.jimenez@example.com','password8','paciente','2024-11-05 19:43:28'),(9,'Miguel','Romero','miguel.romero@example.com','password9','paciente','2024-11-05 19:43:28'),(10,'Claudia','Reyes','claudia.reyes@example.com','password10','paciente','2024-11-05 19:43:28'),(11,'Luis','Hernandez','luis.hernandez@example.com','password11','paciente','2024-11-05 19:43:28'),(12,'Patricia','Silva','patricia.silva@example.com','password12','paciente','2024-11-05 19:43:28'),(13,'Jorge','Ramos','jorge.ramos@example.com','password13','paciente','2024-11-05 19:43:28'),(14,'Veronica','Navarro','veronica.navarro@example.com','password14','paciente','2024-11-05 19:43:28'),(15,'Daniel','Flores','daniel.flores@example.com','password15','paciente','2024-11-05 19:43:28'),(16,'Monica','Ortega','monica.ortega@example.com','password16','paciente','2024-11-05 19:43:28'),(17,'Rafael','Garcia','rafael.garcia@example.com','password17','paciente','2024-11-05 19:43:28'),(18,'Adriana','Morales','adriana.morales@example.com','password18','paciente','2024-11-05 19:43:28'),(19,'Eduardo','Guzman','eduardo.guzman@example.com','password19','paciente','2024-11-05 19:43:28'),(20,'Sofia','Molina','sofia.molina@example.com','password20','paciente','2024-11-05 19:43:28'),(21,'Alberto','Cruz','alberto.cruz@example.com','password21','paciente','2024-11-05 19:43:28'),(22,'Isabel','Paredes','isabel.paredes@example.com','password22','paciente','2024-11-05 19:43:28'),(23,'Oscar','Vega','oscar.vega@example.com','password23','paciente','2024-11-05 19:43:28'),(24,'Gabriela','Rivas','gabriela.rivas@example.com','password24','paciente','2024-11-05 19:43:28'),(25,'Manuel','Torres','manuel.torres@example.com','password25','paciente','2024-11-05 19:43:28'),(26,'Jose','Martinez','jose.martinez@example.com','password26','medico','2024-11-05 19:43:28'),(27,'Paula','Suarez','paula.suarez@example.com','password27','medico','2024-11-05 19:43:28'),(28,'Andres','Delgado','andres.delgado@example.com','password28','medico','2024-11-05 19:43:28'),(29,'Carolina','Franco','carolina.franco@example.com','password29','medico','2024-11-05 19:43:28'),(30,'Diego','Lara','diego.lara@example.com','password30','medico','2024-11-05 19:43:28'),(31,'Ines','Mendoza','ines.mendoza@example.com','password31','medico','2024-11-05 19:43:28'),(32,'Guillermo','Vargas','guillermo.vargas@example.com','password32','medico','2024-11-05 19:43:28'),(33,'Daniela','Pe?a','daniela.pena@example.com','password33','medico','2024-11-05 19:43:28'),(34,'Martin','Cabrera','martin.cabrera@example.com','password34','medico','2024-11-05 19:43:28'),(35,'Beatriz','Rojas','beatriz.rojas@example.com','password35','medico','2024-11-05 19:43:28'),(36,'Hector','Leon','hector.leon@example.com','password36','medico','2024-11-05 19:43:28'),(37,'Marcela','Padilla','marcela.padilla@example.com','password37','medico','2024-11-05 19:43:28'),(38,'Pablo','Zambrano','pablo.zambrano@example.com','password38','medico','2024-11-05 19:43:28'),(39,'Alicia','Valencia','alicia.valencia@example.com','password39','medico','2024-11-05 19:43:28'),(40,'Tomas','Mora','tomas.mora@example.com','password40','medico','2024-11-05 19:43:28'),(41,'Silvia','Nieves','silvia.nieves@example.com','password41','medico','2024-11-05 19:43:28'),(42,'Cristian','Parra','cristian.parra@example.com','password42','medico','2024-11-05 19:43:28'),(43,'Gloria','Escobar','gloria.escobar@example.com','password43','medico','2024-11-05 19:43:28'),(44,'Felipe','Saenz','felipe.saenz@example.com','password44','medico','2024-11-05 19:43:28'),(45,'Nancy','Lemus','nancy.lemus@example.com','password45','medico','2024-11-05 19:43:28'),(46,'Victor','Munoz','victor.munoz@example.com','password46','medico','2024-11-05 19:43:28'),(47,'Elena','Montoya','elena.montoya@example.com','password47','medico','2024-11-05 19:43:28'),(48,'Javier','Fierro','javier.fierro@example.com','password48','medico','2024-11-05 19:43:28'),(49,'Susana','Tapia','susana.tapia@example.com','password49','medico','2024-11-05 19:43:28'),(50,'Roberto','Galeano','roberto.galeano@example.com','password50','medico','2024-11-05 19:43:28');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-27 10:45:17
