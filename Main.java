/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pros5121poe;

import java.util.Scanner;

public class Main {

    private static Object pros5121poe;
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println(
                    "===== Registration and Login System ====="
            );

            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter a username: ");
            String username = scanner.nextLine();

            System.out.print("Enter a password: ");
            String password = scanner.nextLine();

            System.out.print(
                    "Enter your South African cellphone number "
                    + "(+27 followed by 9 digits): "
            );

            String cellPhoneNumber = scanner.nextLine();

            pros5121poe.Login user = new pros5121poe.Login(
                    firstName,
                    lastName,
                    username,
                    password,
                    cellPhoneNumber
            );

            String registrationMessage =
                    user.registerUser();

            System.out.println("\n" + registrationMessage);

            if (!registrationMessage.equals(
                    "User successfully registered.")) {

                return;
            }

            System.out.println("\n===== Login =====");

            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();

            boolean loginStatus = user.loginUser(
                    enteredUsername,
                    enteredPassword
            );

            System.out.println(
                    user.returnLoginStatus(loginStatus)
            );

            if (!loginStatus) {

                return;
            }

            System.out.println("\nWelcome to QuickChat.");

            boolean running = true;

            while (running) {

                System.out.println("\n===== QuickChat Menu =====");

                System.out.println("1. Send Messages");

                System.out.println(
                        "2. Show recently sent messages"
                );

                System.out.println("3. Quit");

                System.out.print("Choose an option: ");

                String menuInput = scanner.nextLine().trim();

                switch (menuInput) {

                    case "1":

                        sendMessages(scanner);

                        break;

                    case "2":

                        System.out.println("Coming Soon.");

                        break;

                    case "3":

                        Message counter =
                                new Message(0, "", "");

                        System.out.println(
                                "Total messages successfully sent: "
                                + counter.returnTotalMessages()
                        );

                        System.out.println(
                                "Thank you for using QuickChat. Goodbye!"
                        );

                        running = false;

                        break;

                    default:

                        System.out.println(
                                "Invalid option. Please choose 1, 2, or 3."
                        );
                }
            }
        }
    }

    // Handles the sending of multiple messages
    private static void sendMessages(Scanner scanner) {

        int numberOfMessages;

        while (true) {

            System.out.print(
                    "How many messages would you like to enter? "
            );

            String input = scanner.nextLine().trim();

            try {

                numberOfMessages = Integer.parseInt(input);

                if (numberOfMessages < 1) {

                    System.out.println(
                            "Please enter a number greater than zero."
                    );

                    continue;
                }

                break;

            } catch (NumberFormatException ex) {

                System.out.println(
                        "Please enter a valid whole number."
                );
            }
        }

        for (int i = 0; i < numberOfMessages; i++) {

            System.out.println(
                    "\n--- Message "
                    + (i + 1)
                    + " of "
                    + numberOfMessages
                    + " ---"
            );

            System.out.print(
                    "Enter recipient cellphone number "
                    + "(+27 followed by 9 digits): "
            );

            String recipient = scanner.nextLine().trim();

            System.out.print(
                    "Enter your message (maximum 250 characters): "
            );

            String text = scanner.nextLine();

            Message message = new Message(
                    i,
                    recipient,
                    text
            );

            // Check message length
            String lengthResult =
                    message.checkMessageLength();

            System.out.println(lengthResult);

            if (!lengthResult.equals("Message ready to send.")) {

                continue;
            }

            // Check recipient cellphone number
            String cellResult =
                    message.checkRecipientCell();

            System.out.println(cellResult);

            if (!cellResult.equals(
                    "Cell phone number successfully captured.")) {

                continue;
            }

            // Display message details
            System.out.println(
                    "Message ID: " + message.getMessageID()
            );

            System.out.println(
                    "Message Hash: " + message.getMessageHash()
            );

            System.out.println(
                    "Recipient: " + message.getRecipientCell()
            );

            System.out.println(
                    "Message: " + message.getMessageText()
            );

            System.out.println(
                    "\nChoose what to do with this message:"
            );

            System.out.println("1. Send Message");

            System.out.println("2. Disregard Message");

            System.out.println(
                    "3. Store Message to send later"
            );

            System.out.print("Enter choice: ");

            String choiceInput = scanner.nextLine().trim();

            try {

                int choice = Integer.parseInt(choiceInput);

                System.out.println(
                        message.SentMessage(choice)
                );

            } catch (NumberFormatException ex) {

                System.out.println(
                        "Invalid option. Message was not sent."
                );
            }
        }
    }
}
