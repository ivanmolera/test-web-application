package model.dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public interface UserDAO {

    List<User> findAllUsers();

    User findByUsername(String username);

    void add(User user) throws SQLException;

    void update(User user) throws SQLException;

    void delete(String username) throws SQLException;
}
