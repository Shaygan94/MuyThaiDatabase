import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuUtils {
    CountryRepository countryRepository;
    CityRepository cityRepository;
    ThaiClubRepository thaiClubRepository;
    ClubsInCitiesRepository clubsInCitiesRepository;
    FighterRepository fighterRepository;
    FileHandler fileHandler;


    public MenuUtils(CountryRepository countryRepository, CityRepository cityRepository, ThaiClubRepository thaiClubRepository, ClubsInCitiesRepository clubsInCitiesRepository, FighterRepository fighterRepository, FileHandler fileHandler) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.thaiClubRepository = thaiClubRepository;
        this.clubsInCitiesRepository = clubsInCitiesRepository;
        this.fighterRepository = fighterRepository;
        this.fileHandler = fileHandler;
    }

    public String promptForCode(Scanner scan, Set<String> existingCodes, String typeName, String invalidMsg) {
        System.out.println("Enter fighter's " + typeName + "code. Type 'list' to see available codes.");

        while (true) {
            System.out.println("> ");
            String input = scan.nextLine().trim().toUpperCase();

            if (input.equals("LIST")) {
                System.out.println("Available " + typeName + " codes:");
                existingCodes.forEach(code -> System.out.println(" - " + code));
            } else if (existingCodes.contains(input)) {
                System.out.println(typeName + " code '" + input + "' selected. ");
                return input;
            } else if (!input.matches("[A-Z]{3}")) {
                System.out.println(invalidMsg + ". Must be 3 uppercase letters.");
            } else {
                return input;
            }
        }
    }

    public String promptForClubName(Scanner scan, Set<String> existingNames) {
        System.out.println("Enter fighter's club name. " + Messages.TYPE_LIST_CLUB_NAMES);

        while (true) {
            System.out.println("> ");
            String input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("list")) {
                System.out.println("Available clubs: ");
                existingNames.forEach(name -> System.out.println(" - " + name));
            } else if (existingNames.contains(input)) {
                System.out.println("Club '" + input + "' selected. ");
                return input;
            } else {
                return input;
            }
        }
    }

    private long readPositiveLong(Scanner scan) {
        while (true) {
            try {
                long value = Long.parseLong(scan.nextLine().trim());
                if (value > 0) return value;
                System.out.println("Value must be a positive number. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again");
            }
        }
    }

    private record LocationData(String name, long population, long surfaceArea) {
    }

    private LocationData readBasicLocationData(Scanner scan, String type) {
        System.out.println("Enter " + type + " name: ");
        String name = scan.nextLine().trim();

        System.out.println("Enter " + type + " population (number): ");
        long population = readPositiveLong(scan);

        System.out.println("Enter " + type + " surface area (number): ");
        long surfaceArea = readPositiveLong(scan);

        return new LocationData(name, population, surfaceArea);
    }


    public Country createNewCountry(Scanner scan, String code) {
        System.out.println("Country code not found. Lets add new country. ");

        LocationData data = readBasicLocationData(scan, "country");
        return new Country(code, data.name, data.population, data.surfaceArea);
    }


    public void saveNewCountry(Country country) {
        try {
            countryRepository.insertCountries(List.of(country));
            System.out.println("New country added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding new country to database: " + e.getMessage());
        }
        try {
            fileHandler.appendCountryToFile("country.txt", country);
            System.out.println("New country added to file country.txt.");
        } catch (IOException e) {
            System.out.println("Error writing new country to file country.txt: " + e.getMessage());
        }
    }


    public City createNewCity(Scanner scan, String codeCity, String codeCountry) {
        System.out.println("City code not found. Lets add new city. ");

        LocationData data = readBasicLocationData(scan, "city");

        return new City(codeCity, data.name, data.population, data.surfaceArea, codeCountry);

    }


    public void saveNewCity(City city) {
        try {
            cityRepository.insertCitiesAdmin(List.of(city));
            System.out.println("New city added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding new city to database: " + e.getMessage());
        }

        try {
            String fileName = "cities_" + city.codeCountry() + ".txt";
            fileHandler.appendCityToFile(fileName, city);
            System.out.println("New city added to file " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing city fo file: " + e.getMessage());

        }
    }

    public ThaiboxingClubAdmin createNewClub(Scanner scan, String clubName) {
        System.out.println("Lets add a new ThaiBoxing club with selected name: " + clubName + ". ");

        System.out.println("Enter club address: ");
        String address = scan.nextLine().trim();

        System.out.println("Enter club email: ");
        String email = scan.nextLine().trim();

        System.out.println("Enter club phone number: ");
        String phone = scan.nextLine().trim();

        System.out.println("Enter year established (number, or 0 if unknown): ");
        int yearEstablished = 0;
        while (true) {
            try {
                yearEstablished = Integer.parseInt(scan.nextLine().trim());
                if (yearEstablished >= 0) break;
                System.out.println("Year must be a positive number or 0. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again");
            }

        }
        System.out.println("Enter club owner name:  ");
        String owner = scan.nextLine().trim();

        return new ThaiboxingClubAdmin(clubName, address, email, phone, yearEstablished, owner);
    }

    public void saveNewClub(ThaiboxingClubAdmin club) {
        try {
            thaiClubRepository.insertThaiClubsAdmin(List.of(club));
            System.out.println("New thaiboxing club added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding new thaiboxing club to database: " + e.getMessage());
        }

        try {
            fileHandler.appendThaiClubToFile("thaiboxingClubs.txt", club);
            System.out.println("New thaiboxing club added to file thaiboxingClubs.txt.");
        } catch (IOException e) {
            System.out.println("Error writing new thaiboxing club to file thaiboxingClubs.txt: " + e.getMessage());
        }
    }

    public Integer selectSpecificClub(Scanner scan, String clubName, String codeCity) {
        try {
            List<ThaiboxingClub> clubs = thaiClubRepository.getClubsByNameAndCity(clubName, codeCity);

            if (clubs.isEmpty()) {
                return null;
            }

            if (clubs.size() == 1) {
                return clubs.get(0).idClub();
            }

            System.out.println("Multiple clubs with name '" + clubName + "' found in this city:");
            for (int i = 0; i < clubs.size(); i++) {
                ThaiboxingClub club = clubs.get(i);
                System.out.println((i + 1) + ". " + club.clubName() + " - " + club.address() + " (Owner: " + club.owner() + ")");
            }

            while (true) {
                System.out.print("Choose club (1-" + clubs.size() + "): ");
                try {
                    int choice = Integer.parseInt(scan.nextLine().trim());
                    if (choice >= 1 && choice <= clubs.size()) {
                        return clubs.get(choice - 1).idClub();
                    } else {
                        System.out.println("Invalid choice. Please choose between 1 and " + clubs.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching clubs: " + e.getMessage());
            return null;
        }
    }

    public FighterDb createNewFighter(Scanner scan, int idClub, String codeCountry) {
        System.out.println("Enter fighter's name: ");
        String fighterName = scan.nextLine().trim();

        String[] fightingStyles = {
                "Muay Mat (puncher)",
                "Muay Khao (knee fighter)",
                "Muay Sok (elbow fighter)",
                "Muay Tae (kicker)",
                "Muay Femur (technician)",
                "Muay Bouk (pressure fighter)",
                "Muay Plum (elusive fighter)",
                "Unknown"
        };

        System.out.println("Choose fighting style: ");
        for (int i = 0; i < fightingStyles.length; i++) {
            System.out.println((i + 1) + ". " + fightingStyles[i]);
        }
        String fightingStyle = "";
        while (true) {
            System.out.println("Your choice (1-" + fightingStyles.length + "): ");
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());
                if (choice >= 1 && choice <= fightingStyles.length) {
                    fightingStyle = fightingStyles[choice - 1];
                    break;
                } else {
                    System.out.println("Invalid choice. Please choose bewteen 1 and " + fightingStyles.length + ". ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please choose bewteen 1 and " + fightingStyles.length + ". ");
            }
        }
        System.out.println("Enter fighter's height in cm: ");
        int height = Integer.parseInt(scan.nextLine().trim());

        System.out.println("Enter fighter's weight in kg: ");
        int weight = Integer.parseInt(scan.nextLine().trim());

        System.out.println("Enter fighter's date of birth (yyyy-mm-dd): ");
        LocalDate dateOfBirth = null;
        while (true) {
            try {
                String dobInput = scan.nextLine().trim();
                dateOfBirth = LocalDate.parse(dobInput);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please enter in the format yyyy-mm-dd. ");
            }
        }

        return new FighterDb(idClub, fighterName, fightingStyle, height, weight, dateOfBirth, codeCountry);
    }

    public void saveNewFighter(FighterDb fighterDb, String clubName) {
        try {
            fighterRepository.insertFighters(List.of(fighterDb));
            System.out.println("New fighter added to database.");
        } catch (SQLException e) {
            System.out.println("Error inserting fighters into db " + e.getMessage());
        }
        try {
            fileHandler.appendFighterToFile("fighters.txt", fighterDb, clubName);
        } catch (IOException e) {
            System.out.println("Error writing fighter to fighters.txt: " + e.getMessage());
        }
    }

}