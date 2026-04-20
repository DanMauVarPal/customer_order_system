import java.util.Random;

/**
 * Simulates interaction with a bank for payment authorization
 * Implements from the PaymentMethod interface
 *
 * @version 1.0
 */
public class CreditCardPayment implements PaymentMethod {
    private final String cardNumber;

    /**
     * Constructs a CreditCardPayment with a given card number
     *
     * @param cardNumber the credit card number
     */
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Processes the payment by simulating a bank approval
     *
     * @param amount the amount to charge
     * @return true if approved, false otherwise
     */
    @Override
    public boolean processPayment(double amount) {
        return new Random().nextBoolean();
    }
}
