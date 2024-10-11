package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((input = bufferedReader.readLine()) != null) {

                String[] strings = input.split("\\|");

                transactions.add(new Transaction(LocalDate.parse(strings[0]), LocalTime.parse(strings[1]), strings[2], strings[3], Double.parseDouble(strings[4])));
            }
        } catch (IOException e) {
            System.err.println("Error while reading the file");
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
            System.out.println("Enter the date of the deposit (yyyy-MM-dd): ");
            date = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Enter the time of the deposit (HH:mm:ss)");
            time = LocalTime.parse(scanner.nextLine().trim());

            System.out.println("Enter the description of the deposit: ");
            description = scanner.nextLine().trim();

            System.out.println("Enter the vendor of the deposit: ");
            vendor = scanner.nextLine().trim();

            System.out.println("Enter the amount of the deposit: ");
            amount = scanner.nextDouble();
            scanner.nextLine();

            if (amount <= 0) {
                System.out.println("Cannot enter an amount less than or equal to 0");
                return;
            }

        } catch (Exception e) {
            System.err.println("Error occurred entering the deposit");
            return;
        }

        transactions.add(new Transaction(date, time, description, vendor, amount));

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
            System.out.println("Enter the date of the payment (yyyy-MM-dd): ");
            date = LocalDate.parse(scanner.nextLine().trim());

            System.out.println("Enter the time of the payment (HH:mm:ss)");
            time = LocalTime.parse(scanner.nextLine().trim());

            System.out.println("Enter the description of the payment: ");
            description = scanner.nextLine().trim();

            System.out.println("Enter the vendor of the payment: ");
            vendor = scanner.nextLine().trim();

            System.out.println("Enter the amount of the payment: ");
            amount = -(scanner.nextDouble());
            scanner.nextLine();

            if (amount >= 0) {
                System.out.println("Cannot enter an amount less than or equal to 0");
                return;
            }

        } catch (Exception e) {
            System.err.println("Error occurred entering the payment");
            return;
        }

        transactions.add(new Transaction(date, time, description, vendor, amount));
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

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.toString());
            }
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
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
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

            if (transaction.getDate().isAfter(startDate) && transaction.getDate().isBefore(endDate)) {

                System.out.println(transaction.toString());
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

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)){

                System.out.println(transaction.toString());
            }
        }
    }
}