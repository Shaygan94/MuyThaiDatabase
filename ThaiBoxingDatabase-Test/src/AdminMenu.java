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
    private ClubsInCitiesRepository clubsInCitiesRepository;
    private FileHandler fileHandler;
    private MenuUtils menuUtils;

    public AdminMenu(FighterRepository fighterRepository, ThaiClubRepository thaiClubRepository, CityRepository cityRepository, CountryRepository countryRepository, ClubsInCitiesRepository clubsInCitiesRepository, FileHandler fileHandler) {
        this.menuUtils = new MenuUtils(countryRepository, cityRepository, thaiClubRepository, clubsInCitiesRepository, fighterRepository, fileHandler);
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
            String chosenCountryCode = menuUtils.promptForCode(scan, codeCountryList, "country",
                    Messages.INVALID_CCODE + Messages.TYPE_LIST_COCODES);

            if (!codeCountryList.contains(chosenCountryCode)) {
                Country newCountry = menuUtils.createNewCountry(scan, chosenCountryCode);
                menuUtils.saveNewCountry(newCountry);
                codeCountryList.add(chosenCountryCode);
            }

            List<String> cityFiles = fileHandler.getAllCityFiles(".");
            Set<String> codeCityList = fileHandler.getAllCityCodes(cityFiles.toArray(new String[0]));

            String chosenCityCode = menuUtils.promptForCode(scan, codeCityList, "city",
                    Messages.INVALID_CICODE + Messages.TYPE_LIST_CICODES);

            if (!codeCityList.contains(chosenCityCode)) {
                City newCity = menuUtils.createNewCity(scan, chosenCityCode, chosenCountryCode);
                menuUtils.saveNewCity(newCity);
                codeCityList.add(chosenCityCode);
            }

            Set<String> clubNames = fileHandler.getAllClubNames("thaiboxingClubs.txt");
            String chosenClubName = menuUtils.promptForClubName(scan, clubNames).trim();

            String normalizedClubName = clubNames.stream()
                    .filter(name -> name.equalsIgnoreCase(chosenClubName))
                    .findFirst()
                    .orElse(chosenClubName);

            boolean createNewClub = false;

            boolean clubExists = clubNames.stream()
                    .anyMatch(name -> name.equalsIgnoreCase(normalizedClubName));

            if (clubExists) {
                Set<String> existingCity = clubsInCitiesRepository.getCityCodeByClubName(normalizedClubName);

                if (existingCity == null || !existingCity.contains(chosenCityCode)) {
                    createNewClub = true; //
                } else {
                    System.out.println("A club with this name already exists in this city. Do you want to create a NEW club with different details (Y/N)?");
                    String answer = scan.nextLine().trim().toUpperCase();
                    if (answer.equals("Y")) createNewClub = true;
                }
            } else {
                createNewClub = true;
            }

            if (createNewClub) {
                ThaiboxingClubAdmin newClub = menuUtils.createNewClub(scan, normalizedClubName);
                menuUtils.saveNewClub(newClub);
                clubNames.add(normalizedClubName);
            }

            Integer idClub;

            if (createNewClub) {
                idClub = thaiClubRepository.getLastInsertedIdByName(normalizedClubName);
            } else {
                idClub = menuUtils.selectSpecificClub(scan, normalizedClubName, chosenCityCode);
                if (idClub == null) {
                    System.out.println("Could not select club. returning to menu.");
                    return;
                }
            }

            fileHandler.appendClubCityToFile("club_city.txt", idClub.toString(), chosenCityCode);
            clubsInCitiesRepository.insertClubsInCities(idClub, chosenCityCode);

            FighterDb newFighter = menuUtils.createNewFighter(scan, idClub, chosenCountryCode);
            menuUtils.saveNewFighter(newFighter, normalizedClubName);

        } catch (IOException e) {
            System.out.println("Error reading reqiured data files " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error inserting data to database" + e.getMessage());
        }
    }


}
