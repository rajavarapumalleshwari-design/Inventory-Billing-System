import java.util.*;

class Item {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Item(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int quantity) {
        stock += quantity;
    }

    public boolean reduceStock(int quantity) {
        if (quantity <= stock) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | %-15s | Price: ₹%.2f | Stock: %d",
                id, name, price, stock
        );
    }
}

public class InventoryBillingSystem {

    // HashMap provides fast item lookup using item ID
    private static Map<Integer, Item> inventory = new HashMap<>();

    // ArrayList stores items added to the current bill
    private static List<Item> billItems = new ArrayList<>();

    // Stores quantities for items in the current bill
    private static Map<Integer, Integer> billQuantities = new HashMap<>();

    // Add a new inventory item
    public static void addItem(Item item) {

        if (inventory.containsKey(item.getId())) {
            System.out.println("Item ID already exists.");
            return;
        }

        if (item.getPrice() < 0 || item.getStock() < 0) {
            System.out.println("Price and stock cannot be negative.");
            return;
        }

        inventory.put(item.getId(), item);
        System.out.println("Item added successfully.");
    }

    // Update stock
    public static void updateStock(int id, int quantity) {

        Item item = inventory.get(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (quantity < 0 && Math.abs(quantity) > item.getStock()) {
            System.out.println("Cannot reduce stock below zero.");
            return;
        }

        item.updateStock(quantity);

        System.out.println("Stock updated successfully.");
        System.out.println("Current stock: " + item.getStock());
    }

    // Display inventory
    public static void displayInventory() {

        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("\n========== INVENTORY ==========");

        for (Item item : inventory.values()) {
            System.out.println(item);
        }

        System.out.println("===============================");
    }

    // Add item to current bill
    public static void addToBill(int id, int quantity) {

        Item item = inventory.get(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (quantity > item.getStock()) {
            System.out.println(
                    "Insufficient stock. Available: " +
                    item.getStock()
            );
            return;
        }

        // If item is already in bill, increase its quantity
        if (billQuantities.containsKey(id)) {

            int currentQuantity = billQuantities.get(id);
            billQuantities.put(id, currentQuantity + quantity);

        } else {

            billItems.add(item);
            billQuantities.put(id, quantity);
        }

        // Reduce stock after adding to bill
        item.reduceStock(quantity);

        System.out.println(
                quantity + " x " + item.getName() +
                " added to bill."
        );
    }

    // Generate final bill
    public static void generateBill() {

        if (billItems.isEmpty()) {
            System.out.println("No items in the bill.");
            return;
        }

        double subtotal = 0;

        System.out.println("\n==============================================");
        System.out.println("                FINAL BILL");
        System.out.println("==============================================");
        System.out.printf(
                "%-15s %-8s %-12s %-12s%n",
                "Item", "Qty", "Price", "Amount"
        );
        System.out.println("----------------------------------------------");

        for (Item item : billItems) {

            int quantity = billQuantities.get(item.getId());
            double amount = item.getPrice() * quantity;

            subtotal += amount;

            System.out.printf(
                    "%-15s %-8d ₹%-11.2f ₹%-11.2f%n",
                    item.getName(),
                    quantity,
                    item.getPrice(),
                    amount
            );
        }

        double tax = subtotal * 0.05;
        double total = subtotal + tax;

        System.out.println("----------------------------------------------");
        System.out.printf("Subtotal:       ₹%.2f%n", subtotal);
        System.out.printf("GST (5%%):       ₹%.2f%n", tax);
        System.out.printf("Total Amount:   ₹%.2f%n", total);
        System.out.println("==============================================");
        System.out.println("       Thank you for shopping!");
        System.out.println("==============================================");

        // Clear bill after generating
        billItems.clear();
        billQuantities.clear();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== INVENTORY BILLING SYSTEM =====");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Stock");
            System.out.println("4. Add Item to Bill");
            System.out.println("5. Generate Bill");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Item ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Item Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Price: ₹");
                    double price = sc.nextDouble();

                    System.out.print("Enter Stock Quantity: ");
                    int stock = sc.nextInt();

                    Item item = new Item(id, name, price, stock);
                    addItem(item);
                    break;

                case 2:
                    displayInventory();
                    break;

                case 3:
                    System.out.print("Enter Item ID: ");
                    int stockId = sc.nextInt();

                    System.out.print(
                            "Enter quantity to add/remove " +
                            "(use negative to remove): "
                    );
                    int quantity = sc.nextInt();

                    updateStock(stockId, quantity);
                    break;

                case 4:
                    displayInventory();

                    System.out.print("Enter Item ID: ");
                    int billId = sc.nextInt();

                    System.out.print("Enter Quantity: ");
                    int billQuantity = sc.nextInt();

                    addToBill(billId, billQuantity);
                    break;

                case 5:
                    generateBill();
                    break;

                case 6:
                    System.out.println(
                            "Thank you for using the system!"
                    );
                    sc.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }
}