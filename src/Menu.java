
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Menu {
    private FileHandler fileHandler;
    private FighterRepository fighterRepository;
    private ThaiClubRepository thaiClubRepository;
    private CountryRepository countryRepository;
    private ClubsInCitiesRepository clubsInCitiesRepository;
    private CityRepository cityRepository;


    public Menu(FileHandler fileHandler, FighterRepository fighterRepository, ThaiClubRepository thaiClubRepository, CityRepository cityRepository, CountryRepository countryRepository, ClubsInCitiesRepository clubsInCitiesRepository) {
        this.fileHandler = fileHandler;
        this.fighterRepository = fighterRepository;
        this.thaiClubRepository = thaiClubRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.clubsInCitiesRepository = clubsInCitiesRepository;

    }

    public void menu() throws IOException, SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the Thaiboxing Database menu, please select one option");
        int choice = 0;
        while (choice != 6) {
            System.out.println("\n1. Show all fighters in a country");
            System.out.println("2. Show all fighters in a thaiboxing Club");
            System.out.println("3. Show all thaiboxing clubs in a City");
            System.out.println("4. Show which cities thaiboxing clubs are located");
            System.out.println("5. Admin menu");
            System.out.println("6. Exit");
            System.out.println("Your choice: ");
            if (scan.hasNextInt()) {
                choice = scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println(Messages.INVALID_INPUT + "1 and 6");
                scan.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> showAllFightersInCountry(scan);
                case 2 -> showAllFightersInClub(scan);
                case 3 -> showAllClubsInCity(scan);
                case 4 -> showCitiesClubsAreLocated(scan);
                case 5 -> showAdminMenu(scan);
                case 6 -> System.out.println("Exiting. Goodbye!");
                default -> System.out.println(Messages.INVALID_INPUT + "1 and 6");

            }
        }
    }

    private void showAdminMenu(Scanner scan) {
        try {
            String correctPassword = fileHandler.readAdminPassword("admin.properties");

            System.out.println("Enter admin password: ");
            String input = scan.nextLine();

            if (input.equals(correctPassword)) {
                AdminMenu adminMenu = new AdminMenu(fighterRepository, thaiClubRepository, cityRepository, countryRepository, clubsInCitiesRepository, fileHandler);
                adminMenu.start(scan);
            } else {
                System.out.println("Incorrect password. Please try again.");
            }

        } catch (IOException e) {
            System.out.println("Error reading password file " + e.getMessage());
        }
    }

    private void showCitiesClubsAreLocated(Scanner scan) {
        try {
            Set<String> clubNames = fileHandler.getAllClubNames("thaiboxingClubs.txt");

            System.out.println("Please enter club name. " + Messages.TYPE_LIST_CLUB_NAMES);

            while (true) {
                System.out.print("> ");
                String input = scan.nextLine().trim();

                if (input.equalsIgnoreCase("list")) {
                    System.out.println("Available club names: ");
                    clubNames.forEach(name -> System.out.println(" - " + name));
                } else if (clubNames.contains(input)) {
                    System.out.println("You selected club: " + input);
                    List<Integer> ids = thaiClubRepository.getAllIdClubsByName(input);
                    if (ids.isEmpty()) {
                        System.out.println("Club not found: " + input);
                        return;
                    }

                    Set<City> allCities = new HashSet<>();
                    for (Integer id : ids) {
                        List<City> cities = cityRepository.getCitiesByIDClub(id);
                        allCities.addAll(cities);
                    }
                    if (allCities.isEmpty()) {
                        System.out.println("No cities found for club: " + input);
                    } else {
                        System.out.println("Club '" + input + "' is located in these cities:");
                        allCities.forEach(city -> System.out.println(" - " + city.nameCity() + " (" + city.codeCity() + ")"));
                    }
                    break;

                } else {
                    System.out.println("Invalid club name. " + Messages.TYPE_LIST_CLUB_NAMES);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading thaiboxingClubs.txt file: " + e.getMessage());
        }
    }

    private void showAllClubsInCity(Scanner scan) {
        try {
            List<String> cityFiles = fileHandler.getAllCityFiles(".");
            Set<String> codeCities = fileHandler.getAllCityCodes(cityFiles.toArray(new String[0]));
            System.out.println("Please enter city code. " + Messages.TYPE_LIST_CICODES);

            while (true) {
                System.out.print("> ");
                String input = scan.nextLine().trim().toUpperCase();

                if (input.equals("LIST")) {
                    System.out.println("Available city codes:");
                    codeCities.forEach(code -> System.out.println(" - " + code));
                } else if (codeCities.contains(input)) {
                    System.out.println("You selected city code: " + input);

                    try {
                        List<ThaiboxingClub> clubs = thaiClubRepository.getClubsByCityCode(input);
                        if (clubs.isEmpty()) {
                            System.out.println("No thaiboxing clubs found for city code: " + input);
                        } else {
                            clubs.forEach(System.out::println);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting clubs from database: " + e.getMessage());
                    }

                    break;
                } else {
                    System.out.println("Invalid city code. " + Messages.TYPE_LIST_CICODES);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading city files: " + e.getMessage());
        }
    }

    private void showAllFightersInClub(Scanner scan) {
        try {
            Set<String> clubNames = fileHandler.getAllClubNames("thaiboxingClubs.txt");
            System.out.println("Please enter club name. " + Messages.TYPE_LIST_CLUB_NAMES);

            while (true) {
                System.out.print("> ");
                String input = scan.nextLine().trim();

                if (input.equalsIgnoreCase("LIST")) {
                    System.out.println("Available club names:");
                    clubNames.forEach(name -> System.out.println(" - " + name));
                } else if (clubNames.contains(input)) {
                    System.out.println("You selected club: " + input);
                    List<FighterDb> fighters = fighterRepository.getFightersByClubName(input);
                    if (fighters.isEmpty()) {
                        System.out.println("No fighters found for club: " + input);
                    } else {
                        fighters.forEach(f -> System.out.println(f.toString()));
                    }
                    break;
                } else {
                    System.out.println("Invalid club name. " + Messages.TYPE_LIST_CLUB_NAMES);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file thaiboxingClubs.txt  " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error getting fighters from database: " + e.getMessage());
        }
    }


    private void showAllFightersInCountry(Scanner scan) {
        try {
            Set<String> codeCountry = fileHandler.getAllCountryCodes("country.txt");
            System.out.println("Please enter the country code. " + Messages.TYPE_LIST_COCODES);
            while (true) {
                System.out.print("> ");
                String input = scan.nextLine().trim().toUpperCase();

                if (input.equals("LIST")) {
                    System.out.println("Available country codes:");
                    for (String code : codeCountry) {
                        System.out.println(" - " + code);
                    }
                } else if (codeCountry.contains(input)) {
                    System.out.println("You selected country code: " + input);

                    List<FighterDb> fighters = fighterRepository.getFightersByCountryCode(input);
                    if (fighters.isEmpty()) {
                        System.out.println("No fighters found for country code: " + input);
                    } else {
                        fighters.forEach(f -> System.out.println(f));
                    }
                    break;
                } else {
                    System.out.println(Messages.INVALID_CCODE + Messages.TYPE_LIST_COCODES);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting fighters from database: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading fighters.txt file: " + e.getMessage());
        }
    }
}
