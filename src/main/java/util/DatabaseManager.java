package util;

import constants.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();

    private DatabaseManager() {

    }

    public static DatabaseManager initDatabase() {
        createTables();
        setDefaultValues();
        return instance;
    }

    private static void createTables() {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.executeUpdate("DROP TABLE if EXISTS Users");
            statement.executeUpdate("DROP TABLE if EXISTS Roles");
            statement.executeUpdate("DROP TABLE if EXISTS UserRoles");

            statement.executeUpdate("CREATE TABLE Users (username string PRIMARY KEY, password string)");
            statement.executeUpdate("CREATE TABLE Roles (role string PRIMARY KEY)");
            statement.executeUpdate("CREATE TABLE UserRoles (username string, role string, PRIMARY KEY (username, role), FOREIGN KEY (username) REFERENCES Users(username) ON DELETE CASCADE)");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }

    private static void setDefaultValues() {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.executeUpdate("INSERT INTO Users VALUES('user01', '" + PasswordManager.encrypt("1111") + "')");
            statement.executeUpdate("INSERT INTO Users VALUES('user02', '" + PasswordManager.encrypt("2222") + "')");
            statement.executeUpdate("INSERT INTO Users VALUES('user03', '" + PasswordManager.encrypt("3333") + "')");
            statement.executeUpdate("INSERT INTO Users VALUES('admin', '" + PasswordManager.encrypt("admin") + "')");

            statement.executeUpdate("INSERT INTO Roles VALUES('PAGE_1')");
            statement.executeUpdate("INSERT INTO Roles VALUES('PAGE_2')");
            statement.executeUpdate("INSERT INTO Roles VALUES('PAGE_3')");
            statement.executeUpdate("INSERT INTO Roles VALUES('ADMIN')");

            statement.executeUpdate("INSERT INTO UserRoles VALUES('user01', 'PAGE_1')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('user02', 'PAGE_2')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('user03', 'PAGE_3')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('admin', 'ADMIN')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('admin', 'PAGE_1')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('admin', 'PAGE_2')");
            statement.executeUpdate("INSERT INTO UserRoles VALUES('admin', 'PAGE_3')");
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }
}
