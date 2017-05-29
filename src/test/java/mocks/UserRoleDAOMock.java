package mocks;

import constants.Roles;
import model.dao.UserRoleDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class UserRoleDAOMock implements UserRoleDAO {

    public List<String> findRolesByUsername(String username) {
        List<String> roles = new ArrayList<String>();
        roles.add(Roles.PAGE_ONE.getRole());
        roles.add(Roles.PAGE_TWO.getRole());
        return roles;
    }

    public void add(String username, String role) throws SQLException { }

    public void deleteRolesByUsername(String username) throws SQLException { }
}
