import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ClubsInCitiesRepository {
    private final Connection con;

    public ClubsInCitiesRepository(Connection con) {
        this.con = con;
    }

    public void insertClubsInCities(int idClub, String codeCity) throws SQLException {

        try (PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_CLUBS_IN_CITIES)) {
            ps.setInt(1, idClub);
            ps.setString(2, codeCity);
            ps.executeUpdate();
        }
    }

    public Set<String> getCityCodeByClubName(String clubName) throws SQLException {
        Set<String> cityCodes = new HashSet<>();
        try (PreparedStatement stmt = con.prepareStatement(SqlQueries.GET_CITYCODE_BY_CLUBNAME)) {
            stmt.setString(1, clubName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cityCodes.add(rs.getString("codeCity"));
                }
            }
        }
        return cityCodes;
    }
}