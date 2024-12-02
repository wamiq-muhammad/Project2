import java.io.*;
import java.util.Scanner;

//========================================================================================================
/**
 * Manages the fleet of boats, including loading and saving fleet data, displaying the menu,
 * and handling user inputs to perform actions such as printing boat information, adding a boat,
 * removing a boat, or managing boat expenses.
 * @author Muhammad Wamiq
 */
public class FleetManagement {

    //----------------------------------------------------------------------------------------------------
    private static final Scanner keyboard = new Scanner(System.in);

    //----------------------------------------------------------------------------------------------------
    private static final String DB_FILE = "FleetData.db";

    //----------------------------------------------------------------------------------------------------
    /**
     * The main method that initializes the fleet, loads data, and starts the menu system.
     * @param args Command line arguments that may contain the CSV file location for initializing data
     */
    public static void main(String[] args) {
        Fleet fleet = new Fleet();
        loadFleetData(args, fleet);
        displayMenu(fleet);
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Loads the fleet data either from a CSV file (if provided as a command line argument)
     * or from the serialized database file.
     * @param args Command line arguments (CSV file location)
     * @param fleet The fleet object to load data into
     */
    public static void loadFleetData(String[] args, Fleet fleet) {
        if (args.length > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    Boat boat = new Boat( Boat.BoatType.valueOf(data[0].toUpperCase()), data[1],Integer.parseInt(data[2]),
                            data[3], Integer.parseInt(data[4]), Double.parseDouble(data[5]) );
                    fleet.addBoat(boat);
                }
                saveFleetData(fleet); // Save fleet data after loading from CSV
            } catch (IOException e) {
                System.out.println("Error reading CSV file. Starting with an empty fleet.");
            }
        } else {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(DB_FILE))) {
                Fleet loadedFleet = (Fleet) input.readObject();
                fleet.getBoats().addAll(loadedFleet.getBoats());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No existing database found. Starting with an empty fleet.");
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Saves the current fleet data to a serialized database file.
     * @param fleet The fleet object to be saved
     */
    public static void saveFleetData(Fleet fleet) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(DB_FILE));
            out.writeObject(fleet);
        } catch (IOException e) {
            System.out.println("Error saving database file.");
        } finally {
            if (out != null) {
                try {
                    out.close(); // Ensure the output stream is closed
                } catch (IOException e) {
                    System.out.println("Error closing the database file.");
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
    /**
     * Displays the main menu for the fleet management system, allowing the user to select
     * actions such as printing boat details, adding a boat, removing a boat, or managing expenses.
     * @param fleet The fleet object containing the boats to manage
     */
    public static void displayMenu(Fleet fleet) {
        String option;
        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            option = keyboard.nextLine().trim().toUpperCase();
            switch (option) {
                case "P":
                    System.out.println();
                    System.out.println(fleet);
                    break;
                case "A":
                    System.out.print("Please enter the new boat CSV data : ");
                    String csvData = keyboard.nextLine();
                    try {
                        String[] data = csvData.split(",");
                        Boat newBoat = new Boat(
                                Boat.BoatType.valueOf(data[0].toUpperCase()),
                                data[1],
                                Integer.parseInt(data[2]),
                                data[3],
                                Integer.parseInt(data[4]),
                                Double.parseDouble(data[5])
                        );
                        fleet.addBoat(newBoat);
                    } catch (Exception e) {
                        System.out.println("Invalid boat data. Please try again.");
                    }
                    System.out.println();
                    break;
                case "R":
                    System.out.print("Which boat do you want to remove? : ");
                    String nameToRemove = keyboard.nextLine();
                    if (!fleet.removeBoat(nameToRemove)) {
                        System.out.println("Cannot find boat " + nameToRemove);
                    }
                    System.out.println();
                    break;
                case "E":
                    System.out.print("Which boat do you want to spend on? : ");
                    String nameToSpend = keyboard.nextLine();
                    Boat boat = fleet.getBoatByName(nameToSpend);
                    if (boat == null) {
                        System.out.println("Cannot find boat " + nameToSpend);
                        System.out.println();
                        break;
                    }
                    System.out.print("How much do you want to spend? : ");
                    try {
                        double amount = Double.parseDouble(keyboard.nextLine());
                        if (boat.canSpend(amount)) {
                            boat.addExpense(amount);
                            System.out.printf("Expense authorized, $%.2f spent.\n", boat.getExpenses());
                        } else {
                            System.out.printf("Expense not permitted, only $%.2f left to spend.\n",
                                    boat.getPurchasePrice() - boat.getExpenses());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Please try again.");
                    }
                    System.out.println();
                    break;
                case "X":
                    saveFleetData(fleet);
                    System.out.println();
                    System.out.println("Exiting the Fleet Management System");
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
            }
        } while (!option.equals("X"));
    }
}