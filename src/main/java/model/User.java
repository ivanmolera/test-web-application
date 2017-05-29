package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import constants.Roles;
import util.Views;

import java.util.List;

/**
 * Created by ivanmolera on 27/5/17.
 */
public class User {

    @JsonProperty("username")
    @JsonView(Views.Normal.class)
    private String username;

    @JsonProperty("password")
    @JsonView(Views.Admin.class)
    private String password;

    @JsonProperty("roles")
    @JsonView(Views.Normal.class)
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean hasAccessToPage(int page) {
        boolean hasAccess = false;

        switch (page) {
            case 1:
                if(roles.contains(Roles.PAGE_ONE.getRole())) {
                    hasAccess = true;
                }
                break;
            case 2:
                if(roles.contains(Roles.PAGE_TWO.getRole())) {
                    hasAccess = true;
                }
                break;
            case 3:
                if(roles.contains(Roles.PAGE_THREE.getRole())) {
                    hasAccess = true;
                }
                break;
            default:
                break;
        }
        return hasAccess;
    }
}
