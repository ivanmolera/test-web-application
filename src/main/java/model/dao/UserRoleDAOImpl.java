package model.dao;

import constants.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class UserRoleDAOImpl implements UserRoleDAO {

    public List<String> findRolesByUsername(String username) {

        List<String> roles = new ArrayList<String>();

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            ResultSet rsUserRole = statement.executeQuery("SELECT * FROM UserRoles WHERE username = '" + username + "'");
            while(rsUserRole.next())
            {
                roles.add(rsUserRole.getString("role"));
            }
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
        return roles;
    }

    public void add(String username, String role) throws SQLException {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.execute("INSERT INTO UserRoles (username, role) VALUES ('" + username + "','" + role + "')");
        }
        catch(SQLException e)
        {
            throw e;
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

    public void deleteRolesByUsername(String username) throws SQLException {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.execute("DELETE FROM UserRoles WHERE username = '" + username + "'");
        }
        catch(SQLException e)
        {
            throw e;
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
