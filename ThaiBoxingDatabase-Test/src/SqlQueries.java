public class SqlQueries {
    public static final String INSERT_COUNTRIES = "INSERT IGNORE INTO country (codeCountry, nameCountry, populationCountry, surfaceAreaCountry) VALUES (?, ?, ?, ?)";
    public static final String INSERT_CITIES = "INSERT IGNORE INTO city (codeCity, nameCity, populationCity, surfaceAreaCity, codeCountry) VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_THAI_CLUBS = "INSERT IGNORE INTO thaiboxingclub (clubName, address, email, phone, establishedYear, owner) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_CLUBS_IN_CITIES = "INSERT IGNORE INTO clubs_in_cities (idClub, codeCity) VALUES (?, ?)";
    public static final String INSERT_FIGHTERS = "INSERT IGNORE INTO fighter (idClub, fighterName, fighterStyle, height_in_cm, weight_in_kg, age, codeCountry) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_ID_CLUB_BY_NAME = "SELECT idClub FROM thaiboxingClub WHERE clubName = ?";
    public static final String GET_CODE_CITY_BY_NAME = "SELECT codeCity FROM city WHERE nameCity = ?";

    public static final String GET_FIGHTERS_BY_COUNTRYCODE = "SELECT id_Fighter, idClub, fighterName, fighterStyle, height_in_cm, weight_in_kg, age, codeCountry FROM fighter WHERE codeCountry = ? ";
    public static final String GET_FIGHTERS_BY_CLUBNAME =
            "SELECT " +
                    "f.id_Fighter, f.idClub, f.fighterName, f.fighterStyle, f.height_in_cm, f.weight_in_kg, f.age, f.codeCountry " +
                    "FROM fighter f " +
                    "INNER JOIN thaiboxingclub t ON f.idClub = t.idClub " +
                    "WHERE UPPER(t.clubName) = ?";


    public static final String GET_CLUBS_BY_CODECITY = "SELECT c.idClub, c.clubName, c.address, c.email, c.phone, c.establishedYear, c.owner FROM thaiboxingclub c JOIN clubs_in_cities cic ON c.idClub = cic.idClub WHERE cic.codeCity = ? ";
    public static final String GET_ALL_CLUBS = "SELECT idClub, clubName, address, email, phone, establishedYear, owner FROM thaiboxingclub";
    public static final String GET_CITIES_BY_IDCLUB = "SELECT c.codeCity, c.nameCity, c.populationCity, c.surfaceAreaCity, c.codeCountry FROM city c JOIN clubs_in_cities cic ON c.codeCity = cic.codeCity WHERE cic.idClub = ? ";
}