import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a fleet of boats with functionality for adding, removing, retrieving boats,
 * and calculating the total purchase cost and expenses of all boats in the fleet.
 * The fleet is stored as an ArrayList of Boat objects.
 *
 * @author Muhammad Wamiq
 */
public class Fleet implements Serializable {
    private static final long serialVersionUID = 1L;  // Ensures version compatibility for serialization

    /** List of boats in the fleet */
    private ArrayList<Boat> boats;

    /**
     * Constructor that initializes the fleet with an empty list of boats.
     */
    public Fleet() {
        boats = new ArrayList<>();
    }

    /**
     * Adds a new boat to the fleet.
     * @param boat The boat to be added to the fleet
     */
    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    /**
     * Removes a boat from the fleet by its name.
     * @param name The name of the boat to be removed
     * @return True if the boat was found and removed, false otherwise
     */
    public boolean removeBoat(String name) {
        // Attempts to remove the boat based on its name
        return boats.removeIf(boat -> boat.getName().equalsIgnoreCase(name));
    }

    /**
     * Retrieves a boat from the fleet by its name.
     * @param name The name of the boat to retrieve
     * @return The boat with the specified name, or null if not found
     */
    public Boat getBoatByName(String name) {
        for (Boat boat : boats) {
            if (boat.getName().equalsIgnoreCase(name)) {
                return boat;
            }
        }
        return null;  // Return null if no boat is found by the name
    }

    /**
     * Calculates the total amount spent on all boats in the fleet.
     * @return The total expenses for all boats in the fleet
     */
    public double totalSpent() {
        return boats.stream().mapToDouble(Boat::getExpenses).sum();
    }

    /**
     * Calculates the total purchase cost of all boats in the fleet.
     * @return The total purchase cost for all boats in the fleet
     */
    public double totalPurchaseCost() {
        return boats.stream().mapToDouble(Boat::getPurchasePrice).sum();
    }

    /**
     * Returns the list of all boats in the fleet.
     * @return The list of boats in the fleet
     */
    public ArrayList<Boat> getBoats() {
        return boats;
    }

    /**
     * Returns a string representation of the fleet, including details of each boat
     * and the total purchase cost and expenses for the fleet.
     * @return A formatted string representing the fleet
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Fleet Report:\n");

        // Append details of each boat in the fleet
        for (Boat boat : boats) {
            sb.append(" ").append(boat.toString()).append("\n");
        }

        // Append the total cost and expenses of the fleet
        sb.append(String.format(" Total Fleet: Paid $%10.2f : Spent $%10.2f\n", totalPurchaseCost(), totalSpent()));

        return sb.toString();  // Return the full report
    }
}
