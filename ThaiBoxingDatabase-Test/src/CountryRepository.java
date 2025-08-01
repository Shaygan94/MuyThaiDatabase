import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CountryRepository {
    private final Connection con;

    public CountryRepository(Connection con){
        this.con = con;
    }

    public void insertCountries(List<Country> countries) throws SQLException {
        PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_COUNTRIES);
        for (Country country : countries) {
            ps.setString(1, country.codeCountry());
            ps.setString(2, country.nameCountry());
            ps.setLong(3, country.populationCountry());
            ps.setLong(4, country.surfaceAreaCountry());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}