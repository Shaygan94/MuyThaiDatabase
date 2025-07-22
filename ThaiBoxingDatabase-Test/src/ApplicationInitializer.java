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
        this.countryRepository = new CountryRepository(connection);
        this.cityRepository = new CityRepository(connection);
        this.thaiClubRepository = new ThaiClubRepository(connection);
        this.clubsInCitiesRepository = new ClubsInCitiesRepository(connection);
        this.fighterRepository = new FighterRepository(connection);
        this.fileHandler = new FileHandler();
    }

    private void loadCountries() throws IOException, SQLException {
        List<Country> countries = fileHandler.readCountriesFromFile("country.txt");
        countryRepository.insertCountries(countries);
        System.out.println("Successfully inserted countries from file country.txt");
    }

    private void loadCities() throws IOException, SQLException {
        List<String> cityFiles = fileHandler.getAllCityFiles(".");

      for (String file : cityFiles) {
          String code = file.replace("cities_", "").replace(".txt", "");
          List<City> cities = fileHandler.readCitiesFromFile(file);
          cityRepository.insertCities(cities, code);
          System.out.println("Succesfully inserted cities from file " + file + ".");
      }
    }
    //Finne en måte slik at metoden i adminmeny addnewfighter kan lese alle cityCodes.

    private void loadThaiClubs() throws IOException, SQLException {
        List<ThaiboxingClub> thaiClubs = fileHandler.readThaiClubsFromFile("thaiboxingClubs.txt");
        thaiClubRepository.insertThaiClubs(thaiClubs);
        System.out.println("Successfully inserted thaiboxingClubs from file thaiboxingClubs.txt");
    }

    private void linkClubsToCities() throws IOException, SQLException {
        List<String> lines = Files.readAllLines(Paths.get("club_city.txt"));
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length == 2) {
                String clubName = parts[0].trim();
                String cityName = parts[1].trim();
                insertClubInCityByName(clubName, cityName);
            }
        }
    }
    private void insertClubInCityByName(String clubName, String cityName) throws SQLException {
        Integer idClub = thaiClubRepository.getIdClubByName(clubName);
        String codeCity = cityRepository.getCodeCityByName(cityName);

        if (idClub == null) {
            System.out.println("Club not found: " + clubName);
            return;
        }
        if (codeCity == null) {
            System.out.println("City not found: " + cityName);
            return;
        }

        clubsInCitiesRepository.insertClubsInCities(idClub, codeCity);
        System.out.println("Inserted: " + clubName + " in " + cityName);
    }

    private void loadFighters() throws IOException, SQLException {
        List<Fighter> fighters = fileHandler.readFightersFromFile("fighters.txt");
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

        fighterRepository.insertFighters(fightersForDb);
        System.out.println("Successfully inserted fighters from fighters.txt");
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
