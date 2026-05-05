import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Graphical User Interface for the Customer Order System (COS)
 * Entry point of the application
 * This class coordinates interactions between system components
 * Handles Item Selection, Order Making, and Order View
 *
 * @version 3.4
 */
public class CustomerOrderSystemGUI extends JFrame {
    private final AuthService auth = new AuthService();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final JTextArea cartTextArea = new JTextArea();
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private Customer loggedIn = null;

    /**
     * Constructor specifying class details
     */
    public CustomerOrderSystemGUI() {
        setTitle("Customer Order System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(mainMenuPanel(), "MainMenu");
        cardPanel.add(customerMenuPanel(), "CustomerMenu");
        cardPanel.add(selectItemsPanel(), "SelectItems");

        add(cardPanel);

        cardLayout.show(cardPanel, "MainMenu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerOrderSystemGUI gui = new CustomerOrderSystemGUI();
            gui.setVisible(true);
        });
    }

    /**
     * Creates the Main Menu Screen (Log In / Sign Up)
     */
    private JPanel mainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Welcome to COS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JButton loginBtn = new JButton("Log In");
        JButton signupBtn = new JButton("Sign Up");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(e -> showLogin());

        signupBtn.addActionListener(e -> showSignup());

        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(loginBtn);
        panel.add(signupBtn);
        panel.add(exitBtn);

        return panel;
    }

    /**
     * Displays a dialog for Log In.
     * Handles the 3-attempt limit and the security question verification.
     */
    private void showLogin() {
        JDialog logIn = new JDialog(this, "Log In", true);
        logIn.setSize(300, 200);
        logIn.setLayout(new GridLayout(4, 2, 10, 10));
        logIn.setLocationRelativeTo(this);

        JLabel idLabel = new JLabel("User ID:");
        JTextField idField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        final int[] attempts = {0};

        // Login Button Logic
        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passField.getPassword());

            Customer c = auth.getCustomer(id);

            // Check if user id exists
            if (c == null) {
                JOptionPane.showMessageDialog(logIn, "No account found.", "Error", JOptionPane.ERROR_MESSAGE);
                logIn.dispose();
                return;
            }

            // Check password
            if (!c.validatePassword(password)) {
                attempts[0]++;
                if (attempts[0] >= 3) {
                    JOptionPane.showMessageDialog(logIn, "Maximum login attempts exceeded.", "Locked Out", JOptionPane.ERROR_MESSAGE);
                    logIn.dispose();
                } else {
                    JOptionPane.showMessageDialog(logIn, "Incorrect password. Attempts left: " + (3 - attempts[0]), "Warning", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            // Show security question
            String answer = JOptionPane.showInputDialog(logIn, c.getSecurityQuestion(), "Security Question", JOptionPane.QUESTION_MESSAGE);

            // Verify security answer
            if (answer != null && answer.equals(c.getSecurityAnswer())) {
                JOptionPane.showMessageDialog(logIn, "Welcome, " + c.getName() + "!");
                loggedIn = c;
                logIn.dispose();
                cardLayout.show(cardPanel, "CustomerMenu");
            } else {
                JOptionPane.showMessageDialog(logIn, "Incorrect security answer. Access denied.", "Error", JOptionPane.ERROR_MESSAGE);
                logIn.dispose();
            }
        });

        cancelBtn.addActionListener(e -> logIn.dispose());

        logIn.add(idLabel);
        logIn.add(idField);
        logIn.add(passLabel);
        logIn.add(passField);
        logIn.add(loginBtn);
        logIn.add(cancelBtn);

        logIn.setVisible(true);
    }

    /**
     * Displays a dialog for the Create Account use case.
     * Validates input fields and enforces password rules before registering.
     */
    private void showSignup() {
        JDialog signUp = new JDialog(this, "Sign Up", true);
        signUp.setSize(400, 400);
        signUp.setLayout(new GridLayout(8, 2, 10, 10));
        signUp.setLocationRelativeTo(this);

        JTextField idField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField ccField = new JTextField();

        // Security questions dropdown
        String[] questions = {
                "Where were you born?",
                "What's your father's name?",
                "What's your mother's maiden name?"
        };
        JComboBox<String> questionBox = new JComboBox<>(questions);
        JTextField answerField = new JTextField();

        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");

        // Register Button Logic
        registerBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String cc = ccField.getText().trim();
            String answer = answerField.getText().trim();
            String question = (String) questionBox.getSelectedItem();

            // Check for empty fields
            if (id.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty() || cc.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(signUp, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if ID already exists
            if (auth.getCustomer(id) != null) {
                JOptionPane.showMessageDialog(signUp, "ID already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password
            if (!Validator.isValidPassword(password)) {
                JOptionPane.showMessageDialog(signUp,
                        "Password must be at least 6 chars, with 1 uppercase, 1 digit, and 1 special char (@#$%&*).",
                        "Invalid Password", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create the customer and register
            Customer c = new Customer(id, password, name, address, cc);
            c.setSecurityQuestion(question);
            c.setSecurityAnswer(answer);
            auth.register(c);

            JOptionPane.showMessageDialog(signUp, "Account created successfully! You can now log in.");
            signUp.dispose();
        });

        cancelBtn.addActionListener(e -> signUp.dispose());

        signUp.add(new JLabel(" User ID:"));
        signUp.add(idField);
        signUp.add(new JLabel(" Password:"));
        signUp.add(passField);
        signUp.add(new JLabel(" Name:"));
        signUp.add(nameField);
        signUp.add(new JLabel(" Address:"));
        signUp.add(addressField);
        signUp.add(new JLabel(" Credit Card:"));
        signUp.add(ccField);
        signUp.add(new JLabel(" Security Question:"));
        signUp.add(questionBox);
        signUp.add(new JLabel(" Security Answer:"));
        signUp.add(answerField);
        signUp.add(registerBtn);
        signUp.add(cancelBtn);

        signUp.setVisible(true);
    }

    /**
     * Creates the Customer Menu Screen (After Login)
     */
    private JPanel customerMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Customer Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton selectItemsBtn = new JButton("Select Items");
        JButton makeOrderBtn = new JButton("Make Order");
        JButton viewOrdersBtn = new JButton("View Orders");
        JButton logOutBtn = new JButton("Log Out");

        selectItemsBtn.addActionListener(e -> {
            updateCartDisplay();
            cardLayout.show(cardPanel, "SelectItems");
        });

        makeOrderBtn.addActionListener(e -> showCheckout());

        viewOrdersBtn.addActionListener(e -> showViewOrders());

        logOutBtn.addActionListener(e -> {
            loggedIn = null;
            if (cartTextArea != null) cartTextArea.setText("");

            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            cardLayout.show(cardPanel, "MainMenu");
        });

        panel.add(titleLabel);
        panel.add(selectItemsBtn);
        panel.add(makeOrderBtn);
        panel.add(viewOrdersBtn);
        panel.add(logOutBtn);

        return panel;
    }

    /**
     * Creates the Select Items (Catalog) Screen.
     */
    private JPanel selectItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Browse Catalog", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel selectionPanel = new JPanel(new FlowLayout());

        String[] productNames = new String[Catalog.products.length];
        for (int i = 0; i < Catalog.products.length; i++) {
            Product p = Catalog.products[i];
            productNames[i] = p.getName() + " - $" + p.getEffectivePrice();
        }
        JComboBox<String> productBox = new JComboBox<>(productNames);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JButton addBtn = new JButton("Add to Cart");

        selectionPanel.add(new JLabel("Select Product:"));
        selectionPanel.add(productBox);
        selectionPanel.add(new JLabel("Qty:"));
        selectionPanel.add(quantitySpinner);
        selectionPanel.add(addBtn);

        centerPanel.add(selectionPanel, BorderLayout.NORTH);

        cartTextArea.setEditable(false);
        cartTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(cartTextArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Save Cart & Return to Menu");
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            if (loggedIn == null) return;

            // Initialize cart if empty
            if (loggedIn.getCart() == null) {
                loggedIn.setCart(new Cart());
            }

            int selectedIndex = productBox.getSelectedIndex();
            Product selectedProduct = Catalog.products[selectedIndex];
            int qty = (Integer) quantitySpinner.getValue();

            loggedIn.getCart().addProduct(selectedProduct, qty);
            updateCartDisplay();
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "CustomerMenu"));

        return panel;
    }

    /**
     * Helper method to refresh the text inside the Cart text area.
     */
    private void updateCartDisplay() {
        if (loggedIn == null || loggedIn.getCart() == null || loggedIn.getCart().getItems().isEmpty()) {
            cartTextArea.setText("Your cart is currently empty.");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Current Cart ---\n\n");
        Cart cart = loggedIn.getCart();

        for (Product p : cart.getItems().keySet()) {
            int qty = cart.getItems().get(p);
            sb.append(String.format("%-25s x%d   $%.2f\n", p.getName(), qty, p.getEffectivePrice() * qty));
        }

        sb.append("\n====================================\n");
        sb.append(String.format("TOTAL: $%.2f", cart.calculateTotal()));

        cartTextArea.setText(sb.toString());
    }

    /**
     * Displays a dialog for the Make Order (Checkout) use case.
     * Handles delivery selection, total calculation, and simulated bank approval.
     */
    private void showCheckout() {
        // Check if there's anything to buy
        if (loggedIn == null || loggedIn.getCart() == null || loggedIn.getCart().getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty! Please select items first.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog checkoutDialog = new JDialog(this, "Checkout", true);
        checkoutDialog.setSize(350, 250);
        checkoutDialog.setLayout(new BorderLayout(10, 10));
        checkoutDialog.setLocationRelativeTo(this);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        double subtotal = loggedIn.getCart().calculateTotal();
        JLabel totalLabel = new JLabel("Subtotal: $" + String.format("%.2f", subtotal));

        // Delivery Options
        JRadioButton mailOption = new JRadioButton("Mail Delivery (+$3.00)");
        JRadioButton pickupOption = new JRadioButton("In-Store Pickup (Free)", true);
        ButtonGroup deliveryGroup = new ButtonGroup();
        deliveryGroup.add(mailOption);
        deliveryGroup.add(pickupOption);

        JLabel finalTotalLabel = new JLabel("Final Total: $" + String.format("%.2f", subtotal));
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        java.awt.event.ActionListener updateTotal = e -> {
            double total = loggedIn.getCart().getTotal();
            if (mailOption.isSelected()) total += 3.00;
            finalTotalLabel.setText("Final Total: $" + String.format("%.2f", total));
        };
        mailOption.addActionListener(updateTotal);
        pickupOption.addActionListener(updateTotal);

        centerPanel.add(totalLabel);
        centerPanel.add(mailOption);
        centerPanel.add(pickupOption);
        centerPanel.add(finalTotalLabel);

        checkoutDialog.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = getJPanel(mailOption, checkoutDialog);
        checkoutDialog.add(bottomPanel, BorderLayout.SOUTH);

        checkoutDialog.setVisible(true);
    }

    private JPanel getJPanel(JRadioButton mailOption, JDialog checkoutDialog) {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton payButton = new JButton("Pay Now");
        JButton cancelButton = new JButton("Cancel");

        payButton.addActionListener(e -> {
            double finalTotal = loggedIn.getCart().getTotal();
            if (mailOption.isSelected()) finalTotal += 3.00;

            String currentCc = loggedIn.getCreditCard();
            boolean paymentSuccess = false;

            while (!paymentSuccess) {
                PaymentMethod payment = new CreditCardPayment(currentCc);

                // Simulating Bank Approval
                if (payment.processPayment(finalTotal)) {
                    paymentSuccess = true;
                    int authCode = new java.util.Random().nextInt(9000) + 1000;

                    // Create and store order
                    orders.add(new Order(loggedIn.getId(), loggedIn.getCart(), authCode));

                    // Clear cart
                    loggedIn.setCart(null);

                    if (cartTextArea != null) {
                        cartTextArea.setText("Your cart is currently empty.");
                    }

                    JOptionPane.showMessageDialog(checkoutDialog,
                            "Order Confirmed!\nAuthorization Code: " + authCode,
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    checkoutDialog.dispose();
                } else {
                    // Payment denied
                    // Prompt for a new card
                    String newCc = JOptionPane.showInputDialog(checkoutDialog,
                            "Bank denied the charge.\nPlease enter a new credit card number:",
                            "Payment Failed", JOptionPane.WARNING_MESSAGE);

                    if (newCc == null || newCc.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(checkoutDialog, "Checkout aborted.");
                        return;
                    }

                    currentCc = newCc;
                    loggedIn.setCreditCard(currentCc);
                }
            }
        });

        cancelButton.addActionListener(e -> checkoutDialog.dispose());

        bottomPanel.add(payButton);
        bottomPanel.add(cancelButton);
        return bottomPanel;
    }

    /**
     * Displays a dialog showing the user's past orders.
     */
    private void showViewOrders() {
        JDialog viewDialog = new JDialog(this, "Order History", true);
        viewDialog.setSize(400, 300);
        viewDialog.setLocationRelativeTo(this);

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder("--- Your Past Orders ---\n\n");
        boolean hasOrders = false;

        for (Order o : orders) {
            if (o.getCustomerId().equals(loggedIn.getId())) {
                sb.append(o.getOrderDetails()).append("\n\n-------------------------\n\n");
                hasOrders = true;
            }
        }

        if (!hasOrders) {
            sb.append("You have no past orders.");
        }

        historyArea.setText(sb.toString());
        viewDialog.add(new JScrollPane(historyArea));

        viewDialog.setVisible(true);
    }
}