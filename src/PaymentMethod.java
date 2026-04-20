/**
 * Represents a generic payment method
 * This interface defines the behavior required to process a payment
 *
 * @version 1.0
 */
public interface PaymentMethod {

    /**
     * Processes a payment for the specified amount
     *
     * @param amount the amount to be charged
     * @return true if the payment is successful, false otherwise
     */
    boolean processPayment(double amount);
}
