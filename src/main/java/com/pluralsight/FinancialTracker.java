package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    private static final LocalDateTime NOW = LocalDateTime.now();


    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
        scanner.close();
    }

    public static void loadTransactions(String fileName) {

        String input;
        try {
            //Read the .csv file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((input = bufferedReader.readLine()) != null) {

                String[] strings = input.split("\\|");

                //Add the read line as an object to the transactions list
                transactions.add(new Transaction(LocalDate.parse(strings[0]), LocalTime.parse(strings[1]), strings[2], strings[3], Double.parseDouble(strings[4])));

            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error while reading the file");
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;
        try {
            //Prompt for the deposit details
            System.out.println("Enter the date of the deposit (yyyy-MM-dd): ");
            date = LocalDate.parse(LocalDate.parse(scanner.nextLine().trim()).format(DATE_FORMATTER));

            System.out.println("Enter the time of the deposit (HH:mm:ss)");
            time = LocalTime.parse(LocalTime.parse(scanner.nextLine().trim()).format(TIME_FORMATTER));

            System.out.println("Enter the description of the deposit: ");
            description = scanner.nextLine().trim();

            System.out.println("Enter the vendor of the deposit: ");
            vendor = scanner.nextLine().trim();

            System.out.println("Enter the amount of the deposit: ");
            amount = scanner.nextDouble();
            scanner.nextLine();

            //Check for invalid amount input
            if (amount <= 0) {
                System.out.println("Cannot enter an amount less than or equal to 0");
                return;
            }

            //Create a transaction with the input and add it to the transaction list
            Transaction transaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(transaction);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bufferedWriter.write(transaction.toString());

            bufferedWriter.close();

        } catch (Exception e) {
            System.err.println("Error occurred entering the deposit");
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;
        try {
            //Prompt the user for payment details
            System.out.println("Enter the date of the payment (yyyy-MM-dd): ");
            date = LocalDate.parse(LocalDate.parse(scanner.nextLine().trim()).format(DATE_FORMATTER));

            System.out.println("Enter the time of the payment (HH:mm:ss)");
            time = LocalTime.parse(LocalTime.parse(scanner.nextLine().trim()).format(TIME_FORMATTER));

            System.out.println("Enter the description of the payment: ");
            description = scanner.nextLine().trim();

            System.out.println("Enter the vendor of the payment: ");
            vendor = scanner.nextLine().trim();

            System.out.println("Enter the amount of the payment: ");
            amount = -(scanner.nextDouble());
            scanner.nextLine();

            //Check for the invalid input
            if (amount >= 0) {
                System.out.println("Cannot enter an amount less than or equal to 0");
                return;
            }

            //Create a new Transaction object with the input and add it to list
            Transaction transaction = new Transaction(date, time, description, vendor, (amount));
            transactions.add(transaction);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bufferedWriter.write(transaction.toString());

            bufferedWriter.close();

        } catch (Exception e) {
            System.err.println("Error occurred writing the payment");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("C) Custom Search");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "C":
                    customSearch(scanner);
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Table of All Transactions");
        System.out.println("date | time | description | vendor | amount");

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Table of Deposit Transactions");
        System.out.println("date | time | description | vendor | amount");

        //Iterate through the list and print out deposits by validating if they are positive
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction.toString());
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Table of Payment Transactions");
        System.out.println("date | time | description | vendor | amount");

        //Iterate through the list and print out payments by validating if they are negative
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.toString());
            }
        }
    }

    private static void customSearch(Scanner scanner) {

        System.out.println("Enter the values you want to search for.\nLeave a value empty for the values you don't want to filter for.");

        //Prompt for custom search values
        LocalDate startDate = null;
        try {
            System.out.println("Enter Start Date");
            startDate = LocalDate.parse(LocalDate.parse(scanner.nextLine().trim()).format(DATE_FORMATTER));
        } catch (Exception ignored) {
        }
        LocalDate endDate = null;
        try {
            System.out.println("Enter End Date: ");
            endDate = LocalDate.parse(LocalDate.parse(scanner.nextLine().trim()).format(DATE_FORMATTER));
        } catch (Exception ignored) {
        }
        System.out.println("Enter Description: ");
        String description = scanner.nextLine().trim();
        System.out.println("Enter Vendor");
        String vendor = scanner.nextLine().trim();
        System.out.println("Enter Amount");
        String amountInput = scanner.nextLine();
        Double amount = returnIfDouble(amountInput);

        List<Transaction> filteredList = transactions;

           /* Filter the list by;
            First checking if the search value exists
            Then remove the unmatched objects from the list
            Result is a list of objects filtered down    */
        if (startDate != null) {
            for (Transaction transaction : filteredList) {
                if (transaction.getDate().isBefore(startDate)) {
                    filteredList.remove(transaction);
                }
            }
        }

        if (endDate != null) {
            for (Transaction transaction : filteredList) {
                if (transaction.getDate().isAfter(endDate)) {
                    filteredList.remove(transaction);
                }
            }
        }

        if (!(description.isEmpty())) {
            for (Transaction transaction : filteredList) {
                if (!(transaction.getDescription().equalsIgnoreCase(description))) {
                    filteredList.remove(transaction);
                }
            }
        }

        if (!(vendor.isEmpty())) {
            for (Transaction transaction : filteredList) {
                if (!(transaction.getVendor().equalsIgnoreCase(vendor))) {
                    filteredList.remove(transaction);
                }
            }
        }

        if (amount != null) {
            for (Transaction transaction : filteredList) {
                if (Math.abs(transaction.getAmount()) != amount) {
                    filteredList.remove(transaction);
                }
            }
        }

        System.out.println("Filtered result: ");
        for (Transaction transaction : filteredList) {
            System.out.println(transaction.toString());
        }
    }

    public static Double returnIfDouble(String input){

        if (input.isEmpty()){
            return null;
        } else {
            return Double.parseDouble(input);
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    filterTransactionsByDate(NOW.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalDate.from(NOW));
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    filterTransactionsByDate(NOW.toLocalDate().minusMonths(1).minusDays((NOW.getDayOfMonth()) - 1), NOW.toLocalDate().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
                    break;

                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    filterTransactionsByDate(NOW.toLocalDate().with(TemporalAdjusters.firstDayOfYear()), NOW.toLocalDate());
                    break;

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    filterTransactionsByDate(NOW.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().minusYears(1), NOW.with(TemporalAdjusters.firstDayOfYear()).toLocalDate());
                    break;

                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                    System.out.println("Enter the Vendor you want to search for: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                    break;

                case "0":
                    running = false;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        System.out.println("Transactions between " + startDate + " - " + endDate + ": \n");

        for (Transaction transaction : transactions) {

            if (transaction.getDate().isAfter(startDate.minusDays(1)) && transaction.getDate().isBefore(endDate.plusDays(1))) {
                System.out.println(transaction);
            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.

        System.out.println("Transactions by the \"" + vendor + "\": \n");
        StringBuilder stringBuilder = new StringBuilder();

        for (Transaction transaction : transactions) {

            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                stringBuilder.append("\n" + transaction.toString());
            }
        }

        if (stringBuilder.isEmpty()){
            System.out.println("No results found!");
        }
    }
}