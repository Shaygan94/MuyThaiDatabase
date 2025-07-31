import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThaiClubRepository {
    private final Connection con;

    public ThaiClubRepository(Connection con) {
        this.con = con;
    }

    public void insertThaiClubs(List<ThaiboxingClub> thaiClubs) throws SQLException {
        PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_THAI_CLUBS, PreparedStatement.RETURN_GENERATED_KEYS);
        for (ThaiboxingClub thaiClub : thaiClubs) {
            ps.setString(1, thaiClub.clubName());
            ps.setString(2, thaiClub.address());
            ps.setString(3, thaiClub.email());
            ps.setString(4, thaiClub.phone());
            ps.setInt(5, thaiClub.establishedYear());
            ps.setString(6, thaiClub.owner());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    public void insertThaiClubsAdmin(List<ThaiboxingClubAdmin> thaiClubs) throws SQLException {
        PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_THAI_CLUBS, PreparedStatement.RETURN_GENERATED_KEYS);
        for (ThaiboxingClubAdmin thaiClub : thaiClubs) {
            ps.setString(1, thaiClub.clubName());
            ps.setString(2, thaiClub.address());
            ps.setString(3, thaiClub.email());
            ps.setString(4, thaiClub.phone());
            ps.setInt(5, thaiClub.establishedYear());
            ps.setString(6, thaiClub.owner());
            ps.addBatch();
        }
        ps.executeBatch();
    }


    public Integer getIdClubByName(String name) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_ID_CLUB_BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idClub");
            } else {
                return null;
            }
        }
    }

    public List<Integer> getAllIdClubsByName(String name) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(SqlQueries.GET_ID_CLUB_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("idClub"));
                }
            }
        }
        return ids;
    }

    public Integer getLastInsertedIdByName(String clubName) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(SqlQueries.GET_LAST_ID_BY_NAME)) {
            stmt.setString(1, clubName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idClub");
                }
            }
        }
        return null;
    }

    public List<ThaiboxingClub> getClubsByCityCode(String codeCity) throws SQLException {
        List<ThaiboxingClub> clubs = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_CLUBS_BY_CODECITY)) {
            ps.setString(1, codeCity);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clubs.add(new ThaiboxingClub(
                        rs.getInt("idClub"),
                        rs.getString("clubName"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("establishedYear"),
                        rs.getString("owner")
                ));
            }
        }
        return clubs;
    }


    public List<ThaiboxingClub> getClubsByNameAndCity(String clubName, String cityCode) throws SQLException {
        List<ThaiboxingClub> clubs = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_CLUBS_BY_NAME_AND_CITY)) {
            ps.setString(1, clubName);
            ps.setString(2, cityCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clubs.add(new ThaiboxingClub(
                        rs.getInt("idClub"),
                        rs.getString("clubName"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("establishedYear"),
                        rs.getString("owner")
                ));
            }
        }
        return clubs;
    }

}