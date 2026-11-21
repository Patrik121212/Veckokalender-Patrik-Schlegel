import java.time.LocalDate;
import java.awt.Color;

/**
 * Klassen för de enskilda aktiviteterna
 */

public class Activity {

    //ATTRIBUTES:

    private String userActivity;
    private LocalDate activityDate; //använder java.time.LocalDate och döper variablen till activityDate

    private static final Color CARD_COLOR = new Color(232, 206, 134);

    //KONSTRUKTOR:

    public Activity(String activityName, LocalDate activityDate) {  //tar emot användarens aktivitet och datum. public för att andra klasser (ex. GUI där användare skriver in sin aktivitet) ska kunna skapa aktivit
        userActivity = activityName;  //sparar i variablen userActivity. (this behövs inte för att namnet från konstruktorn skiljer sig från instansvariabeln)
        this.activityDate = activityDate;
    }

    //GETTER: //för att andra klasse ska kunna nå instansvariablerna

    /** Returnerar aktivitetens beskrivning. */
    public String getUserActivity() { //matchar namnet geUserActivity med klassens variabel userActivity
        return userActivity;
    }

    /** Returnerar aktivitetens datum. */
    public LocalDate getActivityDate() {
        return activityDate;
    }

    /** Returnerar kortets bakgrundsfärg. */
    public Color getCardColor() {
        return CARD_COLOR;
    }

    /** Returnerar aktivitetens kategorinamn och symbol. */
    public String getCategoryIcon() {
        return "Annat: ✦";
    }

}
