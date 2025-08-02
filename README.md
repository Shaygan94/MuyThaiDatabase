# MuyThaiDatabase

A personal Java project inspired by the 2025 Java exam. It manages Muay Thai fighters, clubs, cities, and countries with file and database integration.
---

## 📄 About

The application reads and writes data related to Thai boxing: countries, cities, Muay Thai clubs, and fighters. It supports adding new entries interactively via an Admin Menu that **simultaneously updates both the MySQL database tables and corresponding text files** to maintain data persistence across both storage methods.

The backend uses JDBC to connect to a MySQL database, leveraging a structured relational schema with proper keys and constraints. The project is built with Maven for dependency management and uses the MySQL Connector/J version 8.0.30 for database connectivity.

---

## 💡 Key Features

- Interactive Admin Menu for adding countries, cities, clubs, and fighters.  
- Automatic update and creation of structured text files to reflect changes in the data.  
- Reads data from text files and imports into a MySQL database via JDBC.  
- Well-defined database schema with primary keys, foreign keys, and relational integrity.  
- Clean Maven project structure, easy to build and extend.  
- Uses MySQL Connector/J 8.0.30 to ensure compatibility with MySQL Workbench and the database.

---

## ⚙️ Technologies Used

- Java 17+  
- Maven  
- JDBC  
- MySQL Workbench  
- IntelliJ IDEA (recommended)  
- MySQL Connector/J 8.0.30  

---

## ▶️ How to Run

1. **Database setup:**  
   Run the SQL script `thaiboxingdatabase.sql` (located in the root folder) to create the database schema with all tables and relationships.  
   _Note: This script contains the database structure only — no data included._

2. **Project setup:**  
   Open the folder `ThaiBoxingDatabase-Test` in IntelliJ IDEA to ensure the correct project structure and file path resolution.

3. **Create the database connection properties:**  
   In the root directory, create a file named `db.properties` with the following content (replace placeholders with your actual credentials):

   db.url=jdbc:mysql://localhost:3306/thaiboxingdatabase

   db.user=YOUR_DB_USERNAME

   db.password=YOUR_DB_PASSWORD

Ensure compatible MySQL Connector:
The project uses MySQL Connector/J version 8.0.30, included in lib/mysql-connector-java-8.0.30.jar. Make sure your MySQL Workbench and connector driver match this version.

Run the application:
Launch the Main class in your IDE to start the program. Use the Admin Menu to manage data interactively.
The password for admin menu is admin123.

