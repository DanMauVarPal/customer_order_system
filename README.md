================================================================================
CUSTOMER ORDER SYSTEM (COS)
Spring 2026 CS2365
================================================================================

================================================================================
1. HOW TO RUN THE PROGRAM
   ================================================================================
   This project includes BOTH a Console Application and a Graphical User Interface
   (GUI) Application, sharing the same backend domain logic.

[ Prerequisites ]
- Java Development Kit (JDK) 8 or higher installed on your system.
- A command-line terminal (Command Prompt, PowerShell, Terminal, etc.).

[ Compilation Instructions ]
1. Open your terminal or command prompt.
2. Navigate to the directory containing all the .java files for the project.
3. Compile all Java files by executing the following command:
   javac *.java

[ Execution Instructions - CONSOLE APPLICATION ]
1. Start the console application by running the main class:
   java CustomerOrderSystem
2. The application will launch and display the Main Menu in the terminal.
3. Navigation: Type the number corresponding to the menu option you wish to
   select and press [Enter]. Data entry relies on standard keyboard input.

[ Execution Instructions - GUI APPLICATION ]
1. Start the GUI application by running its respective main class:
   java CustomerOrderSystemGUI
2. A separate graphical window will open.
3. Navigation: Use your mouse to interact with buttons, dropdowns, and text
   fields. The application utilizes a windowed CardLayout to seamlessly switch
   between the Main Menu, Store Catalog, and checkout dialogs.


================================================================================
2. DEMONSTRATION OF USE CASES
   ================================================================================
   Both the Console and GUI applications demonstrate the following core use cases:

[ Create Account ]
- Provide a unique ID, a valid password (minimum 6 chars, 1 uppercase, 1 digit,
  1 special character), Name, Address, and Credit Card Number.
- Select and answer a security question.

[ Log On ]
- Enter your registered ID and Password.
- You have up to 3 attempts to enter the correct password.
- Upon success, answer your security question to gain access to the Customer Menu.

[ Select Items ]
- Browse the Catalog, select items, and specify quantities to add to your Cart.

[ Make Order ]
- Choose your delivery method (Mail for $3.00 or In-Store for free).
- The system will process your credit card.
    * Alternative Sequence: If the simulated bank denies the charge, you will be
      prompted to enter a new credit card number or cancel.
- Upon success, an authorization code is generated and the cart is cleared.