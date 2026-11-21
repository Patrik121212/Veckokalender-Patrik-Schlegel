import java.awt.*;
import java.time.LocalDate;

/**
 Child klass till Activity, den ärver userActivity och activityDate från sin parent (Activity), samt overridar cardColor och CategoryIcon.
 */

public class FreeTimeActivity extends Activity {

    private static final Color CARD_COLOR = new Color(232, 184, 186);

    //Konstruktor
    public FreeTimeActivity(String userActivity, LocalDate activityDate) {
        super(userActivity, activityDate); //skickar vidare "standardvärdena" till sin förälder (Activity)
    }

    /** Returnerar kortets bakgrundsfärg för fritidsaktiviteter. */
    @Override //denna skriver över färgen i sin parentklass
    public Color getCardColor() {
        return CARD_COLOR;
    }

    /** Returnerar kategorinamn och symbol för fritidsaktiviteter. */
    @Override
    public String getCategoryIcon() {
        return "Fritid: ❖";
    }
}
