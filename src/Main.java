/**
 Startklass som startar upp kalenderlogik och GUI
 */

public class Main {
    public static void main() {
        CalendarManager calendar = new CalendarManager();
        GUI gui = new GUI(calendar); //skickar in calendar för att gui behöver den för att köra
    }
}