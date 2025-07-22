import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FighterRepository {
    private final Connection con;

    public FighterRepository(Connection con) {
        this.con = con;
    }

    public void insertFighters(List<FighterDb> fighters) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_FIGHTERS)) {
            for (FighterDb fighter : fighters) {
                ps.setInt(1, fighter.idClub());
                ps.setString(2, fighter.fighterName());
                ps.setString(3, fighter.fighterStyle());
                ps.setInt(4, fighter.heightInCm());
                ps.setInt(5, fighter.weightInKg());
                ps.setDate(6, Date.valueOf(fighter.age()));
                ps.setString(7, fighter.codeCountry());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

   public List<FighterDb> getFightersByCountryCode(String code) throws SQLException{
        List<FighterDb> fighters = new ArrayList<>();
       try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_FIGHTERS_BY_COUNTRYCODE)) {
           ps.setString(1, code);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               fighters.add(new FighterDb(
                       rs.getInt("idClub"),
                       rs.getString("fighterName"),
                       rs.getString("fighterStyle"),
                       rs.getInt("height_In_Cm"),
                       rs.getInt("weight_In_Kg"),
                       rs.getDate("age").toLocalDate(),
                       rs.getString("codeCountry")
               ));
           }
       }
        return fighters;
   }

   public List<FighterDb> getFightersByClubName(String clubName) throws SQLException{
        List<FighterDb> fighters = new ArrayList<>();
       try (PreparedStatement ps = con.prepareStatement(SqlQueries.GET_FIGHTERS_BY_CLUBNAME)) {
           ps.setString(1, clubName);
           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               fighters.add(new FighterDb(
                       rs.getInt("idClub"),
                       rs.getString("fighterName"),
                       rs.getString("fighterStyle"),
                       rs.getInt("height_In_Cm"),
                       rs.getInt("weight_In_Kg"),
                       rs.getDate("age").toLocalDate(),
                       rs.getString("codeCountry")));
           }
       }
        return fighters;
   }

}
