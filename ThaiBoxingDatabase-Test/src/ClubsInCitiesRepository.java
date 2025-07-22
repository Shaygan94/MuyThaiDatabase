import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}