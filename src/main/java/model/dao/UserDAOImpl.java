package model.dao;

import constants.Database;
import model.User;
import util.PasswordManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class UserDAOImpl implements UserDAO {

    public User findByUsername(String username) {
        User user = null;

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            UserRoleDAO userRoleDAO = new UserRoleDAOImpl();

            ResultSet rsUsers = statement.executeQuery("SELECT * FROM Users WHERE username = '" + username + "'");
            if(rsUsers.next()) {
                user = new User();
                user.setUsername(rsUsers.getString("username"));
                user.setPassword(rsUsers.getString("password"));

                List<String> roles = userRoleDAO.findRolesByUsername(username);
                user.setRoles(roles);
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
        return user;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<User>();

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            UserRoleDAO userRoleDAO = new UserRoleDAOImpl();

            ResultSet rsUsers = statement.executeQuery("SELECT * FROM Users");
            while(rsUsers.next())
            {
                String username = rsUsers.getString("username");
                String password = rsUsers.getString("password");

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                List<String> roles = userRoleDAO.findRolesByUsername(username);
                user.setRoles(roles);

                users.add(user);
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
        return users;
    }

    public void add(User user) throws SQLException {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.execute("INSERT INTO Users (username, password) VALUES ('" + user.getUsername() + "','" + PasswordManager.encrypt(user.getPassword()) + "')");

            if(user.getRoles() != null && user.getRoles().size() > 0) {
                for (String role : user.getRoles()) {
                    statement.execute("INSERT INTO UserRoles (username, role) VALUES ('" + user.getUsername() + "','" + role + "')");
                }
            }
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

    public void update(User user) throws SQLException {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.executeUpdate("UPDATE Users SET password = '" + user.getPassword() + "' WHERE username = '" + user.getUsername() + "'");
            statement.execute("DELETE FROM UserRoles WHERE username = '" + user.getUsername() + "'");

            if(user.getRoles() != null && user.getRoles().size() > 0) {
                for (String role : user.getRoles()) {
                    statement.execute("INSERT INTO UserRoles (username, role) VALUES ('" + user.getUsername() + "','" + role + "')");
                }
            }
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

    public void delete(String username) throws SQLException {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(Database.CONNECTION);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(Database.TIMEOUT);

            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("DELETE FROM Users WHERE username = '" + username + "'");
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
