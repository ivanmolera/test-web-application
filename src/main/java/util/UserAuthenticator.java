package util;

import com.sun.net.httpserver.BasicAuthenticator;
import model.User;
import model.dao.UserDAO;
import model.dao.UserDAOImpl;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class UserAuthenticator extends BasicAuthenticator {

    public UserAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String user, String pwd) {
        UserDAO userDao = new UserDAOImpl();
        User myUser = userDao.findByUsername(user);

        String hash = PasswordManager.encrypt(pwd);

        return hash.equals(myUser.getPassword());
    }
}
