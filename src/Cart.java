import java.util.*;

/**
 * Represents a shopping cart containing selected products and their quantities
 * Provides functionality to add items and calculate total cost
 *
 * @version 1.0
 */
public class Cart {
    private final Map<Product, Integer> items = new HashMap<>();
    private double total;

    /**
     * Adds a product and its quantity to the cart
     *
     * @param product  the product to add
     * @param quantity the quantity of the product
     */
    public void addProduct(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    /**
     * Calculates the total cost of all items in the cart
     *
     * @return the total price
     */
    public double calculateTotal() {
        total = 0;
        for (Product p : items.keySet())
            total += p.getEffectivePrice() * items.get(p);

        return total;
    }

    /**
     * Returns all items in the cart
     *
     * @return a map of products and their quantities
     */
    public Map<Product, Integer> getItems() {
        return items;
    }

    /**
     * Returns the total price of the cart
     *
     * @return the double total price of the cart
     */
    public double getTotal() {
        return total;
    }
}
