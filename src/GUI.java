import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Denna klass ritar upp det grafiska gränssnittet
 */

public class GUI extends JFrame {

    //ATRRIBUTES

    //instansvariabler, går att använda i alla metoder i hela klassen, variabler som skapas i en metod (lokala variabler) behövs skickas vidare genom parametrar

    private final CalendarManager manager;
    private LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); //för att få ut måndagen i veckan vi är i nu

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private static final int DAYS_IN_WEEK = 7;

    //färgpalett, font och border (stora bokstäver eftersom de är konstanter)
    private static final Color BG_COLOR_1 = new Color(245, 238, 222);
    private static final Color BG_COLOR_2A = new Color(116, 178, 188);
    private static final Color BG_COLOR_3A = new Color(235, 162, 92);
    private static final Color BG_COLOR_3B = new Color(208, 114, 52);
    private static final Color TEXT_BORDER_COLOR = new Color(18, 28, 42);
    private static final Color BUTTON_COLOR = new Color(230, 222, 200);

    private static final Font HEADER_FONT = new Font("Century Gothic", Font.BOLD, 24);
    private static final Font DATE_FONT_BIG = new Font("Century Gothic", Font.BOLD, 20);
    private static final Font DATE_FONT_SMALL = new Font("Century Gothic", Font.BOLD, 10);
    private static final Font TEXT_FONT = new Font("Century Gothic", Font.PLAIN, 12);
    private static final Font ICON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    private static final Border HEADER_EMPTY_BORDER = new EmptyBorder(15, 15, 15, 15);
    private static final Border WEEKPANEL_EMPTY_BORDER = new EmptyBorder(0, 15, 15, 15);

    private static final Border COLUMN_EMPTY_BORDER = new EmptyBorder(5, 5, 5, 5);
    private static final Border COLUMN_LINE_BORDER = new LineBorder(TEXT_BORDER_COLOR, 1, true);
    private static final Border COLUMN_BORDER = BorderFactory.createCompoundBorder(COLUMN_LINE_BORDER, COLUMN_EMPTY_BORDER);
    private static final Border DATE_EMPTY_BORDER = new EmptyBorder(6, 0, 6, 0); //6 för att de ska hamna i linje med dagens datum (eftersom den använder annan border) så det ser rakt ut.


    //KONSTRUKTORN:

    public GUI(CalendarManager manager) { //Så att GUI kan använda CalendarManagers metoder ex. getActivitiesForDate
        this.manager = manager;

        calendarSetup();

    }

    //METODER:

    /** Bygger upp huvudfönstret, rubriken och veckans sju dagkolumner. */
    private void calendarSetup() {
        ImageIcon icon = new ImageIcon("icon.jpg"); //iconen uppe till vänster
        this.setIconImage(icon.getImage());
        this.setTitle("Veckokalender");

        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(BG_COLOR_1);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Skapar kalenderdagarna med hjälp av currentMonday kombinerat med loopar längre fram

        int weekNumber = currentMonday.get(WeekFields.ISO.weekOfWeekBasedYear());//denna hittar vilken vecka på året det är utifrån currentMonday. Jag använder den i rubriken
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("sv-SE"));
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("sv-SE")); //ändrar format på dagarna till ex. MÅN på svenska

        //HJÄLPMETOD createHeader
        JPanel header = createHeader(currentMonday, weekNumber, monthFormatter); //skapar header med metoden createHeader, skickar in det den behöver för att göra jobbet
        this.add(header, BorderLayout.NORTH); //lägger in header i fönstret och placerar den högst upp


        //GRID-MALLEN FÖR HELA VECKAN
        JPanel weekPanel = new JPanel(new GridLayout(1, DAYS_IN_WEEK, 5, 0)); //horisontal och vertical gap mellan kolumnerna
        weekPanel.setBorder(WEEKPANEL_EMPTY_BORDER);
        weekPanel.setOpaque(false);
        this.add(weekPanel, BorderLayout.CENTER);

        for (int i = 0; i < DAYS_IN_WEEK; i++) { //datumloop
            LocalDate thisDay = currentMonday.plusDays(i); //tillfällig variabel, loopen börjar på currentMonday och sen plussas på för hela veckan.

            //HJÄLPMETOD createDayColumn
            JPanel dayColumn = createDayColumn(thisDay, dayFormatter); //I denna finns ännu en loop som kikar på dagens aktiviteter som skapas utifrån arrayListan thisDayActivities (som hämtar data ifrån CalendarManager metoden getActivitiesForDate
            weekPanel.add(dayColumn); //lägger in dagskolumn i weekPanel
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /** Skapar rubrikpanelen med veckonummer, månad och knappar för att byta vecka. */
    private JPanel createHeader(LocalDate currentMonday, int weekNumber, DateTimeFormatter monthFormatter) { //dessa parametrar behöver metoden för att göra jobbet

        JPanel headerPanel = new JPanel(new BorderLayout()); //borderLayout för att få knapparna längst ut
        headerPanel.setOpaque(false); //gör panelen genomskinlig så bakgrundsfärgen kommer fram

        //SKAPAR VECKOBYTEN MED KNAPPAR
        JButton prevWeekButton = new JButton("❰");
        prevWeekButton.setBackground(BUTTON_COLOR);
        prevWeekButton.setForeground(TEXT_BORDER_COLOR);
        prevWeekButton.setFocusPainted(false); //ta bort standadfokus

        prevWeekButton.addActionListener(e -> {
            this.currentMonday = this.currentMonday.minusWeeks(1); //this för att ändra instansvariablen och inte parametervariabeln eftersom de heter samma
            refreshCalendar();
        });

        JLabel weekAndMonth = new JLabel("~~~~~~  v. " + String.valueOf(weekNumber) + " " + currentMonday.format(monthFormatter).toUpperCase() + "  ~~~~~~");
        weekAndMonth.setHorizontalAlignment(SwingConstants.CENTER);
        weekAndMonth.setFont(HEADER_FONT);
        weekAndMonth.setForeground(TEXT_BORDER_COLOR);

        JButton nextWeekButton = new JButton("❱");
        nextWeekButton.setBackground(BUTTON_COLOR);
        nextWeekButton.setForeground(TEXT_BORDER_COLOR);

        nextWeekButton.addActionListener(e -> {
            this.currentMonday = this.currentMonday.plusWeeks(1);
            refreshCalendar();
        });

        headerPanel.add(prevWeekButton, BorderLayout.WEST);
        headerPanel.add(weekAndMonth, BorderLayout.CENTER);
        headerPanel.add(nextWeekButton, BorderLayout.EAST);

        headerPanel.setBorder(HEADER_EMPTY_BORDER); //få lite luft uppe och nere

        return headerPanel;
    }

    /** Skapar en lodrät panel för en specifik dag med datum, aktiviteter och lägg till-knapp. */
    private JPanel createDayColumn(LocalDate thisDay, DateTimeFormatter dayFormatter) {
        //SKAPAR NY LISTA SOM FYLLS MED AKTIVITETER KOPPLADE TILL SAMMA DATUM
        ArrayList<Activity> thisDayActivities = manager.getActitiviesForDate(thisDay); //hämtar aktiviteterna utifrån loopens tillfälliga datum och sparar i en ny lista. Här skickar jag in thisDay (rörlig variabl) i calendar(manager) metoden getActivitiesForDate, alltså alla aktiviteter som kommer ut från metoden är aktiviteter på samma dag.

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //så att allt innehåll staplas lodrätt

        //RAM RUNT VARJE DAGSKOLUMN OC FÄRG
        panel.setBorder(COLUMN_BORDER);
        panel.setBackground(BG_COLOR_2A);

        //DATUMUM I DAG OCH SIFFRA
        String dayText = thisDay.format(dayFormatter).toUpperCase(); //skapar Stringvariabel i formatet monday MÅN 16, använder min formatter som jag skapade tidigare och ändrar till stora bokstäver
        JLabel labelDayText = new JLabel(dayText);
        labelDayText.setFont(DATE_FONT_SMALL);
        labelDayText.setForeground(TEXT_BORDER_COLOR);
        labelDayText.setAlignmentX(Component.CENTER_ALIGNMENT); //centrerar texten

        String dayNumber = String.valueOf(thisDay.getDayOfMonth()); //måste vara string för att label ska ta emot den.
        JLabel labelDayNumber = new JLabel(dayNumber);
        labelDayNumber.setFont(DATE_FONT_BIG);
        labelDayNumber.setForeground(TEXT_BORDER_COLOR);
        labelDayNumber.setAlignmentX(CENTER_ALIGNMENT);

        JPanel panelDay = new JPanel();
        panelDay.setLayout(new BoxLayout(panelDay, BoxLayout.Y_AXIS));
        panelDay.setBorder(DATE_EMPTY_BORDER);

        panelDay.add(labelDayText);
        panelDay.add(labelDayNumber);

        panelDay.setBackground(BG_COLOR_3A);
        panelDay.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelDay.getPreferredSize().height)); //för att boxen ska sträcka ut sig hela vägen höger/vänster (gör att det ser snyggare ut när dagen är markerad)

        panel.add(panelDay);
        panel.add(Box.createVerticalStrut(15)); //mellanrum till första aktiviteten

        //HIGHLIGHTA DAGEN DET ÄR IDAG

        if (thisDay.equals(LocalDate.now())) { //markera dagens datum med mörkare färg
            panelDay.setBackground(BG_COLOR_3B);
            panelDay.setBorder(COLUMN_BORDER); //återanvänder samma border som panel
        }

        //SKRIVER UT AKTIVITETERNA FÖR DEN SPECIFIKA DAGEN

        for (int j = 0; j < thisDayActivities.size(); j++) { //aktivitetsloopen. loop i loop, denna skriver ut alla aktiviteter för just den specifika dagen.

            Activity currentActivity = thisDayActivities.get(j); //sparar hela objektet (inte bara namnet) för att kunna använda remove metoden. Den funkar inte endast med text.

            //HJÄLPMETOD createActivityCard
            JPanel activityCard = createActivityCard(currentActivity); //skapar aktivitet med hjälp av hjälpmetoden createActivityCard

            panel.add(activityCard); //skriver ut activityCard i panelen
            panel.add(Box.createVerticalStrut(15)); //lägger till mellanrum mellan aktiviteterna
        }

        //SKAPAR LÄGG TILL KNAPP

        JButton activityButtonAdd = new JButton("✚");
        panel.add(Box.createVerticalGlue()); //för att få trycka ner knapparna längst ner.
        panel.add(activityButtonAdd);
        activityButtonAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityButtonAdd.setBackground(BUTTON_COLOR);
        activityButtonAdd.setForeground(TEXT_BORDER_COLOR);
        activityButtonAdd.setFocusPainted(false); //tar bort fokusring

        //KNAPPLYSSNARE

        activityButtonAdd.addActionListener(e -> { //lambda uttryck,
           //Hjälpmetod
            handleAddActivity(thisDay);
        });

        return panel;

    } //i denna anropas även metoden (metod i metod) createActivityCard som skriver ut korten.

    /** Skapar ett enskilt aktivitetskort med ikon, text, färg och raderingsknapp. */
    private JPanel createActivityCard(Activity currentActivity) {
        //kategori och icon label (samma)
        JLabel iconLabel = new JLabel(currentActivity.getCategoryIcon()); //Java anropar automatiskt rätt subklass (WorkActivity, FreeTimeActivity eller Activity) beroende på vilket objekt som skapades. Polymorfism.
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setForeground(TEXT_BORDER_COLOR);
        iconLabel.setFont(ICON_FONT);

        String currentActivityText = currentActivity.getUserActivity(); //hämtar texten från currentActivity och skriver ut aktiviteterna den specifika dagen

        JPanel thisActivityPanel = new JPanel();

        JTextPane thisActivityText = new JTextPane(); //pane för att textArea inte hade en centrera text funktion, samt label kunde man inte radbryta snyggt.
        thisActivityText.setText(currentActivityText);

        thisActivityText.setEditable(false);
        thisActivityText.setOpaque(false);
        thisActivityText.setAlignmentX(Component.CENTER_ALIGNMENT);

        //detta stycke är för att centrera texten i aktivitetsfönstrena, fick importera styleconstants bla.
        StyledDocument doc = thisActivityText.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        thisActivityText.setFont(TEXT_FONT);
        thisActivityText.setForeground(TEXT_BORDER_COLOR);

        JButton activityButtonRemove = new JButton("✖");
        activityButtonRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
        activityButtonRemove.setBackground(BUTTON_COLOR);
        activityButtonRemove.setForeground(TEXT_BORDER_COLOR);

        thisActivityPanel.add(iconLabel);
        thisActivityPanel.add(Box.createVerticalStrut(4));
        thisActivityPanel.add(thisActivityText);
        thisActivityPanel.add(Box.createVerticalStrut(8)); //lite avstånd mellan text och radera-knappen.
        thisActivityPanel.add(activityButtonRemove);

        thisActivityPanel.setBorder(COLUMN_BORDER); //återanvänder border från panel

        thisActivityPanel.setLayout(new BoxLayout(thisActivityPanel, BoxLayout.Y_AXIS));
        thisActivityPanel.setBackground(currentActivity.getCardColor()); //här hämtas färgen utfrån om det är WorkActivity eller FreeTimeActivity (getCardColor)
        thisActivityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, thisActivityPanel.getPreferredSize().height)); //höjden anpassas efter innehållet

        //TA BORT AKTIVITET KNAPP LYSSNARE

        activityButtonRemove.addActionListener(e -> {
            manager.removeActivity(currentActivity);
            refreshCalendar();
        });

        thisActivityPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        return thisActivityPanel;
    }

    /** Hanterar dialogflödet och felkontrollen när användaren skapar en ny aktivitet. */
    private void handleAddActivity(LocalDate thisDay) {

        String[] options = {"Jobb", "Fritid", "Annat"}; //valen som ska komma upp i showOptionDialog (funkar lite som en switch)

        int choice = JOptionPane.showOptionDialog(this, "Välj kategori:", "Kategori", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options); //option är min array med text (valen) till knapparna.
        if (choice == -1) { //förhindrar att den går vidare till skriv in aktivitet om användaren kryssar i krysset. -1 pga en array alltid är 0 eller högre.
            return;
        }

        String userActivity = JOptionPane.showInputDialog(this, "Skriv in din aktivitet:");//ny ruta, this betyder att den ska poppa upp mitt över kalenderfönstret.
        if (userActivity != null && !userActivity.trim().isEmpty()) { //så att användaren skriver in riktig text.

            //klockslag
            String timeInput = JOptionPane.showInputDialog(this, "Ange starttid för aktiviteten (HH:MM):");
            if (timeInput == null) { //för att stänga ner om använderan trycker kryss
                return;
            }

            try {
                LocalTime time = LocalTime.parse(timeInput.trim()); //gör om texten till ett riktigt klockslag (krävs att användaren skrivit in rätt), vid fel hoppas detta blcok över och catch kör. om det lyckas körs try och aktivitet skapass utifrån vilket val användaren gjort
                String activityWithTime = time  + "\n" + userActivity;

                Activity newUserActivity;
                //beroende på vilket val användaren gör så skapas det nya objektet från child eller parent klassen. dvs. olika färger. Texten och klockslaget som användaren skrivit finns i activityWithTime, samt datumet i thisDay (för att kunna sortera aktiviteterna utifrån datum)
                //obs Detta är knappar så användaren fyller inte i siffror.
                if (choice == 0) { //choice är kopplad till användarens svar i första rutan om kategori.
                    newUserActivity = new WorkActivity(activityWithTime, thisDay);
                } else if (choice == 1) {
                    newUserActivity = new FreeTimeActivity(activityWithTime, thisDay);
                } else {
                    newUserActivity = new Activity(activityWithTime, thisDay);
                }

                this.manager.addActivity(newUserActivity); //lägger in aktiviteten i kalenderlistan, subklasserna går under samma lista.

                //HJÄLPMETOD för att rensa och skapa ny
                refreshCalendar();

            } catch (DateTimeParseException ex) { //om användaren skriver in fel format (ex hej, 14.30 eller tom rutan) så sker felet i parantesen vilket gör att catchblocket körs

                JOptionPane.showMessageDialog(this, "Ogiltigt klockslag. Starttiden måste vara en giltig tid med formatet HH:MM (ex. 12:30).");

            }

        }

    }

    /** Tömmer och ritar om hela kalendervyn vid ändringar eller veckobyte. */
    private void refreshCalendar() { //skapar metod för att undvika upprepning med rensa kalender vid lägga till /ta bort aktivitet.

        this.getContentPane().removeAll(); //måste tömma fönstret först för att kunna lägga til nytt.
        calendarSetup(); //skapar calendern igen med min nya aktivitet
        this.revalidate(); //räknar om storlek och postitionering med den uppdaterade kalendern
        this.repaint(); //ritar ut allt igen.

    }

}
