import java.io.Serializable;

/**
 * Represents a boat with details about its type, name, year of manufacture, make/model, 
 * length, purchase price, and expenses.
 * This class allows for managing expenses within the limits of the boat's purchase price.
 */
public class Boat implements Serializable {

    private static final long serialVersionUID = 1L;

    private BoatType type;
    private String name;
    private int yearOfManufacture;
    private String makeModel;
    private int lengthInFeet;
    private double purchasePrice;
    private double expenses;

    // Default constructor
    public Boat() {
        type = BoatType.SAILING;
        name = "";
        yearOfManufacture = 0;
        makeModel = "";
        lengthInFeet = 0;
        purchasePrice = 0.0;
        expenses = 0.0;
    }

    // Constructor with parameters
    public Boat(BoatType boatType, String nameOfBoat, int year, String model, int length, double price) {
        this.type = boatType;
        this.name = nameOfBoat;
        this.yearOfManufacture = year;
        this.makeModel = model;
        this.lengthInFeet = length;
        this.purchasePrice = price;
        this.expenses = 0.0;
    }

    public String getName() {
        return name;
    }

    public boolean canSpend(double amount) {
        return (expenses + amount) <= purchasePrice;
    }

    public void addExpense(double amount) {
        if (canSpend(amount)) {
            expenses += amount;
        } else {
            throw new IllegalArgumentException("Expense exceeds the allowed limit!");
        }
    }

    public double getExpenses() {
        return expenses;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %4d %-12s %4d' : Paid $%10.2f : Spent $%10.2f",
                type, name, yearOfManufacture, makeModel, lengthInFeet, purchasePrice, expenses);
    }

    public enum BoatType {
        SAILING, POWER
    }
}