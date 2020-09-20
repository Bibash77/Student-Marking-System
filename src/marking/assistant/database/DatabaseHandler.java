package marking.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * This class handles all the database related connection creation and query executing
 */
public class DatabaseHandler {

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection connection = null;
    private static Statement statement = null;
    private static DatabaseHandler databaseHandler = null;

    // initialize db connection and create connection in constructor
    private DatabaseHandler() {
        this.createDBConnection();
        setMarksTable();
    }

    // creates instance of DatabaseHandler if its not created
    public static DatabaseHandler getInstance(){
        if (databaseHandler ==  null){
         databaseHandler = new DatabaseHandler();
        }
        return databaseHandler;
    }

    // connects with db
    void createDBConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    // creates a marks table if it is not created
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

    // executes query and returns data
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }

    // execute query but returns operation in boolean form
    public boolean execAction(String qu) {
        try {
            statement = connection.createStatement();
            statement.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured",
                JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.getMessage() + ' ' + "Error Occured" +JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
    }

}
