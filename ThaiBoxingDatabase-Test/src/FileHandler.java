import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class FileHandler {
    public List<Country> readCountriesFromFile(String fileName) throws IOException {
        List<Country> countries = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(fileName))) {
            while (scan.hasNextLine()) {
                String codeCountry = scan.nextLine();
                String nameCountry = scan.nextLine();
                long populationCountry = Long.parseLong(scan.nextLine());
                long surfaceAreaCountry = Long.parseLong(scan.nextLine());
                countries.add(new Country(codeCountry, nameCountry, populationCountry, surfaceAreaCountry));
                if (scan.hasNextLine()) {
                    scan.nextLine();
                }
            }

        }
        return countries;
    }

    public List<City> readCitiesFromFile(String fileName) throws IOException {
        List<City> cities = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(fileName))) {
            while (scan.hasNextLine()) {
                String codeCity = scan.nextLine();
                String nameCity = scan.nextLine();
                long populationCity = Long.parseLong(scan.nextLine());
                long surfaceAreaCity = Long.parseLong(scan.nextLine());
                String codeCountry = scan.nextLine();
                cities.add(new City(codeCity, nameCity, populationCity, surfaceAreaCity, codeCountry));
                if (scan.hasNextLine()) {
                    scan.nextLine();
                }
            }
        }
        return cities;
    }

    public List<String> getAllCityFiles(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("cities_") && name.endsWith(".txt") );

        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    public List<ThaiboxingClub> readThaiClubsFromFile(String fileName) throws IOException {
        List<ThaiboxingClub> thaiClubs = new ArrayList<>();

        try (Scanner scan = new Scanner(new File(fileName))) {
            while (scan.hasNextLine()) {

                String clubName = scan.nextLine();
                String address = scan.nextLine();
                String email = scan.nextLine();
                String phone = scan.nextLine();
                int establishedYear = Integer.parseInt(scan.nextLine());
                String owner = scan.nextLine();
                thaiClubs.add(new ThaiboxingClub(0, clubName, address, email, phone, establishedYear, owner));
                if (scan.hasNextLine()) {
                    scan.nextLine();
                }
            }
        }
        return thaiClubs;
    }

    public List<Fighter> readFightersFromFile(String fileName) throws IOException {
        List<Fighter> fighters = new ArrayList<>();
        try (Scanner scan = new Scanner(new File(fileName))) {
            while (scan.hasNextLine()) {
                String fighterName = scan.nextLine();
                String fighterStyle = scan.nextLine();
                int height = Integer.parseInt(scan.nextLine());
                int weight = Integer.parseInt(scan.nextLine());
                LocalDate birthDate = LocalDate.parse(scan.nextLine());
                String codeCountry = scan.nextLine();
                String clubName = scan.nextLine();
                fighters.add(new Fighter(0, fighterName, fighterStyle, height, weight, birthDate, codeCountry, clubName));
                if (scan.hasNextLine()) {
                    scan.nextLine();
                }
            }
        }
        return fighters;
    }

    public Set<String> getAllClubNames(String fileName) throws IOException {
        List<ThaiboxingClub> thaiClubs = readThaiClubsFromFile(fileName);
        Set<String> clubNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        thaiClubs.forEach(club -> clubNames.add(club.clubName()));
        return clubNames;
    }

    public Set<String> getAllCountryCodes(String fileName) throws IOException {
        List<Country> countries = readCountriesFromFile(fileName);
        Set<String> codeCountry = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        countries.forEach(country -> codeCountry.add(country.codeCountry()));
        return codeCountry;

    }

    public Set<String> getAllCityCodes(String[] fileNames) throws IOException {
        Set<String> codes = new TreeSet<>();

        for (String file : fileNames) {
                List<City> cities = readCitiesFromFile(file);
                cities.forEach(city -> codes.add(city.codeCity()));
            }
        return codes;
    }

    public String readAdminPassword(String filePath) throws IOException {
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Paths.get(filePath)));
            return props.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void appendCountryToFile(String fileName, Country country) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(country.codeCountry());
            writer.newLine();
            writer.write(country.nameCountry());
            writer.newLine();
            writer.write(Long.toString(country.populationCountry()));
            writer.newLine();
            writer.write(Long.toString(country.surfaceAreaCountry()));
            writer.newLine();
            writer.write("-----");
            writer.newLine();
        }
    }
    public void appendCityToFile(String fileName, City city) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(city.codeCity());
            writer.newLine();
            writer.write(city.nameCity());
            writer.newLine();
            writer.write(Long.toString(city.populationCity()));
            writer.newLine();
            writer.write(Long.toString(city.surfaceAreaCity()));
            writer.newLine();
            writer.write(city.codeCountry());
            writer.newLine();
            writer.write("-----");
            writer.newLine();
        }
    }
}