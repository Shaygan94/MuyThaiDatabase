# MuyThaiDatabase
A personal Java project inspired by the 2025 Java exam. It manages Muay Thai fighters, clubs, cities, and countries with file and database integration, featuring an admin menu for secure data management and input validation.

ThaiBoxingDatabase
This is a custom Java project inspired by the Java 2025 exam, designed to manage and organize Muay Thai (Thai boxing) data including countries, cities, clubs, and fighters. It demonstrates advanced handling of relational data with a clean, modular architecture.

Key Features
Comprehensive Data Model: Represents countries, cities, Muay Thai clubs, and fighters with fully linked relational tables.

Admin Menu Interface: Allows users to add new countries, cities, clubs, and fighters interactively through an admin menu.

File-Based Data Management: When adding or updating data, the program writes changes to existing text files or creates new ones when needed, ensuring data persistence outside the database.

Dynamic File-Based Data Import: Automatically reads from external files to populate and update the database, allowing easy data expansion without manual SQL inserts.

JDBC Integration: Uses JDBC with MySQL to connect and manipulate the database programmatically.

Maven Project Setup: Structured with Maven for dependency management and easy build.

Robust Data Integrity: Implements primary keys, unique constraints, and foreign key relationships to ensure data consistency.

Scalable Design: Easily extendable to support more features or data entities.

Getting Started
Database Setup
Run the SQL script thaiboxingdatabase.sql located in the root folder to create the database schema with all necessary tables and constraints. This file contains the database structure only (no preloaded data).

Create a db.properties file in the project root directory with the following content:
db.url=jdbc:mysql://localhost:3306/thaiboxingdatabase
db.user=YOUR_DB_USERNAME
db.password=YOUR_DB_PASSWORD
**Replace YOUR_DB_USERNAME and YOUR_DB_PASSWORD with your actual MySQL credentials.**

Project Setup
Open the folder named ThaiBoxingDatabase-Test in IntelliJ IDEA to ensure the project structure is correctly recognized.

The project uses JDBC MySQL Connector version 8.0.30. Please ensure your MySQL Workbench and MySQL Connector/J driver matches this version for compatibility.

The connector .jar file is included in the lib directory:
lib/mysql-connector-java-8.0.30.jar
