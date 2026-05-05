import java.util.Date;

/**
 * Represents a completed customer order
 * Stores order details including products, total cost, and authorization code
 *
 * @version 1.0
 */
public class Order {
    private final Date orderDate;
    private final String customerId;
    private final Cart cart;
    private final int authCode;

    /**
     * Constructs a Customer with login credentials
     *
     * @param customerId the customer id
     * @param cart       the order cart
     * @param authCode   the order authorization code
     */
    public Order(String customerId, Cart cart, int authCode) {
        this.orderDate = new Date();
        this.customerId = customerId;
        this.cart = cart;
        this.authCode = authCode;
    }

    /**
     * Displays basic order information
     */
    public void displayOrder() {
        System.out.println("Order Date: " + orderDate);

        System.out.println(cart.getItems());

        System.out.println("Total: $" + cart.getTotal());
    }

    /**
     * Returns the order customer id
     *
     * @return the order customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Formats the order details as a String for the GUI.
     *
     * @return A formatted string of the order details.
     */
    public String getOrderDetails() {
        return "Order Date: " + orderDate + "\n" +
                "Items: " + cart.getItems().toString() + "\n" +
                "Total: $" + String.format("%.2f", cart.getTotal()) + "\n" +
                "Auth Code: " + authCode;
    }
}
