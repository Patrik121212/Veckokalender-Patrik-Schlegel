import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Denna klass strukturerar upp datan från Activity till en kalender-lista.
 */

public class CalendarManager {

    //ATTRIBUTES:

    private ArrayList<Activity> activityList; //skapar listan. private = endast kod inom denna klass för röra listan (inkapsling). Listan ska innehålla mina egna activityobjekt

    //KONSTRUKTOR:

    public CalendarManager() { //public så att main eller GUI kan starta den
        this.activityList = new ArrayList<>(); //skapar själva lådan
    }

    //METODER:

    /** Lägger till en aktivitet i kalenderlistan. */
    public void addActivity(Activity activity) { //skapar en add metod och anger klassen Activity som parameter
        activityList.add(activity);
    }

    /** Tar bort en specifik aktivitet från kalenderlistan. */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /** Returnerar en lista med alla aktiviteter som tillhör ett visst datum. */
    public ArrayList<Activity> getActitiviesForDate(LocalDate date) { //metoden behöver datum för att kunna göra jobbet därför skickas LocalDate med i parametern. Detta värde (datum) jämför vi med den tillfälliga variablen thisActivity för att sortera aktiviteterna utifrån datum. alla aktiviteter som har samma datum läggs i matchingActivities

        ArrayList<Activity> matchingActivities = new ArrayList<>(); //skapar tom lista med de aktiviteter som matchar

        for (int i = 0; i < activityList.size(); i++) { //loopar hela listans dynamiska storlet .size()

            Activity thisActivity = activityList.get(i); //hämtar aktivitet som ligger på plats (i) just nu, sparar i en temporär variabel.

            if (thisActivity.getActivityDate().equals(date)) { //kolla om den preliminära variabeln thisActivitys datum stämmer med datumet som jag skickat in i metoden. //.equals används istället för == eftersom date är ett objekt.
                matchingActivities.add(thisActivity);
            }
        }

        return matchingActivities;

    }

}
