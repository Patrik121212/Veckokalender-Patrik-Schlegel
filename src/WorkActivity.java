import java.awt.Color;
import java.time.LocalDate;

/**
 Child klass till Activity, den ärver userActivity och activityDate från sin parent (Activity), samt overridar cardColor och CategoryIcon.
 */

public class WorkActivity extends Activity {

    private static final Color CARD_COLOR = new Color(168, 192, 166);

    //Konstruktor
    public WorkActivity(String userActivity, LocalDate activityDate) {
        super(userActivity, activityDate); //skickar vidare "standardvärdena" till sin förälder (Activity)
    }

    /** Returnerar kortets bakgrundsfärg för jobbaktiviteter. */
    @Override //denna skriver över färgen i sin parentklass
    public Color getCardColor() {
        return CARD_COLOR;
    }

    /** Returnerar kategorinamn och symbol för jobbaktiviteter. */
    @Override
    public String getCategoryIcon() {
        return "Jobb: ✎";
    }
}
