/**
 * Represents a customer in the Customer Order System
 * Stores personal information, payment details, and security credentials
 * Inherits from the User class
 *
 * @version 1.0
 */
public class Customer extends User {
    private final String name;
    private final String address;
    private String creditCard;
    private String securityQuestion;
    private String securityAnswer;
    private Cart cart;

    /**
     * Constructs a Customer with login credentials
     *
     * @param id         the customer ID
     * @param password   the customer password
     * @param name       the customer name
     * @param address    the customer address
     * @param creditCard the customer credit card
     */
    public Customer(String id, String password, String name, String address, String creditCard) {
        super(id, password);
        this.name = name;
        this.address = address;
        this.creditCard = creditCard;
    }

    /**
     * Returns the customer credit card
     *
     * @return the customer credit card
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * Sets the customer security question
     *
     * @param creditCard the customer security question
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * Returns the customer security question
     *
     * @return the customer security question
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * Sets the customer security question
     *
     * @param securityQuestion the customer security question
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * Returns the customer security answer
     *
     * @return the customer security answer
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * Sets the customer security answer
     *
     * @param securityAnswer the customer security answer
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * Returns the customer cart
     *
     * @return the customer cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Sets the customer cart
     *
     * @param cart the customer cart
     */
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}