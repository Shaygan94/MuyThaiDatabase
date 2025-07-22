import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Integer getIdClubByName(String name) throws SQLException {
        try(PreparedStatement ps = con.prepareStatement(SqlQueries.GET_ID_CLUB_BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idClub");
            } else {
                return null;
            }
        }
    }

    public List<ThaiboxingClub> getClubsByCityCode(String codeCity) throws SQLException{
        List<ThaiboxingClub> clubs = new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement(SqlQueries.GET_CLUBS_BY_CODECITY)) {
            ps.setString(1, codeCity);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
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

    public List<ThaiboxingClub> getAllClubs() throws SQLException{
        List<ThaiboxingClub> clubs = new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement(SqlQueries.GET_ALL_CLUBS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
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