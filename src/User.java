/**
 * Abstract base class representing a system user
 * Stores userID and password
 *
 * @version 1.0
 */
public abstract class User {
    private final String id;
    private final String password;

    /**
     * Constructs a User with an ID and password
     *
     * @param id       the unique user identifier
     * @param password the user's password
     */
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Validates the entered password against the stored password
     *
     * @param input the password entered by the user
     * @return true if the password matches, false otherwise
     */
    public boolean validatePassword(String input) {
        return password.equals(input);
    }

    /**
     * Returns the user ID
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }
}
