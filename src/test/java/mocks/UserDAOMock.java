package mocks;

import constants.Roles;
import model.User;
import model.dao.UserDAO;
import util.PasswordManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanmolera on 28/5/17.
 */
public class UserDAOMock implements UserDAO {

    private boolean findCalled;
    private boolean addCalled;
    private boolean updateCalled;
    private boolean deleteCalled;

    public boolean findIsCalled()
    {
        return findCalled;
    }
    public boolean addIsCalled()
    {
        return addCalled;
    }
    public boolean updateIsCalled()
    {
        return updateCalled;
    }
    public boolean deleteIsCalled()
    {
        return deleteCalled;
    }

    private boolean isAdmin;
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public UserDAOMock() {
        resetCallers();
    }

    public void resetCallers() {
        findCalled = false;
        addCalled = false;
        updateCalled = false;
        deleteCalled = false;
        isAdmin = false;
    }

    public User findByUsername(String username) {
        findCalled = true;
        return createDummyUser(username);
    }

    public List<User> findAllUsers() {
        return new ArrayList<User>();
    }

    public void add(User user) throws SQLException { addCalled = true; }

    public void update(User user) throws SQLException { updateCalled = true; }

    public void delete(String username) throws SQLException { deleteCalled = true; }

    public User createDummyUser() {
        return createDummyUser("user99");
    }

    public User createDummyUser(String username) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(PasswordManager.encrypt("9999"));

        List<String> roles = new ArrayList<String>();
        roles.add(Roles.PAGE_ONE.getRole());

        if(this.isAdmin) roles.add(Roles.ADMIN.getRole());

        user.setRoles(roles);

        return user;
    }
}
