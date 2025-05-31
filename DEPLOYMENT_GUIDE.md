# HRMS-SSM Deployment Guide

This guide provides instructions for deploying the Human Resource Management System (HRMS-SSM) application.

## 1. Prerequisites

Ensure the following software is installed on your system:

-   **JDK:** Version 17 or higher.
-   **Apache Maven:** Version 3.6 or higher (ideally 3.8+ for better Java 17 support).
-   **Apache Tomcat:** Version 10.0.x or higher (to support Jakarta EE 9 / Servlet 5.0).
-   **MySQL Server:** Version 8.x recommended.
-   **IDE (Optional):**
    -   IntelliJ IDEA (Community or Ultimate)
    -   Eclipse IDE for Enterprise Java and Web Developers

## 2. Database Setup

The application requires a MySQL database named `hrms_db`.

1.  **Create the Database:**
    Connect to your MySQL server and run the following command:
    ```sql
    CREATE DATABASE hrms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```

2.  **Create Tables (DDL):**
    Use the `hrms_db` database and run the following SQL scripts to create the necessary tables:
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
        password VARCHAR(255) NOT NULL -- Note: Passwords stored in plain text for this version.
    );
    ```

3.  **Create Default Admin User (DML):**
    Insert a default administrator account:
    ```sql
    INSERT INTO users (username, password) VALUES ('admin', 'admin');
    ```
    The default credentials are `username: admin`, `password: admin`.

## 3. Project Setup

1.  **Clone the Repository:**
    ```bash
    git clone <repository_url>
    cd hrms-ssm
    ```
    (Replace `<repository_url>` with the actual URL of the Git repository)

2.  **Import into IDE (Optional):**
    -   **IntelliJ IDEA:**
        -   Select `File > Open...` or `Import Project...`.
        -   Navigate to the cloned `hrms-ssm` directory and select the `pom.xml` file.
        -   Choose "Open as Project".
        -   Ensure IntelliJ recognizes it as a Maven project and configures the JDK.
    -   **Eclipse IDE:**
        -   Select `File > Import...`.
        -   Choose `Maven > Existing Maven Projects`.
        -   Click `Next`, then `Browse...` to the cloned `hrms-ssm` directory.
        -   Ensure the `pom.xml` is selected and click `Finish`.

## 4. Configuration

-   **Database Connection:**
    You must configure the database connection details for your local MySQL instance.
    Edit the file: `src/main/resources/db.properties`

    Update the `db.username` and `db.password` properties with your MySQL credentials.
    The file should look like this:
    ```properties
    db.driverClassName=com.mysql.cj.jdbc.Driver
    db.url=jdbc:mysql://localhost:3306/hrms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    db.username=your_mysql_user
    db.password=your_mysql_password
    ```
    Replace `your_mysql_user` and `your_mysql_password` accordingly.

## 5. Building the Project

1.  **Using Maven:**
    Open a terminal or command prompt, navigate to the root directory of the project (where `pom.xml` is located), and run:
    ```bash
    mvn clean package
    ```
    Alternatively, you can use `mvn clean install` if you want to install the artifact into your local Maven repository.

2.  **Output WAR File:**
    After a successful build, the Web Application Archive (WAR) file will be generated in the `target/` directory. The file will be named `hrms-ssm-1.0-SNAPSHOT.war`.

## 6. Deployment to Apache Tomcat

1.  **Copy WAR to Tomcat `webapps/` Directory:**
    -   Ensure your Apache Tomcat server is running.
    -   Copy the `hrms-ssm-1.0-SNAPSHOT.war` file from the project's `target/` directory to the `<TOMCAT_HOME>/webapps/` directory (where `<TOMCAT_HOME>` is the root directory of your Tomcat installation).
    -   Tomcat will automatically detect and deploy the application. The application will usually be accessible at a context path matching the WAR file name (e.g., `/hrms-ssm-1.0-SNAPSHOT`).

2.  **Alternative: IDE Deployment (IntelliJ IDEA / Eclipse):**
    -   Most IDEs offer integration with application servers like Tomcat.
    -   You can configure a Tomcat server within your IDE and add the `hrms-ssm` project (or its WAR artifact) to the server for deployment.
    -   This method often provides easier debugging and hot-swapping capabilities. Refer to your IDE's documentation for specific instructions on configuring Tomcat and deploying web applications.

## 7. Accessing the Application

1.  **Start Tomcat:** If not already running, start your Apache Tomcat server.
2.  **Open in Browser:**
    Open your web browser and navigate to:
    `http://localhost:8080/hrms-ssm-1.0-SNAPSHOT/`

    *Note: The port `8080` is the default Tomcat port. If your Tomcat is configured on a different port, use that port number. The context path `/hrms-ssm-1.0-SNAPSHOT/` is derived from the WAR file name. If you rename the WAR file (e.g., to `hrms.war`) or deploy with a custom context path in Tomcat, adjust the URL accordingly.*

3.  **Login:**
    Use the default administrator credentials:
    -   **Username:** `admin`
    -   **Password:** `admin`

## 8. Usage Guide (Key Features)

-   **Login/Logout:** Secure access to the system. The root path `/` or `/login` takes you to the login page. Logout functionality is available.
-   **Employee Management:**
    -   View a list of all employees, with search functionality by employee name.
    -   Add new employees with details like name, gender, age, contact information, and assigned position.
    -   Edit existing employee information.
    -   Delete employees from the system.
-   **Position Management:**
    -   View a list of all job positions.
    -   Add new positions with a name and level.
    -   Edit existing position details.
    -   Delete positions (ensure no employees are currently assigned to a position before deletion if referential integrity is critical).
-   **User Management (Admin):**
    -   View a list of system users (administrators).
    -   Add new users with a username and password.
    -   Edit existing user details (e.g., username, password).
    -   Delete users.

## 9. (Optional) Project Structure Overview

-   `src/main/java`: Contains all Java source code.
    -   `com.example.controller`: Spring MVC controllers handling web requests.
    -   `com.example.service`: Service layer interfaces.
    -   `com.example.service.impl`: Service layer implementations, containing business logic.
    -   `com.example.dao`: Data Access Object (DAO) interfaces (MyBatis mappers).
    -   `com.example.model`: Plain Old Java Objects (POJOs) or "Beans" representing data entities.
    -   `com.example.filter`: Servlet filters, like the `AuthenticationFilter`.
-   `src/main/resources`: Contains configuration files.
    -   `applicationContext.xml`: Main Spring configuration (DataSource, MyBatis, Transactions, Service scanning).
    -   `spring-mvc.xml`: Spring MVC specific configuration (Controller scanning, ViewResolver, Static resources).
    -   `mybatis-config.xml`: MyBatis global configuration (Settings, Type Aliases).
    -   `com/example/mapper/`: MyBatis mapper XML files containing SQL statements.
    -   `db.properties`: Database connection details.
    -   `logback.xml` (or similar): Logging configuration (if present, not explicitly created in these steps but good practice).
-   `src/main/webapp`: Contains web application resources.
    -   `WEB-INF/jsp/`: JSP view files.
    -   `css/`: CSS stylesheets.
    -   `js/`: JavaScript files (if any).
    -   `img/`: Image files (if any).
    -   `WEB-INF/web.xml`: Web application deployment descriptor (Servlet/Filter configuration, Listeners, Welcome files).
-   `src/test/java`: Contains Java source code for unit and integration tests.
-   `src/test/resources`: Contains configuration files for tests.
-   `pom.xml`: Maven project configuration file (dependencies, build plugins, project metadata).

## 10. (Optional) Troubleshooting

-   **Application Not Starting / Errors on Startup:**
    -   Check Tomcat logs: `<TOMCAT_HOME>/logs/catalina.out` (or `localhost.<date>.log`, `manager.<date>.log`, etc.). These logs usually contain detailed error messages and stack traces.
-   **Database Connection Issues:**
    -   Verify that the `db.username` and `db.password` in `src/main/resources/db.properties` are correct for your MySQL setup.
    -   Ensure your MySQL server is running and accessible.
    -   Check that the `hrms_db` database exists.
    -   Confirm the JDBC URL in `db.properties` is correct (hostname, port).
-   **404 Errors for Application URL:**
    -   Ensure Tomcat is running.
    -   Verify the context path in the URL matches the deployed WAR file name (e.g., `/hrms-ssm-1.0-SNAPSHOT/`) or the custom context path you configured.
-   **404 Errors for CSS/JS:**
    -   Verify the `<mvc:resources ... />` mappings in `spring-mvc.xml` are correct.
    -   Ensure the `href` paths in your JSPs correctly use `${pageContext.request.contextPath}`.

This guide should help you get the HRMS-SSM application up and running.
If you encounter further issues, consult the detailed logs and verify each configuration step.
