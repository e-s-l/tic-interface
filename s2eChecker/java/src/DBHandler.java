
/*
* Java Database connectivity (JDBC) comes natively with JDK/JRE so let's use this.
* Having said that, we will need a mysql driver, however.
*
* */

import java.sql.*;

public class DBHandler {

    private Connection connection;

    // Constructor:
    public DBHandler() {

        try {
            // Register Driver
            Class.forName("com.mysql.cj.jdbcDriver");

            // Create Connection
            String jdbc_url = "jdbc:mysql://127.0.0.1:3306/dbname";
            String usr = "root";
            String pw = "password";
            connection = DriverManager.getConnection(jdbc_url,usr,pw);


            // Close Connection:
            connection.close();


        } catch (Exception e) {
            System.out.println("ERROR IN DB HANDLER CONSTRUCTOR! \n" + e.getMessage());
        }
    }


    // Push value to db:
    public void saveValue(Double value) {

        try {
            // Create Statement
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM table";
            // Execute Query:
            ResultSet results = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("ERROR IN SQL! \n" + e.getMessage());
        }

    }

}
