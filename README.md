# HRMS (Human Resource Management System) - JSP+Servlet+JavaBean Version

## 1. Introduction

This project is a Human Resource Management System (HRMS) developed using Java Servlets, JavaServer Pages (JSPs), and JavaBeans, following the Model 2 (MVC-like) architecture. It serves as an example project for teaching web development with these Java technologies. The application covers basic HR functionalities like employee management, position management, and user administration.

## 2. Prerequisites

Before you begin, ensure you have the following installed:

*   **Java Development Kit (JDK):** Version 8 or later (e.g., OpenJDK, Oracle JDK).
*   **Apache Tomcat:** Version 9 or 10 is recommended.
*   **MySQL Server:** Version 8 is recommended.
*   **MySQL JDBC Driver:** The JAR file for connecting Java to MySQL (e.g., `mysql-connector-java-8.x.x.jar`).
*   **JSTL JARs:** `jstl-api-1.2.jar` and `jstl-impl-1.2.jar` (or `jakarta.servlet.jsp.jstl-api-2.0.0.jar` and `jakarta.servlet.jsp.jstl-2.0.0.jar` for Tomcat 10+ / Jakarta EE 9+).
*   **IDE (Optional but Recommended):** An Integrated Development Environment like IntelliJ IDEA or Eclipse.

## 3. Database Setup

1.  **Create the Database:**
    Open your MySQL client (e.g., MySQL Command Line, phpMyAdmin, MySQL Workbench) and run the following command to create the database:
    ```sql
    CREATE DATABASE hrms_db;
    ```

2.  **Create Tables:**
    This project assumes the necessary tables (`employees`, `positions`, `users`) already exist in the `hrms_db` database. If you are starting with an empty database, you will need to create these tables manually.

    **Example DDL (Illustrative - adapt data types and constraints as needed):**
    ```sql
    USE hrms_db;

    CREATE TABLE positions (
        id INT AUTO_INCREMENT PRIMARY KEY,
        position_name VARCHAR(255) NOT NULL,
        level INT
    );

    CREATE TABLE employees (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        gender VARCHAR(10),
        age INT,
        phone VARCHAR(20),
        email VARCHAR(255),
        position_id INT,
        FOREIGN KEY (position_id) REFERENCES positions(id)
    );

    CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL
    );
    ```
    *Note: You might need to add initial data (e.g., a default admin user) to the `users` table to log in for the first time.*
    ```sql
    -- Example: Add a default admin user (password: admin)
    INSERT INTO users (username, password) VALUES ('admin', 'admin');
    ```

## 4. Project Setup

1.  **Clone the Repository:**
    ```bash
    git clone <repository_url>
    cd <repository_directory_name>
    ```
    (Replace `<repository_url>` and `<repository_directory_name>` with actual values).

2.  **Import into IDE (Optional):**
    *   **IntelliJ IDEA:** `File > Open...` and select the cloned project directory.
    *   **Eclipse:** `File > Import... > General > Existing Projects into Workspace`, then select the root directory of the cloned project.

## 5. Configuration

1.  **Database Connection:**
    *   Open the file: `src/main/java/com/example/util/DatabaseConnection.java`.
    *   Review and update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` constants to match your local MySQL setup:
        ```java
        private static final String DB_URL = "jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC";
        private static final String DB_USER = "your_mysql_user"; // Replace with your MySQL username
        private static final String DB_PASSWORD = "your_mysql_password"; // Replace with your MySQL password
        ```

2.  **MySQL JDBC Driver:**
    *   Download the MySQL JDBC Driver JAR file (e.g., search for "mysql connector j jar download"). It's usually named something like `mysql-connector-java-8.x.x.jar`.
    *   Copy this JAR file to your Apache Tomcat's `lib` directory (e.g., `<TOMCAT_HOME>/lib/`). This makes the driver available to all web applications deployed on Tomcat.

3.  **JSTL Libraries:**
    *   Download `jstl-api-1.2.jar` and `jstl-impl-1.2.jar`. (For Tomcat 10+ using Jakarta EE 9+, you'll need `jakarta.servlet.jsp.jstl-api-2.0.0.jar` and `jakarta.servlet.jsp.jstl-2.0.0.jar` or newer versions like 3.0.x).
    *   Ensure the directory `src/main/webapp/WEB-INF/lib` exists in your project. If not, create it.
    *   Place the two downloaded JSTL JARs into this `src/main/webapp/WEB-INF/lib/` directory. This bundles them with your web application.

## 6. Build

This project is structured to be deployed from its source files. The Java `.class` files need to be compiled from `src/main/java` and placed into `src/main/webapp/WEB-INF/classes`.

*   **Using an IDE (Recommended):** Most IDEs like IntelliJ IDEA or Eclipse will handle the compilation and placement of class files automatically when you configure and run the project on Tomcat.
    *   Ensure your IDE's project settings are configured to output compiled Java classes to the correct `WEB-INF/classes` directory within the web application structure that Tomcat will use.
*   **Manual Compilation (Advanced):**
    If compiling manually using `javac`, you would compile files from `src/main/java/**/*.java` and place the resulting `.class` files into `src/main/webapp/WEB-INF/classes/`, maintaining the package structure. You'd also need to include necessary libraries (like Servlet API, JSTL API) in your classpath during compilation.

## 7. Deployment to Apache Tomcat

1.  **Configure Tomcat in your IDE (Easiest Method):**
    *   **IntelliJ IDEA:** Add a Tomcat Server configuration (`Run > Edit Configurations... > Add New Configuration > Tomcat Server > Local`). Set the Tomcat home directory. In the 'Deployment' tab, add your project as an artifact (often an "exploded WAR").
    *   **Eclipse:** Add Tomcat to the 'Servers' view (`Window > Show View > Servers`). Right-click in Servers view > `New > Server`. Choose your Tomcat version and point to its installation directory. Then, right-click the server and choose `Add and Remove...` to deploy your project.
    *   When using an IDE, it typically handles copying the project structure (including JSPs, CSS, `WEB-INF/lib` with JSTL JARs, and `WEB-INF/classes` with compiled Java code) to a Tomcat deployment directory.

2.  **Manual Deployment (Alternative):**
    *   Ensure all Java files are compiled into `src/main/webapp/WEB-INF/classes/`.
    *   Copy the entire `src/main/webapp` directory (or a directory containing all its contents like JSPs, CSS, WEB-INF) to Tomcat's `webapps/` directory. Rename the copied folder to your desired application context name (e.g., `hrms`).
    *   **Example:** If you copy the contents of `src/main/webapp` into `webapps/hrms`, your application will be accessible at `/hrms`.

## 8. Accessing the Application

1.  **Start Apache Tomcat Server** (either from your IDE or by running `<TOMCAT_HOME>/bin/startup.sh` or `startup.bat`).
2.  Open a web browser and navigate to:
    `http://localhost:8080/<your_application_context_name>/`
    (e.g., `http://localhost:8080/hrms/` if you named your context `hrms`).
3.  The application should load, and you should see the login page. Use the credentials you set up in the database (e.g., `admin`/`admin` if you used the example insert).

## 9. Troubleshooting Tips

*   **Check Tomcat Logs:** Look in `<TOMCAT_HOME>/logs/` (especially `catalina.out` or `localhost.<date>.log`) for error messages.
*   **HTTP 404 Error:**
    *   Verify the URL and application context path.
    *   Ensure Tomcat is running and the application is deployed correctly.
    *   Check `web.xml` for correct servlet mappings.
*   **`ClassNotFoundException`:**
    *   **JDBC Driver:** Ensure `mysql-connector-java-x.x.x.jar` is in Tomcat's `lib` directory.
    *   **JSTL JARs:** Ensure `jstl-api.jar` and `jstl-impl.jar` (or Jakarta equivalents) are in `src/main/webapp/WEB-INF/lib/`.
    *   **Your Classes:** Ensure your Java classes are compiled and present in `src/main/webapp/WEB-INF/classes/` with correct package structure.
*   **`SQLException` / Database Connection Issues:**
    *   Verify database connection details in `DatabaseConnection.java`.
    *   Ensure your MySQL server is running and accessible.
    *   Check that the `hrms_db` database and required tables exist.
*   **JSP Compilation Errors:** Often indicated by error messages on the web page itself. Can be due to incorrect JSTL tags, EL syntax, or missing JSTL libraries.
*   **Browser Developer Console:** Check for client-side errors (JavaScript, CSS loading issues).

Good luck!
