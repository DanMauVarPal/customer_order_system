import java.util.*;

/**
 * Main driver class for the Customer Order System (COS)
 * Entry point of the application
 * This class coordinates interactions between system components
 * Handles Item Selection, Order Making, and Order View
 *
 * @version 1.0
 */
public class CustomerOrderSystem {
    Scanner scanner = new Scanner(System.in);
    ArrayList<Order> orders = new ArrayList<>();
    AuthService auth = new AuthService();
    Customer loggedIn;
    Cart cart;

    public static void main(String[] args) {
        CustomerOrderSystem cos = new CustomerOrderSystem();
        cos.run();
    }

    /**
     * Main execution loop handling the application's state and menus.
     */
    public void run() {
        System.out.println("Welcome to the Customer Order System (COS)");

        boolean running = true;
        while (running) {
            if (loggedIn == null) {
                running = showMainMenu();
            } else {
                showCustomerMenu();
            }
        }
        System.out.println("Thank you for using COS. Goodbye!");
    }

    private boolean showMainMenu() {
        System.out.print("--- Main Menu ---" +
                "\n1. Log On" +
                "\n2. Create Account" +
                "\n3. Exit" +
                "\nSelect an option: ");

        String choice = scanner.nextLine();

        return switch (choice) {
            case "1" -> {
                loggedIn = auth.logOn(scanner);
                yield true;
            }

            case "2" -> {
                loggedIn = auth.createAccount(scanner);
                yield true;
            }

            case "3" -> false;

            default -> {
                System.out.println("Invalid option. Please try again.");
                yield true;
            }
        };
    }

    private void showCustomerMenu() {
        System.out.print("\n--- Customer Menu ---" +
                "\n1. Select Items (Browse Catalog)" +
                "\n2. Make Order (Checkout)" +
                "\n3. View Orders" +
                "\n4. Log Out" +
                "\nSelect an option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("\n--- Select Items ---");
                selectItems(scanner);
                break;
            case "2":
                System.out.println("\n--- Make Order ---");
                makeOrder(scanner);
                break;
            case "3":
                System.out.println("\n--- View Orders ---");
                viewOrder();
                break;
            case "4":
                System.out.println("\nLogging out...");
                loggedIn = null;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Adds products to cart of logged in customer
     *
     * @return a confirmation boolean for successful selection
     */
    public boolean selectItems(Scanner scanner) {
        Cart cart = new Cart();

        // Step 2: Catalog display
        for (int i = 0; i < Catalog.products.length; i++)
            System.out.println((i + 1) + ". " + Catalog.products[i].getName() + " - $" + Catalog.products[i].getEffectivePrice());

        System.out.println((Catalog.products.length + 1) + ". Checkout" +
                "\n0. Exit product selection");

        while (true) {
            // Step 3: Product selection
            System.out.print("Enter selection: ");
            int choice = Integer.parseInt(scanner.nextLine());

            // Step 3: No product selection
            if (choice < 0 || choice > (Catalog.products.length + 1)) {
                System.out.println("Invalid selection");
                break;
            } else if (choice == 0) {
                System.out.println("Exit product selection");
                break;
            }

            // Step 5: Checkout
            if (choice == (Catalog.products.length + 1)) {
                for (Product p : cart.getItems().keySet())
                    System.out.println(p.getName() + " - Quantity: " + cart.getItems().get(p));

                System.out.println("Total price: $" + cart.calculateTotal());

                if (loggedIn != null)
                    loggedIn.setCart(cart);
                return true;
            }

            // Step 3: Quantity input
            System.out.print("Select quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Step 4: Cart updating
            cart.addProduct(Catalog.products[choice - 1], quantity);
        }

        return false;
    }

    /**
     * Makes an order for the logged in customer
     *
     * @return a confirmation boolean for order making
     */
    public boolean makeOrder(Scanner scanner) {
        if (loggedIn == null || loggedIn.getCart() == null) {
            System.out.println("Your cart is empty. Please select items first.");
            return false;
        }

        // Step 2: Delivery methods display
        System.out.println("Select delivery:\n" +
                "1. Mail by charging a fee ($3.00)\n" +
                "2. In-store pick up for free");

        // Step 3: Delivery method selection
        System.out.print("Enter selection: ");
        int choice = Integer.parseInt(scanner.nextLine());

        double total = loggedIn.getCart().getTotal();

        if (choice == 1)
            total += 3;
            // Step 3: Exit order making
        else if (choice != 2) {
            System.out.println("Order cancelled.");
            return false;
        }

        // Step 4: Order details display
        System.out.println("Total: $" + total);

        // Step 5: User credit card retrieval
        CreditCardPayment payment = new CreditCardPayment(loggedIn.getCreditCard());

        while (true) {
            // Step 6: Requesting bank approval
            if (payment.processPayment(total)) {
                // Step 7: Auth Code
                int authCode = new Random().nextInt(9000) + 1000;

                // Step 8: Store order
                orders.add(new Order(loggedIn.getId(), loggedIn.getCart(), authCode));

                // Clear the cart after a successful order
                loggedIn.setCart(null);

                // Step 9: Order confirmation display
                System.out.println("Order confirmed. Authorization code: " + authCode);
                return true;
            }

            // Step 7: Denied credit card & new entry
            System.out.print("Bank denied the charge. Enter new credit card number (or type 'exit' to cancel): ");
            String newCard = scanner.nextLine();

            if (newCard.equalsIgnoreCase("exit")) {
                System.out.println("Order cancelled.");
                return false;
            }
            loggedIn.setCreditCard(newCard);
            payment = new CreditCardPayment(newCard); // Update the payment method instance with the new card
        }
    }

    /**
     * Customer orders display
     */
    public void viewOrder() {
        if (loggedIn == null)
            return;

        boolean hasOrders = false;
        for (Order o : orders) {
            if (o.getCustomerId().equals(loggedIn.getId())) {
                o.displayOrder();
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            System.out.println("You have no past orders.");
        }
    }
}