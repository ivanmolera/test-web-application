package model.dao;

import model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public interface UserRoleDAO {

    List<String> findRolesByUsername(String username);

    void add(String username, String role) throws SQLException;

    void deleteRolesByUsername(String username) throws SQLException;
}
