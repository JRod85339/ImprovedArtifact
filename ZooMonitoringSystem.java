package zoomonitoringsystem;

import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class ZooMonitoringSystem {

    public static void main(String[] args) throws IOException {
        try (Scanner scnr = new Scanner(System.in)) {
            String menuOption = "";
            FileReader fileReader = new FileReader();  // Instantiate FileReader
            
            // Main menu loop
            while (!menuOption.equals("q")) {
                // Print main menu
                printMenu("Main Menu", new String[]{
                        "To monitor animals: Enter \"a\".",
                        "To monitor habitats: Enter \"h\".",
                        "To exit the zoo monitoring system: Enter \"q\"."
                });

                // Get and validate user input
                menuOption = scnr.next().toLowerCase();
                
                // Handle menu selection
                switch (menuOption) {
                    case "a":
                        handleMenu(scnr, fileReader, "animals");
                        break;
                    case "h":
                        handleMenu(scnr, fileReader, "habitats");
                        break;
                    case "q":
                        System.out.println("Exiting zoo monitoring system.");
                        break;
                    default:
                        System.out.println("Menu option not recognized.");
                        break;
                }
            }
        }
    }

    /**
     * Handles the menu logic for animals or habitats, including submenus.
     * @param scnr Scanner for user input.
     * @param fileReader The FileReader instance.
     * @param type The type of menu to handle ("animals" or "habitats").
     */
    private static void handleMenu(Scanner scnr, FileReader fileReader, String type) throws IOException {
        String menuOption = "";

        // Retrieve and display available options
        List<String> availableList = fileReader.getAvailableList(type);
        System.out.printf("Available %s:%n", type);
        for (String name : availableList) {
            System.out.println("   " + name);
        }
        System.out.println("Enter \"back\" to return to the main menu.");
        System.out.println();

        // Menu loop for selected type
        while (!menuOption.equals("back")) {
            menuOption = scnr.next().toLowerCase();

            // Validate user input
            if (!menuOption.equals("back")) {
                fileReader.printDetails(type, menuOption);
            }
        }
    }

    /**
     * Utility method to print a menu given a title and options.
     * @param title The title of the menu.
     * @param options The options to be displayed in the menu.
     */
    public static void printMenu(String title, String[] options) {
        System.out.println("     " + title);
        System.out.println("----------------------");
        for (String option : options) {
            System.out.println(option);
        }
        System.out.println();
    }
}
