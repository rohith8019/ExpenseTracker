import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private List<Expense> expenses;
    private Map<String, String> users;
    private Scanner scanner;

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.run();
    }

    public ExpenseTracker() {
        expenses = new ArrayList<>();
        users = new HashMap<>();
        scanner = new Scanner(System.in);

        loadUsers();
        loadExpenses();
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to Expense Tracker!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Login successful!");
            userMenu(username);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!users.containsKey(username)) {
            users.put(username, password);
            saveUsers();
            System.out.println("Registration successful!");
        } else {
            System.out.println("Username already exists. Please try again.");
        }
    }

    private void userMenu(String username) {
        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. List Expenses");
            System.out.println("3. Category-wise Summation");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addExpense(username);
                    break;
                case 2:
                    listExpenses(username);
                    break;
                case 3:
                    categoryWiseSummation(username);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addExpense(String username) {
        System.out.print("Enter date (DD-MM-YYYY) : ");
        String date = scanner.nextLine();
        System.out.print("Enter expense name: ");
        String name = scanner.nextLine();
        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Expense expense = new Expense(date, name, amount, username);
        expenses.add(expense);

        saveExpenses();

        System.out.println("Expense added successfully.");
    }

    private void listExpenses(String username) {
        System.out.println("Expenses:");
        for (Expense expense : expenses) {
            if (expense.getUsername().equals(username)) {
                System.out.println(expense.getdate() +  "," + expense.getName() + ": $" + expense.getAmount());
            }
        }
    }

    private void categoryWiseSummation(String username) {
        Map<String, Double> categorySummation = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getUsername().equals(username)) {
                String category = expense.getCategory();
                double amount = expense.getAmount();

                if (!categorySummation.containsKey(category)) {
                    categorySummation.put(category, amount);
                } else {
                    categorySummation.put(category, categorySummation.get(category) + amount);
                }
            }
        }

        System.out.println("Category-wise Summation:");
        for (Map.Entry<String, Double> entry : categorySummation.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine())!= null) {
                String[] parts = line.split(",");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader("expenses.txt"))) {
            String line;
            while ((line = reader.readLine())!= null) {
                String[] parts = line.split(",");
                Expense expense = new Expense(parts[0], parts[1],Double.parseDouble(parts[2]), parts[3]);
                expenses.add(expense);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("expenses.txt"))) {
            for (Expense expense : expenses) {
                writer.write(expense.getdate() + "," + expense.getName() + "," + expense.getAmount() + "," + expense.getUsername() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    class Expense {
        private String name;
        private double amount;
        private String category;
        private String username;
        private String date;
    
        public Expense(String date, String name, double amount, String username) {
            this.date = date;
            this.name = name;
            this.amount = amount;
            this.category = "Uncategorized";
            this.username = username;
        }
        public String getdate(){
            return date;
        }
        public String getName() {
            return name;
        }
    
        public double getAmount() {
            return amount;
        }
    
        public String getCategory() {
            return category;
        }
    
        public void setCategory(String category) {
            this.category = category;
        }
    
        public String getUsername() {
            return username;
        }
    }
}