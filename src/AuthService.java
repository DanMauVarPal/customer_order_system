import java.util.*;

/**
 * Provides authentication services for the Customer Order System
 * Maintains a collection of registered customers
 *
 * @version 1.1
 */
public class AuthService {
    private final Map<String, Customer> customers = new HashMap<>();
    private final String[] securityQuestions = {"Where were you born?", "What's your father's name?", "What's your father's name?"};

    /**
     * Registers a new customer in the system
     *
     * @param c the customer to register
     */
    public void register(Customer c) {
        customers.put(c.getId(), c);
    }

    /**
     * Attempts to log in a customer with up to three attempts
     * Includes validation of security question after password verification
     *
     * @param scanner Scanner object for user input
     * @return the authenticated Customer if successful, null otherwise
     */
    public Customer logOn(Scanner scanner) {
        int attempts = 0;

        while (attempts < 3) {
            // Step 1: ID
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();

            // Step 1: Password
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Step 2: ID & Password validation
            Customer c = customers.get(id);

            // Step 3: Check if user id exists
            if (c == null) {
                System.out.println("No account found");
                return null;
            }

            // Step 3: Check if password is correct
            if (!c.validatePassword(password)) {
                attempts++;
                System.out.println("Incorrect password");

                if (attempts == 3) {
                    System.out.println("Maximum attempts reached");
                    return null;
                }
            } else {
                // Step 3: Security questions display
                System.out.println("Security Question: " + c.getSecurityQuestion());

                // Step 4: Security answer input
                System.out.print("Answer: ");
                String answer = scanner.nextLine();

                // Step 5: Check if security answer is correct
                if (answer.equalsIgnoreCase(c.getSecurityAnswer())) {
                    System.out.println("Welcome " + c.getName() + "!");
                    return c;
                } else {
                    System.out.println("Incorrect security answer");
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Creates an account
     * Includes validation for password
     *
     * @param scanner Scanner object for user input
     * @return the created Customer account
     */
    public Customer createAccount(Scanner scanner) {
        String id;
        String password;
        String name;
        String address;
        String creditCard;

        // Step 1: Customer ID
        while (true) {
            System.out.print("Please enter your ID: ");
            id = scanner.nextLine();

            // Step 2: Check if the account already exists
            if (customers.get(id) == null)
                break;

            System.out.println("Account already exists");
        }

        // Step 3: Password
        while (true) {
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();

            // Step 4: Check if the password is valid
            if (Validator.isValidPassword(password))
                break;

            System.out.println("Incorrect password" +
                    "\nMust be at least 6 characters long, include at least a digit, a special character and an uppercase letter");
        }

        // Step 5: Name, Address & Credit Card Number
        while (true) {
            // Step 6: User input
            System.out.print("Enter your name: ");
            name = scanner.nextLine();

            System.out.print("Enter your address: ");
            address = scanner.nextLine();

            System.out.print("Enter your credit card number: ");
            creditCard = scanner.nextLine();

            // Step 7: Check for non-null input
            if (!name.isEmpty() && !address.isEmpty() && !creditCard.isEmpty())
                break;

            System.out.println("Name, Address, or Credit Card Number empty");
        }

        // Step 7: Confirmation Message
        System.out.println("Account created");
        Customer c = new Customer(id, password, name, address, creditCard);
        register(c);

        // Step 8: Security questions display
        for (int i = 0; i < securityQuestions.length; i++)
            System.out.println((i + 1) + ". " + securityQuestions[i]);

        // Step 9: Security questions selection
        System.out.print("Choose security question: ");
        c.setSecurityQuestion(securityQuestions[Integer.parseInt(scanner.nextLine())]);

        // Step 10: Security answer
        System.out.print("Enter your security answer: ");
        String securityAnswer = scanner.nextLine();
        c.setSecurityAnswer(securityAnswer);

        return c;
    }
}
