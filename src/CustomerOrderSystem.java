import java.util.*;

/**
 * Main driver class for the Customer Order System (COS)
 * Entry point of the application
 * This class coordinates interactions between system components
 * Handles Item Selection, Order Making, and Order View
 *
 * @version 2.0
 */
public class CustomerOrderSystem {
    Scanner scanner = new Scanner(System.in);
    ArrayList<Order> orders = new ArrayList<>();
    AuthService auth = new AuthService();
    Customer loggedIn;
    Cart cart = new Cart();

    public static void main(String[] args) {
        // Create store
        CustomerOrderSystem tiendaMies = new CustomerOrderSystem();
        // Run store
        tiendaMies.run();
    }

    /**
     * Main execution loop handling the application's state and menus.
     */
    public void run() {
        System.out.println("Welcome to the Customer Order System (COS)");

        // System Run
        boolean menu = true;
        while (menu) {
            // Show menu for log in or sign up
            if (loggedIn == null)
                menu = showMainMenu();

            // Show customer menu
            else
                showCustomerMenu();
        }

        System.out.println("Thank you for using COS. Goodbye!");
    }

    private boolean showMainMenu() {
        System.out.print("""
                
                --- Main Menu ---
                1. Log In
                2. Sign Up
                3. Exit
                Select an option:\s""");

        String choice = scanner.nextLine();

        return switch (choice) {
            // Log In
            case "1" -> {
                loggedIn = auth.logOn(scanner);
                yield true;
            }

            // Sign Up
            case "2" -> {
                loggedIn = auth.createAccount(scanner);
                yield true;
            }

            // Exit Program
            case "3" -> false;

            default -> {
                System.out.println("Invalid option. Please try again.");
                yield true;
            }
        };
    }

    private void showCustomerMenu() {
        System.out.print("""
                
                --- Customer Menu ---
                1. Select Items
                2. Make Order (Checkout)
                3. View Orders
                4. Log Out
                Select an option:\s""");

        String choice = scanner.nextLine();

        switch (choice) {
            // Select items and add to cart
            case "1":
                System.out.println("\n--- Select Items ---");
                selectItems();
                break;

            // Checkout
            case "2":
                System.out.println("\n--- Make Order ---");
                makeOrder();
                break;

            // View Customer's Orders
            case "3":
                System.out.println("\n--- View Orders ---");
                viewOrder();
                break;

            // Log Out
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
     */
    public void selectItems() {
        while (true) {
            // Catalog display
            for (int i = 0; i < Catalog.products.length; i++)
                System.out.println((i + 1) + ". " + Catalog.products[i].getName() +
                        " - $" + Catalog.products[i].getEffectivePrice() +
                        " Desc: " + Catalog.products[i].getDescription());

            System.out.println((Catalog.products.length + 1) + ". Checkout" +
                            (Catalog.products.length + 2) + ". Empty Cart" +
                    "\n0. Exit product selection");

            // Product selection
            System.out.print("Enter selection: ");
            int choice = Integer.parseInt(scanner.nextLine());

            // Invalid selections
            if (choice < 0 || choice > (Catalog.products.length + 2)) {
                System.out.println("Invalid selection");
                continue;
            }

            // Exit selection
            else if (choice == 0) {
                System.out.println("Exit product selection");
                break;
            }

            // Checkout
            else if (choice == (Catalog.products.length + 1)) {
                // Display products in cart
                for (Product p : cart.getItems().keySet())
                    System.out.println(p.getName() + " - Quantity: " + cart.getItems().get(p));

                System.out.println("Total price: $" + cart.calculateTotal());

                // Add cart to logged in customer
                loggedIn.setCart(cart);

                return;
            }

            // Get a new cart
            else if (choice == (Catalog.products.length + 2))
                cart = new Cart();

            // Item selected
            // Quantity input
            System.out.print("Select quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Cart update
            cart.addProduct(Catalog.products[choice - 1], quantity);
        }
    }

    /**
     * Makes an order for the logged in customer
     */
    public void makeOrder() {
        // No items selected in cart
        if (loggedIn.getCart() == null) {
            System.out.println("Your cart is empty. Please select items first.");
            return;
        }

        // Delivery method selection
        System.out.print("""
                
                Select delivery:
                1. Mail by charging a fee ($3.00)
                2. In-store pick up for free
                Enter selection:\s""");

        int choice = Integer.parseInt(scanner.nextLine());

        double total = loggedIn.getCart().getTotal();

        switch (choice) {
            case 1:
                total += 3;
            case 2:
                System.out.println("Total: $" + total);

                // Simulate payment
                CreditCardPayment payment = new CreditCardPayment(loggedIn.getCreditCard());
                while (true) {
                    // Requesting bank approval
                    if (payment.processPayment(total)) {
                        // Auth Code
                        int authCode = new Random().nextInt(9000) + 1000;

                        // Store order
                        orders.add(new Order(loggedIn.getId(), loggedIn.getCart(), authCode));

                        // Clear the cart after a successful order
                        loggedIn.setCart(null);

                        // Order confirmation display
                        System.out.println("Order confirmed. Authorization code: " + authCode);
                        return;
                    }

                    // Denied credit card & new entry
                    System.out.print("""
                            Bank denied the charge.
                            Enter new credit card number (or type 'exit' to cancel):\s""");

                    String newCard = scanner.nextLine();

                    // Exit payment
                    if (newCard.equalsIgnoreCase("exit")) {
                        System.out.println("Order cancelled.");
                        return;
                    }

                    // Assign new credit card to customer
                    loggedIn.setCreditCard(newCard);

                    // Update the payment method instance with the new card
                    payment = new CreditCardPayment(newCard);
                }

            default:
                System.out.println("Order cancelled.");
        }
    }

    /**
     * Customer orders display
     */
    public void viewOrder() {
        boolean hasOrders = false;
        for (Order o : orders)
            if (o.getCustomerId().equals(loggedIn.getId())) {
                o.displayOrder();
                hasOrders = true;
            }

        if (!hasOrders)
            System.out.println("You have no past orders.");
    }
}