/**
 * Utility class for validating user password
 *
 * @version 1.0
 */
public class Validator {
    /**
     * Validates a password based on system requirements:
     * - Minimum 6 characters
     * - At least one uppercase letter
     * - At least one digit
     * - At least one special character (@, #, $, %, &, *)
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password.length() < 6) {
            System.out.println("Password too short");
            return false;
        }

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if ("@#$%&*".indexOf(c) != -1) {
                hasSpecial = true;
            }
        }

        return hasUpper && hasDigit && hasSpecial;
    }
}
