package zoomonitoringsystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class FileReader {
    private String animalsFilePath;
    private String habitatsFilePath;
    private static final String CONFIG_FILE_NAME = "config.properties"; // Name of the properties file

    /**
     * Constructor to initialize file paths from the config file.
     */
    public FileReader() {
        loadConfig();
    }

    /**
     * Method to load configuration from the properties file.
     */
    private void loadConfig() {
        Properties properties = new Properties();

        // Use getClassLoader().getResourceAsStream() to load the properties file from the classpath
        try (InputStream configStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (configStream == null) {
                throw new IOException("Config file not found: " + CONFIG_FILE_NAME);
            }

            properties.load(configStream);
            animalsFilePath = properties.getProperty("animalsFilePath");
            habitatsFilePath = properties.getProperty("habitatsFilePath");
        } catch (IOException e) {
            System.err.printf("Error loading config file: %s%n", e.getMessage());
        }
    }

    /**
     * Method to get the list of available animals or habitats.
     * @param type The type of list to retrieve ("animals" or "habitats").
     * @return A list of available animals or habitats.
     */
    public List<String> getAvailableList(String type) {
        List<String> availableList = new ArrayList<>();
        String filePath = getFilePath(type);

        try (InputStream fileByteStream = getClass().getClassLoader().getResourceAsStream(filePath);
             Scanner inFS = new Scanner(fileByteStream)) {

            // Read and collect options
            while (inFS.hasNextLine()) {
                String line = inFS.nextLine();
                if (line.startsWith("Details")) {
                    Scanner inSS = new Scanner(line);
                    String name = extractName(inSS, type);
                    availableList.add(name);
                }
            }
        } catch (IOException e) {
            System.err.printf("Error reading %s file: %s%n", type, e.getMessage());
        }

        return availableList;
    }

    /**
     * Method to print details of the selected animal or habitat.
     * @param type The type of detail to print ("animals" or "habitats").
     * @param menuOption The selected name of animal or habitat.
     */
    public void printDetails(String type, String menuOption) {
        String filePath = getFilePath(type);
        String formattedName = formatInput(menuOption);

        try (InputStream fileByteStream = getClass().getClassLoader().getResourceAsStream(filePath);
             Scanner inFS = new Scanner(fileByteStream)) {

            System.out.printf("%s %s Details:%n%n", formattedName, capitalize(type));
            boolean found = false;

            // Search for the selected name and display details
            while (inFS.hasNextLine()) {
                String line = inFS.nextLine();
                if (line.endsWith(formattedName)) {
                    System.out.println(line);
                    found = true;
                    printSubDetails(inFS);
                    break;
                }
            }

            if (!found) {
                System.out.printf("Error: %s not found in system.%n", formattedName);
            }

        } catch (IOException e) {
            System.err.printf("Error reading %s file: %s%n", type, e.getMessage());
        }
    }

    /**
     * Helper method to extract the name from a Scanner.
     * @param inSS The Scanner object.
     * @param type The type ("animals" or "habitats").
     * @return The extracted name.
     */
    private String extractName(Scanner inSS, String type) {
        String name = "";
        while (inSS.hasNext()) {
            String word = inSS.next();
            if (!word.equals("Details") && !word.equals("on")) {
                name = type.equals("animals") ? word.substring(0, word.length() - 1) : word;
                break;
            }
        }
        return name;
    }

    /**
     * Helper method to print sub-details of an animal or habitat.
     * @param inFS The Scanner object for file input.
     */
    private void printSubDetails(Scanner inFS) {
        while (inFS.hasNextLine()) {
            String subLine = inFS.nextLine();
            if (subLine.isEmpty()) {
                break;
            }
            if (subLine.startsWith("*")) {
                displayWarning(subLine);
            }
            System.out.println(subLine);
        }
    }

    /**
     * Helper method to display warning messages.
     * @param subLine The warning line from the file.
     */
    private void displayWarning(String subLine) {
        subLine = subLine.replace('*', ' ').trim();
        String category = subLine.substring(0, subLine.indexOf(":"));
        String message = subLine.substring(subLine.indexOf(":") + 2);
        JOptionPane.showMessageDialog(null, message, "Warning! " + category, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Helper method to get the file path based on type.
     * @param type The type ("animals" or "habitats").
     * @return The file path.
     */
    private String getFilePath(String type) {
        return type.equals("animals") ? animalsFilePath : habitatsFilePath;
    }

    /**
     * Helper method to format input string to proper case.
     * @param input The input string.
     * @return The formatted string.
     */
    private String formatInput(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Helper method to capitalize a string.
     * @param input The input string.
     * @return The capitalized string.
     */
    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
