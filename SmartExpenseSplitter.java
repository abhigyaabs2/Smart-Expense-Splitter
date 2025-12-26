import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

class Person {
    private String name;
    private BigDecimal balance;

    public Person(String name) {
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addToBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    @Override
    public String toString() {
        return name + " (Balance: " + balance.setScale(2, RoundingMode.HALF_UP) + ")";
    }
}

class Expense {
    private String description;
    private BigDecimal amount;
    private Person paidBy;
    private List<Person> splitAmong;

    public Expense(String description, BigDecimal amount, Person paidBy, List<Person> splitAmong) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitAmong = new ArrayList<>(splitAmong);
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Person getPaidBy() {
        return paidBy;
    }

    public List<Person> getSplitAmong() {
        return splitAmong;
    }

    public BigDecimal getSharePerPerson() {
        return amount.divide(BigDecimal.valueOf(splitAmong.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return description + " - $" + amount.setScale(2, RoundingMode.HALF_UP) +
                " (paid by " + paidBy.getName() + ", split among " + splitAmong.size() + " people)";
    }
}

class Transaction {
    private Person from;
    private Person to;
    private BigDecimal amount;

    public Transaction(Person from, Person to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return from.getName() + " owes " + to.getName() + ": $" +
                amount.setScale(2, RoundingMode.HALF_UP);
    }
}

public class SmartExpenseSplitter {
    private Map<String, Person> people;
    private List<Expense> expenses;

    public SmartExpenseSplitter() {
        this.people = new HashMap<>();
        this.expenses = new ArrayList<>();
    }

    public void addPerson(String name) {
        if (!people.containsKey(name)) {
            people.put(name, new Person(name));
            System.out.println("Added: " + name);
        } else {
            System.out.println(name + " already exists!");
        }
    }

    public void addExpense(String description, double amount, String paidByName, List<String> splitAmongNames) {
        Person paidBy = people.get(paidByName);
        if (paidBy == null) {
            System.out.println("Error: " + paidByName + " not found!");
            return;
        }

        List<Person> splitAmong = new ArrayList<>();
        for (String name : splitAmongNames) {
            Person person = people.get(name);
            if (person == null) {
                System.out.println("Error: " + name + " not found!");
                return;
            }
            splitAmong.add(person);
        }

        if (splitAmong.isEmpty()) {
            System.out.println("Error: No one to split the expense among!");
            return;
        }

        BigDecimal expenseAmount = BigDecimal.valueOf(amount);
        Expense expense = new Expense(description, expenseAmount, paidBy, splitAmong);
        expenses.add(expense);

        BigDecimal sharePerPerson = expense.getSharePerPerson();
        for (Person person : splitAmong) {
            if (person.equals(paidBy)) {
                person.addToBalance(expenseAmount.subtract(sharePerPerson));
            } else {
                person.addToBalance(sharePerPerson.negate());
            }
        }

        System.out.println("Added expense: " + expense);
        System.out.println("Share per person: $" + sharePerPerson);
    }

    public void showExpenses() {
        System.out.println("\n=== All Expenses ===");
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    public void showBalances() {
        System.out.println("\n=== Current Balances ===");
        for (Person person : people.values()) {
            BigDecimal balance = person.getBalance();
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println(person.getName() + " is owed: $" + balance.setScale(2, RoundingMode.HALF_UP));
            } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println(person.getName() + " owes: $" + balance.abs().setScale(2, RoundingMode.HALF_UP));
            } else {
                System.out.println(person.getName() + " is settled up");
            }
        }
    }

    public void calculateSettlements() {
        System.out.println("\n=== Settlement Plan (Optimized) ===");

        List<Person> creditors = new ArrayList<>();
        List<Person> debtors = new ArrayList<>();

        for (Person person : people.values()) {
            BigDecimal balance = person.getBalance();
            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(person);
            } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(person);
            }
        }

        if (creditors.isEmpty() && debtors.isEmpty()) {
            System.out.println("Everyone is settled up!");
            return;
        }

        creditors.sort((a, b) -> b.getBalance().compareTo(a.getBalance()));
        debtors.sort((a, b) -> a.getBalance().compareTo(b.getBalance()));

        List<Transaction> transactions = new ArrayList<>();
        int i = 0, j = 0;

        while (i < creditors.size() && j < debtors.size()) {
            Person creditor = creditors.get(i);
            Person debtor = debtors.get(j);

            BigDecimal creditAmount = creditor.getBalance();
            BigDecimal debtAmount = debtor.getBalance().abs();

            BigDecimal settlementAmount = creditAmount.min(debtAmount);
            transactions.add(new Transaction(debtor, creditor, settlementAmount));

            creditor.addToBalance(settlementAmount.negate());
            debtor.addToBalance(settlementAmount);

            if (creditor.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                i++;
            }
            if (debtor.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                j++;
            }
        }

        if (transactions.isEmpty()) {
            System.out.println("No settlements needed!");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println("💰 " + transaction);
            }
        }
    }

    public static void main(String[] args) {
        SmartExpenseSplitter splitter = new SmartExpenseSplitter();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("  Smart Expense Splitter");
        System.out.println("=================================\n");

        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add person");
            System.out.println("2. Add expense");
            System.out.println("3. Show all expenses");
            System.out.println("4. Show balances");
            System.out.println("5. Calculate settlements");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter person's name: ");
                    String name = scanner.nextLine().trim();
                    splitter.addPerson(name);
                    break;

                case 2:
                    System.out.print("Enter expense description: ");
                    String description = scanner.nextLine().trim();

                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Who paid? ");
                    String paidBy = scanner.nextLine().trim();

                    System.out.print("Split among (comma-separated names): ");
                    String splitInput = scanner.nextLine().trim();
                    List<String> splitAmong = Arrays.asList(splitInput.split("\\s*,\\s*"));

                    splitter.addExpense(description, amount, paidBy, splitAmong);
                    break;

                case 3:
                    splitter.showExpenses();
                    break;

                case 4:
                    splitter.showBalances();
                    break;

                case 5:
                    splitter.calculateSettlements();
                    break;

                case 6:
                    running = false;
                    System.out.println("Thanks for using Smart Expense Splitter!");
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }

        scanner.close();
    }
}
