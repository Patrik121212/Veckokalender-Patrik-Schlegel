# Veckokalender - Inlämningsuppgift

Här har jag byggt en veckokalender i Java med ett grafiskt gränssnitt i Swing. Programmet utgår från dagens datum och visar innevarande veckas dagar med möjlighet att bläddra mellan veckor samt lägga till och ta bort kategoriserade aktiviteter med klockslag.

## Hur man startar programmet
* **Instruktioner:** Öppna projektet i IntelliJ IDEA och kör `main`-metoden i klassen `Main`. (Viktigt att bildfilen `icon.jpg` ligger i projektets rotmapp för att fönsterikonen ska visas.)
* **JDK-version:** Oracle OpenJDK 27

## Klassbeskrivning och Ansvarsområden
* **Main:** Startar programmet och sätter igång logiken (`CalendarManager`) samt gränssnittet (`GUI`).
* **Activity:** Basklass som representerar en enskild aktivitet med text/klockslag och datum, samt standardfärg och kategoriikon.
* **WorkActivity & FreeTimeActivity:** Subklasser till `Activity` (arv) som överskuggar kortfärg och ikon för jobb- respektive fritidsaktiviteter.
* **CalendarManager:** Hanterar lagring och logik för alla aktiviteter i en `ArrayList<Activity>`, separerad från Swing-koden.
* **GUI:** Ritar upp det grafiska gränssnittet i Swing med layout managers, hanterar veckobläddring och tar emot användarens inmatning.

## Kravtabell (Godkänt - G)

| Krav-ID | Beskrivning | Status | Var i koden är kravet uppfyllt? (Klass & Metod) |
| :--- | :--- | :--- | :--- |
| **G-F1** | Öppnar ett Swing-fönster med innevarande vecka | ✅ Klar | `GUI`, i metoden `calendarSetup` |
| **G-F2** | Visar veckodag, datum, månad via `java.time` | ✅ Klar | `GUI`, i `calendarSetup` och `createDayColumn` med `LocalDate` och `TemporalAdjusters` |
| **G-F3** | Dagens datum är tydligt markerat | ✅ Klar | `GUI`, i metoden `createDayColumn` med en `if`-sats som ändrar bakgrundsfärg och ram |
| **G-F4** | Lägga till aktivitet på valfri dag (syns direkt) | ✅ Klar | `GUI` (`handleAddActivity`) och `CalendarManager` (`addActivity`) |
| **G-F5** | En dag kan ha flera aktiviteter | ✅ Klar | `CalendarManager` (`ArrayList`) och `GUI` (`for`-loop i `createDayColumn`) |
| **G-F6** | Ta bort en aktivitet | ✅ Klar | `GUI` (`activityButtonRemove` i `createActivityCard`) och `CalendarManager` (`removeActivity`) |
| **G-F7** | Programmet kraschar inte vid tomma/oväntade fält | ✅ Klar | `GUI`, kontroll av `null` och `.trim().isEmpty()` samt `choice == -1` i `handleAddActivity` |
| **G-K1** | Klassen `Main` har en `main`-metod (max ~10 rader) | ✅ Klar | `Main`, metoden `main` |
| **G-K2** | Minst 3 klasser, separerad logik och Swing | ✅ Klar | Uppdelat i 6 klasser där `CalendarManager` och `Activity`-klasserna är fria från Swing |
| **G-K3** | Alla instansvariabler är `private` | ✅ Klar | `Activity` (`userActivity`, `activityDate`), `CalendarManager` (`activityList`) och `GUI` (`manager`, `currentMonday`) |
| **G-K4** | Aktiviteterna lagras i en samlingstyp | ✅ Klar | `ArrayList<Activity>` i klassen `CalendarManager` |
| **G-K5** | Minst en egen metod tar parametrar + returnerar | ✅ Klar | T.ex. `CalendarManager`: `getActitiviesForDate(LocalDate date)` och `GUI`: `createDayColumn(...)` |
| **G-K6** | Händelser hanteras med `ActionListener` | ✅ Klar | `GUI`, lambda-lyssnare i `createHeader`, `createDayColumn` och `createActivityCard` |
| **G-K7** | Följer Javas namnkonventioner & indrag | ✅ Klar | Gäller genomgående i samtliga klasser |
| **G-K8** | Javadoc på varje klass och `public` metod | ✅ Klar | Gäller samtliga klasser och `public`-metoder |
| **G-K9** | Projektet går att kompilera och köra utan fel | ✅ Klar | Testat och körbart från `Main` |

## Kravtabell (Väl Godkänt - VG)

| Krav-ID | Beskrivning | Status | Var i koden är kravet uppfyllt? (Klass & Metod) |
| :--- | :--- | :--- | :--- |
| **VG-F1** | Minst två extrafunktioner (veckobläddring, veckonummer, kategorier med olika färger) | ✅ Klar | `GUI` (`createHeader` för veckonummer och `<` / `>`-knappar) samt `WorkActivity` & `FreeTimeActivity` för färgkodade kategorier |
| **VG-K1** | Arv eller interface som används polymorft | ✅ Klar | `WorkActivity` och `FreeTimeActivity` ärver från `Activity` (`extends`). I `createActivityCard` anropas `getCardColor()` och `getCategoryIcon()` polymorft|
| **VG-K2** | Felhantering med `try / catch` och begripligt felmeddelande | ✅ Klar | `GUI.handleAddActivity`: tolkar klockslag med `LocalTime.parse` och fångar `DateTimeParseException` med `JOptionPane.showMessageDialog` |
| **VG-K3** | Ingen onödig upprepning; egna metoder och konstanter i stället för magiska tal | ✅ Klar | Konstanter (`private static final`) för färger, typsnitt, ramar och storlekar i `GUI` och `Activity`-klasserna, samt uppdelning i hjälpmetoder (`createHeader`, `createDayColumn`, `createActivityCard`, `handleAddActivity`, `refreshCalendar`) |
| **VG-K4** | Gränssnittet byggs med layout managers (utan `setBounds`/`null`) | ✅ Klar | Använder `BorderLayout`, `GridLayout` och `BoxLayout` genomgående i `GUI` |
| **VG-R1** | Reflektion i README (200–400 ord) | ✅ Klar | Se avsnittet *Reflektioner och vidareutveckling* nedan |
| **VG-M1** | Muntligt resonemang om alternativa lösningar och vidareutveckling | ✅ Klar | Förberett inför redovisningen |

## Reflektioner och vidareutveckling (VG-R1)

**Designval och alternativ:**
Ett viktigt designval var hur användaren skulle mata in nya aktiviteter. Alternativet till att använda `JOptionPane` var att bygga ett helt eget popup-fönster med `JFrame` eller `JDialog`, kompletterat med egna textfält och knappar. Eftersom inmatningen bara krävde tre enkla steg (kategori, beskrivning och klockslag) valde jag `JOptionPane`. Det gav ett mycket renare flöde med betydligt mindre kod att underhålla, samtidigt som felhantering med `try/catch` blev lättare att integrera direkt i dialogflödet.

**Förbättringar och optimering i koden:**
Under arbetets gång växte klassen `GUI` snabbt och metoden `calendarSetup()` blev lång och svårläst med nästlade loopar. För att förbättra läsbarheten och undvika upprepning flyttade jag först upp alla hårdkodade färger, typsnitt, ramar och mått till `private static final`-konstanter. Därefter bröt jag ut koden i mindre hjälpmetoder (`createHeader`, `createDayColumn`, `createActivityCard`, `handleAddActivity` och `refreshCalendar`). Det gjorde koden renare och mer lättläst. 

**Anpassning till månadsvy och framtida utveckling:**
För att bygga om kalendern till en månadsvy skulle jag först byta ut instansvariabeln `currentMonday` mot en `YearMonth` (eller första dagen i vald månad). I `calendarSetup()` skulle jag ändra `GridLayout` från en rad (`1, 7`) till flera rader med sju kolumner (`0, 7`) och låta loopen gå lika många varv som månadens antal dagar (`month.lengthOfMonth()`). Vidare skulle jag vilja lägga till en lodrät `JScrollPane` i dagkolumnerna för dagar med många aktiviteter, samt en sökfunktion på datum – vilket skulle bli ännu effektivare om `ArrayList` i `CalendarManager` byttes ut mot en `HashMap<LocalDate, ArrayList<Activity>>`, där datumen blev nycklar för HashMapen.