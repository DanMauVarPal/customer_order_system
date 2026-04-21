================================================================================
CUSTOMER ORDER SYSTEM (COS)
Spring 2026 CS2365
================================================================================

================================================================================
1. HOW TO RUN THE PROGRAM 
================================================================================
This application is a state-driven, Java-based console application.

[ Prerequisites ]
- Java Development Kit (JDK) 8 or higher installed on your system.
- A command-line terminal (Command Prompt, PowerShell, Terminal, etc.).

[ Compilation Instructions ]
1. Open your terminal or command prompt.
2. Navigate to the directory containing all the .java files for the project.
3. Compile all Java files by executing the following command:
   javac *.java

[ Execution Instructions ]
1. Once successfully compiled, start the application by running the main class:
   java CustomerOrderSystem
2. The application will launch and display the Main Menu.

[ Interacting with the Console ]
- Navigation: Type the number corresponding to the menu option you wish to
  select and press [Enter].
- Data Entry: When prompted for text (e.g., Names, Passwords, Credit Cards),
  type your response and press [Enter].
- Exiting: Select the "Exit" option from the Main Menu, or type "exit" when
  prompted during specific alternative sequences (like a denied bank charge).


================================================================================
2. DEMONSTRATION OF USE CASES
================================================================================
The console application runs through interactive menus:

[ Create Account ]
- From the Main Menu, select "Sign Up".
- Enter a unique ID and a valid password (minimum 6 chars, 1 uppercase, 1 digit,
  1 special character).
- Provide your Name, Address, and Credit Card Number.
- Select and answer a security question.

[ Log On ]
- From the Main Menu, select "Log In".
- Enter your registered ID and Password.
- You have up to 3 attempts to enter the correct password.
- Upon success, answer your security question to gain access to the Customer Menu.

[ Select Items ]
- From the Customer Menu, select "Select Items".
- The system will display the Catalog. Enter the product number to select it,
  then enter the desired quantity.
- Select the "Checkout" option (Option 11) to finalize the cart.

[ Make Order ]
- From the Customer Menu, select "Make Order".
- Choose your delivery method (Mail for $3.00 or In-Store for free).
- The system will process your credit card.
    * Alternative Sequence: If the simulated bank denies the charge, you will be
      prompted to enter a new credit card number or type "exit" to cancel.
- Upon success, an authorization code is generated and the cart is cleared.

[ View Order ]
- From the Customer Menu, select "View Orders".
- The system retrieves and displays the date, items, and total of all historical
  orders linked to your Customer ID.

[ Log Out ]
- From the Customer Menu, select "Log Out" to clear the session and return to
  the Main Menu.


================================================================================
3. PROJECT UPDATES & UML/URL SECTION
================================================================================
[ Project URL ]
- Repository Link: https://github.com/DanMauVarPal/customer_order_system.git

[ Code & Architectural Updates from Part 1 ]
Several updates were implemented to transition the base classes into a fully
functional console application. These updates have been reflected in the updated
UML diagram (User.uml) and Java files:

1. CustomerOrderSystem.java (Updated to v2.0):
    - Transformed into the main driver class of the application.
    - Added a continuous `run()` while-loop to handle application state
      (LoggedIn vs LoggedOut).
    - Added `showMainMenu()` and `showCustomerMenu()` to handle user navigation.
    - Integrated the `tiendaMies` instance to properly instantiate the system.

2. Product.java (Updated to v1.1):
    - Added getter methods `getName()` and `getDescription()` to allow the
      CustomerOrderSystem to cleanly retrieve and print catalog details to the UI.

3. Customer.java (Updated to v1.1):
    - Added the `getName()` method to properly retrieve the customer's name.

4. AuthService.java (Updated to v1.1):
    - Javadoc versioning updated to reflect Part 2 integration.
    - Strengthened console prompt UI text for a smoother user experience.

5. Alternative Sequences Handling:
    - Added loop logic in `makeOrder()` to update the `CreditCardPayment` object
      dynamically if a user needs to input a new card due to a denied charge.
    - Handled empty cart states to prevent null exceptions during checkout.