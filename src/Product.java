/**
 * Represents a product available in the system catalog
 * Stores name, description, pricing, and sale price
 *
 * @version 1.0
 */
public class Product {
    private final String name;
    private final String description;
    private final double price;
    private final double salePrice;

    /**
     * Constructs a Product with pricing details
     *
     * @param name        the product name
     * @param description the product description
     * @param price       the regular price
     * @param salePrice   the sale price
     */
    public Product(String name, String description, double price, double salePrice) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
    }

    /**
     * Returns the effective price (sale price if available, otherwise regular price)
     *
     * @return the price used for purchase
     */
    public double getEffectivePrice() {
        return salePrice > -1 ? salePrice : price;
    }

    /**
     * Returns the name of the product
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
