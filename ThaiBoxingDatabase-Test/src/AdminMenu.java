import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AdminMenu {
    private FighterRepository fighterRepository;
    private ThaiClubRepository thaiClubRepository;
    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private ClubsInCitiesRepository clubsInCitiesRepository;;
    private FileHandler fileHandler;

    public AdminMenu(FighterRepository fighterRepository, ThaiClubRepository thaiClubRepository, CityRepository cityRepository, CountryRepository countryRepository, ClubsInCitiesRepository clubsInCitiesRepository, FileHandler fileHandler) {
        this.fighterRepository = fighterRepository;
        this.thaiClubRepository = thaiClubRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.clubsInCitiesRepository = clubsInCitiesRepository;
        this.fileHandler = fileHandler;
    }

   public void start(Scanner scan) {
        int choice = 0;
        while (choice != 2) {
            System.out.println("\n --- Admin Menu ---");
            System.out.println("1. Add new fighter");
            System.out.println("2. Return to main menu");
            System.out.println("Your choice: ");
            if (scan.hasNextInt()) {
                choice = scan.nextInt();
                scan.nextLine();
            } else {
                System.out.println(Messages.INVALID_INPUT + "1 and 2");
                scan.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> addNewFighter(scan);
                case 2 -> System.out.println("Returning to main menu.");
                default -> System.out.println(Messages.INVALID_INPUT + "1 and 2");
            }
        }

   }

    private void addNewFighter(Scanner scan) {
        try {
            Set<String> codeCountryList = fileHandler.getAllCountryCodes("country.txt");

            String chosenCountryCode = promptForCountryCode(scan, codeCountryList);


            if (!codeCountryList.contains(chosenCountryCode)) {
                Country newCountry = createNewCountry(scan, chosenCountryCode);
                saveNewCountry(newCountry);
                codeCountryList.add(chosenCountryCode);
            }


            List<String> cityFiles = fileHandler.getAllCityFiles(".");
            Set<String> codeCityList = fileHandler.getAllCityCodes(cityFiles.toArray(new String[0]));

            String chosenCityCode = promptForCityCode(scan, codeCityList);

            if(!codeCityList.contains(chosenCityCode)) {
                City newCity = createNewCity(scan, chosenCityCode, chosenCountryCode);
                saveNewCity(newCity);
                codeCityList.add(chosenCityCode);
            }


        } catch (IOException e) {
            System.out.println("Error reading file country.txt: " + e.getMessage());;
        }
    }

    private String promptForCountryCode(Scanner scan, Set<String> existingCountryCodes) {
        System.out.println("Enter fighter's country code. " + Messages.TYPE_LIST_CCODE);

        while(true) {
            System.out.println("> ");
            String input = scan.nextLine().trim().toUpperCase();

            if (input.equals("LIST")) {
                System.out.println("Available country codes:");
                existingCountryCodes.forEach(code -> System.out.println(" - " + code));
            } else if (existingCountryCodes.contains(input)) {
                System.out.println("Country code '" + input + "' selected.");
                return input;
            } else {
                if (!input.matches("[A-Z]{3}")) {
                    System.out.println(Messages.INVALID_CCODE + "Must be 3 uppercase letters.");
                    continue;
                }
                return input;
            }
        }

    }
    private Country createNewCountry(Scanner scan, String code) {
        System.out.println("Country code not found. Lets add new country. ");

        System.out.println("Enter country name: ");
        String name = scan.nextLine().trim();

        System.out.println("Enter country population (number): ");
        long population = readPositiveLong(scan);

        System.out.println("Enter country surface area (number): ");
        long area = readPositiveLong(scan);

        return new Country(code, name, population, area);

    }
    private long readPositiveLong(Scanner scan) {
        while (true) {
            try {
                long value = Long.parseLong(scan.nextLine().trim());
                if (value > 0 ) return value;
                System.out.println("Value must be a positive number. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again");
            }
        }
    }

    private void saveNewCountry(Country country) {
        try{
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
    private String promptForCityCode(Scanner scan, Set<String> existingCityCodes) {
        System.out.println("Enter fighter's city code. " + Messages.TYPE_LIST_CCODE);

        while(true) {
            System.out.println("> ");
            String input = scan.nextLine().trim().toUpperCase();

            if (input.equals("LIST")) {
                System.out.println("Available country codes:");
                existingCityCodes.forEach(code -> System.out.println(" - " + code));
            } else if (existingCityCodes.contains(input)) {
                System.out.println("city code '" + input + "' selected.");
                return input;
            } else {
                if (!input.matches("[A-Z]{3}")) {
                    System.out.println(Messages.INVALID_CICODE + "Must be 3 uppercase letters.");
                    continue;
                }
                return input;
            }
        }
    }

    private City createNewCity(Scanner scan, String codeCity, String codeCountry) {
        System.out.println("City code not found. Lets add new city. ");

        System.out.println("Enter city name: ");
        String nameCity = scan.nextLine().trim();

        System.out.println("Enter city population (number): ");
        long population = readPositiveLong(scan);

        System.out.println("Enter city surface area (number): ");
        long area = readPositiveLong(scan);

        return new City (codeCity, nameCity, population, area, codeCountry);

    }
    private void saveNewCity(City city) {
        try{
            cityRepository.insertCitiesAdmin(List.of(city));
            System.out.println("New city added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding new city to database: " + e.getMessage());
        }

        try {
            String fileName = "cities_" + city.codeCountry() + ".txt";
            fileHandler.appendCityToFile(fileName, city);
            System.out.println("New city added to file " +  fileName);
        } catch (IOException e) {
            System.out.println("Error writing city fo file: " + e.getMessage());

        }
    }
}
