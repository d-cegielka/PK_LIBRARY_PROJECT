package org.pk.library.view;

import org.pk.library.controller.Controller;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class TUI {
    Controller libraryController;
    private final ColoredPrinter purpleTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.MAGENTA)
            .build();
    private final ColoredPrinter blueTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.BLUE)
            .build();
    private final ColoredPrinter yellowTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.YELLOW)
            .build();
    private final ColoredPrinter cyanTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.CYAN)
            .build();
    private final ColoredPrinter greenTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.GREEN)
            .build();
    private final ColoredPrinter redTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.RED)
            .build();
    private final ColoredPrinter blackTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.BLACK)
            .build();

    public TUI() {
        try {
            libraryController = new Controller();
        } catch (SQLException | IOException se) {
            System.out.println("Blad SQL");
            System.out.println(se.getMessage());
        }
        drawMainMenu();
    }

    private static class CLS {
        public static void main(String... arg) throws IOException, InterruptedException {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    private void clearTerminal() {
        try {
            CLS.main();
        } catch (IOException | InterruptedException ignore) {
        }
    }

    private void drawMainMenu() {
        clearTerminal();
        drawMenu("Menu glowne", new String[]{"Ksiazki", "Czytelnicy", "Wypozyczenia", "Eksport do XML","Import z XML", "O programie", "Zamknij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                drawBooksMenu();
                break;
            case '2':
                //drawReadersMenu();
                break;
            case '3':
               // drawRentsMenu();
                break;
            case '4':
                drawExportToXMLMenu();
                break;
            case '5':
                drawImportFromXMLMenu();
                break;
            case '6':
                drawAbout();
                break;
            case '7':
                /*try {
                    Serialization.write(dashboard);
                } catch (IOException e) {
                    redTextColor.println("\t" + e.getMessage());
                }*/
                System.exit(0);
                break;
            default:
                redTextColor.println("\n\tZly wybor!");
                waitForEnter(true);
        }
    }
    
    private void drawMenu(String title, String[] options) {
        int width = 36;
        int len = title.length();
        String repeat = "=".repeat((width - len) / 2);
        purpleTextColor.print("\n\t" + repeat);
        yellowTextColor.print(" " + title + " ");
        purpleTextColor.println(repeat);
        width = 2 * repeat.length() + len + 2;
        purpleTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        for (int i = 0; i < options.length; i++) {
            purpleTextColor.print("\t|  ");
            blueTextColor.print((i + 1) + ") ");
            cyanTextColor.print(options[i]);
            purpleTextColor.println(" ".repeat(width - 7 - options[i].length()) + "|");
        }
        purpleTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        purpleTextColor.println("\t" + "=".repeat(width));
        yellowTextColor.print("\n\tEnter choice: ");
    }

    private void drawBooksMenu(){
        clearTerminal();

    }

    private void drawExportToXMLMenu() {
        clearTerminal();
        drawMenu("Eksport Menu", new String[]{"Eksportuj", "Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            /*case '1':
                yellowTextColor.println("\n\tEnter file path: ");
                try {
                    System.out.print("\t");
                    dashboard.writeToXml(scanner.next());
                    greenTextColor.println("\n\tSuccesfully exported to XML file!");
                    waitForEnter(true);
                } catch (IOException | XMLStreamException e) {
                    redTextColor.println("\n\tExport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    drawExportMenu();
                }
                break;*/
            case '2':
                try {
                    //dashboard.writeToDB();

                    greenTextColor.println("\n\tSuccesfully exported to database!");
                    waitForEnter(true);
                } catch (Exception e) {
                    redTextColor.println("\n\tExport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    //drawExportMenu();
                }
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(false);
                //drawExportMenu();
        }
    }

    private void drawImportFromXMLMenu() {
        clearTerminal();
        drawMenu("Import Menu", new String[]{"Importuj", "Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            /*case '1':
                yellowTextColor.println("\n\tEnter file path: ");
                try {
                    System.out.print("\t");
                    dashboard.readFromXml(scanner.next());
                    greenTextColor.println("\n\tSuccesfully imported dashboard data from XML file!");
                    waitForEnter(true);
                } catch (IOException | XMLStreamException e) {
                    redTextColor.println("\n\tImport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    drawImportMenu();
                }
                break;*/
            case '2':
                //dashboard.updateDashboard(chooseDBRecord());
                greenTextColor.println("\n\tSuccesfully imported dashboard data from database!");
                waitForEnter(true);
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(false);
               // drawImportMenu();
        }
    }

    private void drawAbout() {
        clearTerminal();
        purpleTextColor.print("\n\n\t+----------------------------------------------------------------------------------------------------------+\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|         ");
        yellowTextColor.print("/$$$$$$$                      /$$       /$$                                           /$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$__  $$                    | $$      | $$                                          | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  \\ $$  /$$$$$$   /$$$$$$$| $$$$$$$ | $$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ |____  $$ /$$_____/| $$__  $$| $$__  $$ /$$__  $$ |____  $$ /$$__  $$ /$$__  $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$  /$$$$$$$|  $$$$$$ | $$  \\ $$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\__/| $$  | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ /$$__  $$ \\____  $$| $$  | $$| $$  | $$| $$  | $$ /$$__  $$| $$      | $$  | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$$$$$$/|  $$$$$$$ /$$$$$$$/| $$  | $$| $$$$$$$/|  $$$$$$/|  $$$$$$$| $$      |  $$$$$$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("|_______/  \\_______/|_______/ |__/  |__/|_______/  \\______/  \\_______/|__/       \\_______/");
        purpleTextColor.print("        |\n\t|                                                                                                          |\n" +
                "\t|                                                                                     ");
        redTextColor.print("Version 1.0.0");
        purpleTextColor.print("        |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                      ");
        blueTextColor.print("+---------------+");
        purpleTextColor.print("   |\n\t|                                                                                      ");
        cyanTextColor.print("|A|u|t|o|r|z|y|:|");
        purpleTextColor.print("   |\n\t|                                                      ");
        blueTextColor.print("+---------------------------------+ +-----------+");
        purpleTextColor.print("   |\n\t|                                                      ");
        cyanTextColor.print("|D|o|m|i|n|i|k| |C|e|g|i|e|l|k|a| |2|2|4|4|7|8|");
        purpleTextColor.print("   |\n\t|                                                      ");
        blueTextColor.print("+-----------------------------------------------+");
        purpleTextColor.print("   |\n\t|                                                                  ");
        cyanTextColor.print("|K|a|m|i|l| |Z|a|r|y|c|h| |2|2|4|5|4|6|");
        purpleTextColor.print("   |\n\t|                                                                  ");
        blueTextColor.print("+---------+ +---------+ +-----------+");
        purpleTextColor.println("   |\n" +
                "\t|                                                                                                          |\n" +
                "\t+----------------------------------------------------------------------------------------------------------+");
        System.out.println("\n");
        waitForEnter(true);
    }

    private void waitForEnter(boolean goToMainMenu) {
        yellowTextColor.println("\n\tPress enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            redTextColor.println("\t" + e.getMessage());
        }
        if (goToMainMenu)
            drawMainMenu();
    }

    private void drawTableCell(Object cell, int columnWidth) {
        StringBuilder result = new StringBuilder();
        String repeat = " ".repeat(Math.max(0, columnWidth - cell.toString().length()));
        result.append(repeat);
        result.append(cell.toString());

        cyanTextColor.print(result);
    }

}
