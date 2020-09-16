package marking.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection connection = null;
    private static Statement statement = null;

    public DatabaseHandler() {
        this.createDBConnection();
        setMarksTable();
    }

    void createDBConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    void setMarksTable() {
        String TABLE_NAME = "Marks";
        try {
            statement = connection.createStatement();
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("table " + TABLE_NAME + "  already exist. ");
            } else {
                statement.execute("CREATE TABLE MARKS\n"
                    + "(\n"
                    + "STUDENTID VARCHAR (8) NOT NULL,\n"
                    + " ASSIGNMENT1 INT NOT NULL,\n"
                    + " ASSIGNMENT2 INT NOT NULL,\n"
                    + " EXAM INT NOT NULL,\n"
                    + " TOTAL INT NOT NULL,\n"
                    + "GRADE VARCHAR (4) NOT NULL,\n"
                    + "PRIMARY KEY (STUDENTID)\n"
                    + ")\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
