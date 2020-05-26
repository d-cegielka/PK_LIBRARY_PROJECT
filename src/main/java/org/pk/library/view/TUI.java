package org.pk.library.view;

import org.pk.library.controller.Controller;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import org.pk.library.model.Book;
import org.pk.library.model.Reader;
import org.pk.library.model.Rent;
import org.pk.library.model.RentalReminder;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TUI {
    Controller libraryController;
    private final ColoredPrinter magentaTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.MAGENTA)
            .build();
    private final ColoredPrinter cyanTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.CYAN)
            .build();
    private final ColoredPrinter yellowTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.YELLOW)
            .build();
    private final ColoredPrinter whiteTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.WHITE)
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
                drawReadersMenu();
                break;
            case '3':
                drawRentsMenu();
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
        magentaTextColor.print("\n\t" + repeat);
        yellowTextColor.print(" " + title + " ");
        magentaTextColor.println(repeat);
        width = 2 * repeat.length() + len + 2;
        magentaTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        for (int i = 0; i < options.length; i++) {
            magentaTextColor.print("\t|  ");
            cyanTextColor.print((i + 1) + ") ");
            whiteTextColor.print(options[i]);
            magentaTextColor.println(" ".repeat(width - 7 - options[i].length()) + "|");
        }
        magentaTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        magentaTextColor.println("\t" + "=".repeat(width));
        yellowTextColor.print("\n\tWprowadz wybor: ");
    }

    private void drawBooksMenu(){
        clearTerminal();

        drawMenu("Ksiazki", new String[]{"Dodaj ksiazke", "Usun ksiazke","Wyswietl liste ksiazek","Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch(choice) {
            case '1':
                addBook();
                break;
            case '2':
                deleteBook();
                break;
            case '3':
                listOfBook();
                break;
            case '4':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tZly wybor!");
                waitForEnter(false);
                drawBooksMenu();
        }
    }

    private void addBook(){
        String[] bookInfo = new String[4];
        yellowTextColor.println("\n\tPodaj dane nowej ksiazki:");
        cyanTextColor.print("\t\t1) ");
        whiteTextColor.println("ISBN");
        cyanTextColor.print("\t\t2) ");
        whiteTextColor.println("Tytul");
        cyanTextColor.print("\t\t3) ");
        whiteTextColor.println("Autor");
        cyanTextColor.print("\t\t4) ");
        whiteTextColor.println("Wydawnictwo\n");
        Scanner scanner = new Scanner(System.in);
        for(int i=0;i<bookInfo.length;i++){
            cyanTextColor.print("\t\t"+ (i+1)+") ");
            bookInfo[i] = scanner.nextLine();
        }
        String info = libraryController.addBook(bookInfo[0],bookInfo[1],bookInfo[2],bookInfo[3]);
        if(!info.equals("Książka została dodana pomyślnie!")) {
            redTextColor.print("\t"+info+"\n");
            if(chooseOption())
                addBook();

            drawBooksMenu();
        }else {
            greenTextColor.print("\t"+info);
            waitForEnter(false);
            drawBooksMenu();
        }
    }

    private void deleteBook() {
        drawListOfBooks();
        yellowTextColor.print("\n\tWybierz ksiazke, ktora chcesz usunac (Lp.): ");
        Scanner scanner = new Scanner(System.in);
        short choice = scanner.nextShort();
        if(choice > libraryController.getBooks().size() || choice <= 0) {
            redTextColor.print("\tWybrales ksiazke spoza zakresu!");
            deleteBook();
        }
        String info = libraryController.deleteBook(libraryController.getBooks().get(choice-1));
        if(info.equals("Książka została usunięta pomyślnie!")) {
            greenTextColor.print("\t"+info);
            waitForEnter(false);
            drawBooksMenu();
        }else {
            redTextColor.print("\t"+info);
            if(chooseOption())
                deleteBook();

            drawBooksMenu();
        }


    }

    private void drawListOfBooks() {
        List<Book> books = libraryController.getBooks();
        magentaTextColor.print("\n\t" + "=".repeat(60));
        yellowTextColor.print(" Lista ksiazek ");
        magentaTextColor.print("=".repeat(60) + "\n\t|" + " ".repeat(133) + "|" + "\n\t|");
        cyanTextColor.print("    Lp." + " ".repeat(6) + "ISBN" + " ".repeat(26) + "Tytul" + " ".repeat(25) + "Autor" + " ".repeat(25) + "Wydawnictwo" + " ".repeat(19));
        magentaTextColor.println("|");
        for(int i=0;i<books.size();i++) {
            magentaTextColor.print("\t|    ");
            drawTableCell(i + 1, 9);
            drawTableCell(books.get(i).getIsbn(),30);
            drawTableCell(books.get(i).getTitle(),30);
            drawTableCell(books.get(i).getAuthor(),30);
            drawTableCell(books.get(i).getPublisher(),30);
            magentaTextColor.print("|\n");
        }
        magentaTextColor.println("\t|" + " ".repeat(133) + "|");
        magentaTextColor.println("\t" + "=".repeat(135));
    }

    private void listOfBook(){
        clearTerminal();
        drawListOfBooks();
        yellowTextColor.println("\n\tOpcje:");
        cyanTextColor.print("\t  1) ");
        whiteTextColor.println("Cofnij");
        yellowTextColor.print("\n\tWprowadz wybor: ");
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            choice = Short.parseShort(scanner.next());
        } catch (NumberFormatException e) {
            choice = -1;
        }
        if(choice == 1)
            drawBooksMenu();
        else {
            redTextColor.println("\n\tZly wybor!");
            waitForEnter(false);
            listOfBook();
        }

    }

    private void drawReadersMenu(){
        clearTerminal();

        drawMenu("Czytelnicy", new String[]{"Dodaj czytelnika", "Usun czytelnika","Wyswietl liste czytelnikow","Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch(choice) {
            case '1':
                addReader();
                break;
            case '2':
                deleteReader();
                break;
            case '3':
                listOfReaders();
                break;
            case '4':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tZly wybor!");
                waitForEnter(false);
                drawReadersMenu();
        }

    }

    private void addReader() {
        String[] readerInfo = new String[5];
        yellowTextColor.println("\n\tPodaj dane nowego czytelnika:");
        cyanTextColor.print("\t\t1) ");
        whiteTextColor.println("Imie");
        cyanTextColor.print("\t\t2) ");
        whiteTextColor.println("Nazwisko");
        cyanTextColor.print("\t\t3) ");
        whiteTextColor.println("Data ur.");
        cyanTextColor.print("\t\t4) ");
        whiteTextColor.println("Tel. kontaktowy");
        cyanTextColor.print("\t\t5) ");
        whiteTextColor.println("E-mail\n");
        Scanner scanner = new Scanner(System.in);
        for(int i=0;i<readerInfo.length;i++){
            cyanTextColor.print("\t\t"+ (i+1)+") ");
            readerInfo[i] = scanner.nextLine();
        }
        String info = libraryController.addReader(readerInfo[0],readerInfo[1], LocalDate.parse(readerInfo[2]),readerInfo[3],readerInfo[4]);
        if(!info.equals("Czytelnik został dodany pomyślnie!")) {
            redTextColor.print("\t"+info+"\n");
            addReader();
        }else {
            greenTextColor.print("\t"+info);
            waitForEnter(false);
            drawBooksMenu();
        }
    }

    private void deleteReader() {
        drawListOfReaders();
        yellowTextColor.print("\n\tWybierz czytelnika, ktorego chcesz usunac (Lp.): ");
        Scanner scanner = new Scanner(System.in);
        short choice = scanner.nextShort();
        if(choice > libraryController.getReaders().size() || choice <= 0) {
            redTextColor.print("\tWybrales czytelnika spoza zakresu!");
            deleteReader();
        } else {
            libraryController.deleteReader(libraryController.getReaders().get(choice-1));
            greenTextColor.print("\t"+libraryController.deleteReader(libraryController.getReaders().get(choice-1)));
            waitForEnter(false);
            drawBooksMenu();
        }

    }

    private void drawListOfReaders() {
        List<Reader> readers = libraryController.getReaders();
        magentaTextColor.print("\n\t" + "=".repeat(46));
        yellowTextColor.print(" Lista czytelnikow ");
        magentaTextColor.print("=".repeat(45) + "\n\t|" + " ".repeat(108) + "|" + "\n\t|");
        cyanTextColor.print("    Lp." + " ".repeat(6) + "Imie" + " ".repeat(11) + "Nazwisko" + " ".repeat(7) + "Data ur." + " ".repeat(7) + "Tel. kontaktowy" + " ".repeat(5) + "E-mail" + " ".repeat(24));
        magentaTextColor.println("|");
        for(int i=0;i<readers.size();i++) {
            magentaTextColor.print("\t|    ");
            drawTableCell(i + 1, 9);
            drawTableCell(readers.get(i).getFirstName(),15);
            drawTableCell(readers.get(i).getLastName(),15);
            drawTableCell(readers.get(i).getDateOfBirth(),15);
            drawTableCell(readers.get(i).getPhoneNumber(),20);
            drawTableCell(readers.get(i).getEmailAddress(),30);
            magentaTextColor.print("|\n");
        }
        magentaTextColor.println("\t|" + " ".repeat(108) + "|");
        magentaTextColor.println("\t" + "=".repeat(110));
    }

    private void listOfReaders(){
        clearTerminal();
        drawListOfReaders();
        yellowTextColor.println("\n\tOpcje:");
        cyanTextColor.print("\t  1) ");
        whiteTextColor.println("Cofnij \n");
        yellowTextColor.print("\n\tWprowadz wybor: ");
        Scanner scanner = new Scanner(System.in);
        short choice = scanner.nextShort();
        if(choice == 1)
            drawReadersMenu();
        else {
            redTextColor.println("\n\tZly wybor!");
            waitForEnter(false);
            listOfReaders();
        }

    }

    private void drawRentsMenu(){
        clearTerminal();

        drawMenu("Wypozyczenia", new String[]{"Dodaj wypozyczenie","Wyswietl liste wypozyczen","Wyswietl liste przypomnien","Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch(choice) {
            case '1':
                break;
            case '2':
                listOfRents();
                break;
            case '3':
                listOfRentalReminders();
                break;
            case '4':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tZly wybor!");
                waitForEnter(false);
                drawRentsMenu();
        }

    }

    private void listOfRents(){
        clearTerminal();
        List<Rent> rents = libraryController.getRents();
        magentaTextColor.print("\n\t" + "=".repeat(56));
        yellowTextColor.print(" Lista wypozyczen ");
        magentaTextColor.print("=".repeat(56) + "\n\t|" + " ".repeat(128) + "|" + "\n\t|");
        //cyanTextColor.print("   ID               Imie os. wyp.               Nazwisko os. wyp.               Tytul ksiazki               Data wypozyczenia               Data zwrotu                Czy zwrocono");
        cyanTextColor.print("    Lp." + " ".repeat(6) + "Imie os. wyp." + " ".repeat(7) + "Nazwisko os. wyp." + " ".repeat(3) + "Tytul ksiazki" + " ".repeat(7) + "Data wypozyczenia" + " ".repeat(3) + "Data zwrotu" + " ".repeat(9) + "Czy zwrocono" + " ".repeat(3));
        magentaTextColor.println("|");
        for(int i=0;i<rents.size();i++) {
            magentaTextColor.print("\t|    ");
            drawTableCell(i + 1, 9);
            drawTableCell(rents.get(i).getREADER().getFirstName(),20);
            drawTableCell(rents.get(i).getREADER().getLastName(),20);
            drawTableCell(rents.get(i).getBOOK().getTitle(),20);
            drawTableCell(rents.get(i).getDateOfRent(),20);
            drawTableCell(rents.get(i).getDateOfReturn(),20);
            drawTableCell(rents.get(i).isReturned(),15);
            magentaTextColor.print("|\n");
        }
        magentaTextColor.println("\t|" + " ".repeat(128) + "|");
        magentaTextColor.println("\t" + "=".repeat(130));
        yellowTextColor.println("\n\tOpcje:");
        cyanTextColor.print("\t  1) ");
        whiteTextColor.println("Cofnij \n");
        yellowTextColor.print("\n\tWprowadz wybor: ");
        Scanner scanner = new Scanner(System.in);
        short choice = scanner.nextShort();
        if(choice == 1)
            drawRentsMenu();
        else {
            redTextColor.println("\n\tZly wybor!");
            waitForEnter(false);
            listOfRents();
        }

    }

    private void drawRentalRemindersMenu(){
        clearTerminal();

        drawMenu("Przypomnienia", new String[]{"Dodaj przypomnienie","Wyswietl liste przypomnien","Cofnij"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch(choice) {
            case '1':
                break;
            case '2':
                listOfRents();
                break;
            case '3':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tZly wybor!");
                waitForEnter(false);
                drawRentsMenu();
        }

    }

    private void listOfRentalReminders(){
        clearTerminal();
        List<RentalReminder> rentalReminders = libraryController.getRentalReminders();
        magentaTextColor.print("\n\t" + "=".repeat(50));
        yellowTextColor.print(" Lista przypomnien ");
        magentaTextColor.print("=".repeat(50) + "\n\t|" + " ".repeat(142) + "|" + "\n\t|");
        //cyanTextColor.print("   ID               Imie os. wyp.               Nazwisko os. wyp.               Tytul ksiazki               Data przypomnienia              ");
        cyanTextColor.print("    Lp." + " ".repeat(6) + "Imie os. wyp." + " ".repeat(7) + "Nazwisko os. wyp." + " ".repeat(3) + "Tytul ksiazki" + " ".repeat(7) + "Data wypozyczenia" + " ".repeat(3) + "Data zwrotu" + " ".repeat(9) + "Czy zwrocono" + " ".repeat(3));
        magentaTextColor.println("|");
        for(int i=0;i<rentalReminders.size();i++) {
            magentaTextColor.print("\t| ");
            drawTableCell(i + 1, 4);
            drawTableCell(rentalReminders.get(i).getRent().getREADER().getFirstName(),15);
            drawTableCell(rentalReminders.get(i).getRent().getREADER().getLastName(),15);
            drawTableCell(rentalReminders.get(i).getRent().getBOOK().getTitle(),15);
            drawTableCell(rentalReminders.get(i).getDateOfReminder(),15);
            magentaTextColor.print("   |\n");
        }
        magentaTextColor.println("\t|" + " ".repeat(142) + "|");
        magentaTextColor.println("\t" + "=".repeat(100));
        yellowTextColor.println("\n\tOpcje:");
        cyanTextColor.print("\t  1) ");
        whiteTextColor.println("Cofnij \n");
        yellowTextColor.print("\n\tWprowadz wybor: ");
        Scanner scanner = new Scanner(System.in);
        short choice = scanner.nextShort();
        if(choice == 1)
            drawRentalRemindersMenu();
        else {
            redTextColor.println("\n\tZly wybor!");
            waitForEnter(false);
            listOfRentalReminders();
        }

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
        magentaTextColor.print("\n\n\t+----------------------------------------------------------------------------------------------------------+\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|         ");
        yellowTextColor.print("/$$$$$$$                      /$$       /$$                                           /$$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$__  $$                    | $$      | $$                                          | $$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  \\ $$  /$$$$$$   /$$$$$$$| $$$$$$$ | $$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ |____  $$ /$$_____/| $$__  $$| $$__  $$ /$$__  $$ |____  $$ /$$__  $$ /$$__  $$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$  /$$$$$$$|  $$$$$$ | $$  \\ $$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\__/| $$  | $$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ /$$__  $$ \\____  $$| $$  | $$| $$  | $$| $$  | $$ /$$__  $$| $$      | $$  | $$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$$$$$$/|  $$$$$$$ /$$$$$$$/| $$  | $$| $$$$$$$/|  $$$$$$/|  $$$$$$$| $$      |  $$$$$$$");
        magentaTextColor.print("        |\n\t|        ");
        yellowTextColor.print("|_______/  \\_______/|_______/ |__/  |__/|_______/  \\______/  \\_______/|__/       \\_______/");
        magentaTextColor.print("        |\n\t|                                                                                                          |\n" +
                "\t|                                                                                     ");
        redTextColor.print("Version 1.0.0");
        magentaTextColor.print("        |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                      ");
        cyanTextColor.print("*---------------*");
        magentaTextColor.print("   |\n\t|                                                                                      ");
        whiteTextColor.print("|A|u|t|o|r|z|y|:|");
        magentaTextColor.print("   |\n\t|                                                      ");
        cyanTextColor.print("*-------------------------------* *-----------*");
        magentaTextColor.print("   |\n\t|                                                        ");
        whiteTextColor.print("|D|o|m|i|n|i|k| |C|e|g|i|e|l|k|a| |2|2|4|4|7|8|");
        magentaTextColor.print("     |\n\t|                                                      ");
        cyanTextColor.print("+-----------------------------------------------+");
        magentaTextColor.print("   |\n\t|                                                                ");
        whiteTextColor.print("|K|a|m|i|l| |Z|a|r|y|c|h| |2|2|4|5|4|6|");
        magentaTextColor.print(" |\n\t|                                                                  ");
        cyanTextColor.print("+---------+ +---------+ +-----------+");
        magentaTextColor.println("   |\n" +
                "\t|                                                                                                          |\n" +
                "\t+----------------------------------------------------------------------------------------------------------+");
        System.out.println("\n");
        waitForEnter(true);
    }

    private void waitForEnter(boolean goToMainMenu) {
        yellowTextColor.println("\n\tNacisnij enter aby kontynuowac...");
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
        result.append(cell.toString());
        result.append(repeat);

        whiteTextColor.print(result);
    }

    private boolean chooseOption() {
        yellowTextColor.print("\n\tCzy chcesz sprobowac ponownie?\n");
        cyanTextColor.print("\t\t0) ");
        whiteTextColor.println("Nie");
        cyanTextColor.print("\t\t1) ");
        whiteTextColor.println("Tak");

        Scanner scanner = new Scanner(System.in);
        short choice;
        cyanTextColor.print("\n\tWybierz opcje: ");
        choice = scanner.nextShort();
        if (choice < 0 || choice > 1) {
            redTextColor.print("\tZly wybor");
            chooseOption();
        }

        //return Boolean.parseBoolean(String.valueOf(choice));
        return choice == 1;
    }

}
