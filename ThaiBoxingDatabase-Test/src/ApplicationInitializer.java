import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationInitializer {

    private Connection connection;
    private FileHandler fileHandler;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private ThaiClubRepository thaiClubRepository;
    private ClubsInCitiesRepository clubsInCitiesRepository;
    private FighterRepository fighterRepository;

    public void initializeApplication() throws SQLException, IOException {
        connectDatabase();
        initializeRepositories();
        loadCountries();
        loadCities();
        loadThaiClubs();
        linkClubsToCities();
        loadFighters();
    }

    private void connectDatabase() throws SQLException, IOException {
        ConnectToDatabase connect = new ConnectToDatabase();
        connect.connectToDatabase();
        this.connection = connect.getCon();
        System.out.println("Connected to ThaiboxingDatabase");
    }

    private void initializeRepositories() {
        this.fileHandler = new FileHandler();
        this.countryRepository = new CountryRepository(connection);
        this.cityRepository = new CityRepository(connection);
        this.thaiClubRepository = new ThaiClubRepository(connection);
        this.clubsInCitiesRepository = new ClubsInCitiesRepository(connection);
        this.fighterRepository = new FighterRepository(connection);

    }

    private void loadCountries() throws IOException, SQLException {
        List<Country> countries = fileHandler.readCountriesFromFile("country.txt");
        if (countries.isEmpty()) {
            System.out.println("No countries found in country.txt - file is empty");
        } else {
            countryRepository.insertCountries(countries);
            System.out.println("Successfully inserted " + countries.size() + " countries from file country.txt");
        }
    }

    private void loadCities() throws IOException, SQLException {
        List<String> cityFiles = fileHandler.getAllCityFiles(".");

        if (cityFiles.isEmpty()) {
            System.out.println("No city files found.");
            return;
        }

        for (String file : cityFiles) {
            String code = file.replace("cities_", "").replace(".txt", "");
            List<City> cities = fileHandler.readCitiesFromFile(file);
            if (cities.isEmpty()) {
                System.out.println("No cities found in file " + file + " - file is empty.");
            } else {
                cityRepository.insertCities(cities, code);
                System.out.println("Successfully inserted " + cities.size() + " cities from file " + file + ".");
            }
        }
    }

    private void loadThaiClubs() throws IOException, SQLException {
        List<ThaiboxingClub> thaiClubs = fileHandler.readThaiClubsFromFile("thaiboxingClubs.txt");
        if (thaiClubs.isEmpty()) {
            System.out.println("No thaiboxing clubs found in thaiboxingClubs.txt - file is empty.");
        } else {
            thaiClubRepository.insertThaiClubs(thaiClubs);
            System.out.println("Successfully inserted " + thaiClubs.size() + " thaiboxing clubs from file thaiboxingClubs.txt");
        }
    }


    private void linkClubsToCities() throws IOException, SQLException {
        List<String> lines = Files.readAllLines(Paths.get("club_city.txt"));
        if (lines.isEmpty()) {
            System.out.println("No club-city links found in club_city.txt - file is empty.");
            return;
        }

        int insertedCount = 0;
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                int idClub;
                try {
                    idClub = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid club id format: " + parts[0]);
                    continue;
                }
                String codeCity = parts[1].trim();

                clubsInCitiesRepository.insertClubsInCities(idClub, codeCity);
                System.out.println("Inserted club (id=" + idClub + ") into city (code=" + codeCity + ")");
                insertedCount++;
            }
        }
        if (insertedCount == 0) {
            System.out.println("No valid club-city links processed from club_city.txt");
        }
    }

    private void loadFighters() throws IOException, SQLException {
        List<Fighter> fighters = fileHandler.readFightersFromFile("fighters.txt");
        if (fighters.isEmpty()) {
            System.out.println("No fighters found in fighters.txt - file is empty.");
            return;
        }

        List<FighterDb> fightersForDb = new ArrayList<>();

        for (Fighter f : fighters) {
            try {
                Integer idClub = thaiClubRepository.getIdClubByName(f.clubName());
                if (idClub == null) {
                    System.out.println("Club not found for fighter: " + f.fighterName() + ", skipping.");
                    continue;
                }
                fightersForDb.add(new FighterDb(
                        idClub,
                        f.fighterName(),
                        f.fighterStyle(),
                        f.heightInCm(),
                        f.weightInKg(),
                        f.age(),
                        f.codeCountry()
                ));
            } catch (SQLException e) {
                System.out.println("Error getting id for thaiboxing club: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (fightersForDb.isEmpty()) {
            System.out.println("No valid fighters processed from fighters.txt");
        } else {
            fighterRepository.insertFighters(fightersForDb);
            System.out.println("Successfully inserted " + fightersForDb.size() + " fighters from fighters.txt");
        }
    }

    public FileHandler getFileHandler() {
        return this.fileHandler;
    }

    public FighterRepository getFighterRepository() {
        return this.fighterRepository;
    }

    public ThaiClubRepository getThaiClubRepository() {
        return this.thaiClubRepository;
    }

    public CityRepository getCityRepository() {
        return this.cityRepository;
    }

    public CountryRepository getCountryRepository() {
        return this.countryRepository;
    }

    public ClubsInCitiesRepository getClubsInCitiesRepository() {
        return this.clubsInCitiesRepository;
    }
}
