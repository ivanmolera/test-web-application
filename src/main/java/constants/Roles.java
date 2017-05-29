package constants;

/**
 * Created by ivanmolera on 27/5/17.
 */
public enum Roles {

        ADMIN("ADMIN"),
        PAGE_ONE("PAGE_1"),
        PAGE_TWO("PAGE_2"),
        PAGE_THREE("PAGE_3");

        private String role;

        Roles(String role) { this.role = role; }

        public String getRole() {
            return this.role;
        }
}
