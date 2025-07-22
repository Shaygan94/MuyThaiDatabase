import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CityRepository {
    private final Connection con;

    public CityRepository(Connection con) {
        this.con = con;
    }

    public void insertCities(List<City> cities, String codeCountry) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_CITIES)){
            for (City city : cities) {
                ps.setString(1, city.codeCity());
                ps.setString(2, city.nameCity());
                ps.setLong(3, city.populationCity());
                ps.setLong(4, city.surfaceAreaCity());
                ps.setString(5, codeCountry);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public String getCodeCityByName(String name) throws SQLException {
     try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_CODE_CITY_BY_NAME)) {
         ps.setString(1, name);
         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
             return rs.getString("codeCity");
         } else {
             return null;
         }
     }
    }

    public List<City> getCitiesByIDClub(int idClub) throws SQLException{
         List<City> cities = new ArrayList<>();

        try(PreparedStatement ps = con.prepareStatement(SqlQueries.GET_CITIES_BY_IDCLUB)){
            ps.setInt(1, idClub);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                cities.add(new City(
                rs.getString("codeCity"),
                rs.getString("nameCity"),
                rs.getLong("populationCity"),
                rs.getLong("surfaceAreaCity"),
                rs.getString("codeCountry")
                ));
            }
        }
        return cities;
    }

    public void insertCitiesAdmin(List<City> cities) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_CITIES)) {
            for (City city : cities) {
                ps.setString(1, city.codeCity());
                ps.setString(2, city.nameCity());
                ps.setLong(3, city.populationCity());
                ps.setLong(4, city.surfaceAreaCity());
                ps.setString(5, city.codeCountry()); // <--- Bruker verdien fra selve City-objektet
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
