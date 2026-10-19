/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pros5121poe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Path JSON_FILE = Path.of("messages.json");

    private static int messagesSent = 0;

    private static final List<Message> sentMessages =
            new ArrayList<>();

    private final String messageID;
    private final int messageNumber;
    private final String recipientCell;
    private final String messageText;
    private final String messageHash;

    private String status = "Pending";

    // Constructor
    public Message(int messageNumber, String recipientCell,
                   String messageText) {

        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;

        this.recipientCell = recipientCell == null
                ? "" : recipientCell.trim();

        this.messageText = messageText == null
                ? "" : messageText.trim();

        this.messageHash = createMessageHash();
    }

    // Generates a ten-digit message ID
    private static String generateMessageID() {

        long value = RANDOM.nextLong(
                1_000_000_000L,
                10_000_000_000L
        );

        return Long.toString(value);
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipientCell() {
        return recipientCell;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    // Checks whether the message ID is valid
    public boolean checkMessageID() {

        return messageID != null
                && !messageID.isEmpty()
                && messageID.length() <= 10;
    }

    // Validates the recipient cellphone number
    public String checkRecipientCell() {

        if (recipientCell.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";

        } else {

            return "Cell phone number is incorrectly formatted "
                    + "or does not contain an international code. "
                    + "Please correct the number and try again.";
        }
    }

    // Creates the message hash
    public String createMessageHash() {

        String firstTwo = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        String firstWord = "";
        String lastWord = "";

        if (words.length > 0 && !words[0].isBlank()) {

            firstWord = cleanWord(words[0]);
            lastWord = cleanWord(words[words.length - 1]);
        }

        return (firstTwo + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Removes punctuation from the beginning and end of a word
    private static String cleanWord(String word) {

        return word.replaceAll(
                "^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$",
                ""
        );
    }

    // Checks whether a message is within the character limit
    public String checkMessageLength() {

        if (messageText.length() <= 250) {

            return "Message ready to send.";

        } else {

            int excess = messageText.length() - 250;

            return "Message exceeds 250 characters by "
                    + excess
                    + "; please reduce the size.";
        }
    }

    // Displays the message options and asks the user to choose
    public String SentMessage() {

        java.util.Scanner input =
                new java.util.Scanner(System.in);

        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");

        System.out.print("Choose an option: ");

        String value = input.nextLine().trim();

        try {

            return SentMessage(Integer.parseInt(value));

        } catch (NumberFormatException ex) {

            return "Invalid option. Message was not sent.";
        }
    }

    // Sends, disregards or stores a message
    public String SentMessage(int choice) {

        switch (choice) {

            case 1:

                status = "Sent";

                sentMessages.add(this);

                messagesSent++;

                return "Message successfully sent.";

            case 2:

                status = "Disregarded";

                return "Press 0 to delete the message.";

            case 3:

                status = "Stored";

                try {

                    storeMessage();

                    return "Message successfully stored.";

                } catch (IOException ex) {

                    return "Message could not be stored: "
                            + ex.getMessage();
                }

            default:

                return "Invalid option. Message was not sent.";
        }
    }

    // Displays all messages sent during the current session
    public String printMessages() {

        if (sentMessages.isEmpty()) {

            return "No messages have been sent in this session.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : sentMessages) {

            result.append("Message ID: ")
                    .append(message.messageID)
                    .append('\n');

            result.append("Message Hash: ")
                    .append(message.messageHash)
                    .append('\n');

            result.append("Recipient: ")
                    .append(message.recipientCell)
                    .append('\n');

            result.append("Message: ")
                    .append(message.messageText)
                    .append("\n\n");
        }

        return result.toString().trim();
    }

    // Returns the total number of messages sent
    public int returnTotalMessages() {

        return messagesSent;
    }

    // Stores messages in a JSON file
    public void storeMessage() throws IOException {

        String jsonObject =
                "  {\n"
                + "    \"messageID\": \""
                + jsonEscape(messageID) + "\",\n"

                + "    \"messageNumber\": "
                + messageNumber + ",\n"

                + "    \"messageHash\": \""
                + jsonEscape(messageHash) + "\",\n"

                + "    \"recipient\": \""
                + jsonEscape(recipientCell) + "\",\n"

                + "    \"message\": \""
                + jsonEscape(messageText) + "\",\n"

                + "    \"status\": \""
                + jsonEscape(status) + "\"\n"

                + "  }";

        String existing = Files.exists(JSON_FILE)
                ? Files.readString(
                        JSON_FILE,
                        StandardCharsets.UTF_8
                ).trim()
                : "";

        List<String> objects = new ArrayList<>();

        if (!existing.isEmpty() && !existing.equals("[]")) {

            Matcher matcher = Pattern
                    .compile("(?s)\\{.*?\\}")
                    .matcher(existing);

            while (matcher.find()) {

                objects.add(matcher.group().trim());
            }
        }

        objects.add(jsonObject);

        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < objects.size(); i++) {

            json.append(objects.get(i));

            if (i < objects.size() - 1) {

                json.append(',');
            }

            json.append('\n');
        }

        json.append("]\n");

        Files.writeString(
                JSON_FILE,
                json.toString(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    // Escapes special characters for JSON
    private static String jsonEscape(String value) {

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
