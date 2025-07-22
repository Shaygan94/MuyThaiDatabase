import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ApplicationInitializer initializer = new ApplicationInitializer();
        try {
            initializer.initializeApplication();
            Menu menu = new Menu(
                    initializer.getFighterRepository(),
                    initializer.getThaiClubRepository(),
                    initializer.getCityRepository(),
                    initializer.getCountryRepository(),
                    initializer.getClubsInCitiesRepository()
            );
            menu.menu(); // Starter menyen etter at alt er lastet inn
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File handling error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
