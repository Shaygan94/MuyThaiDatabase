import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.ConnectIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectToDatabase {
    private Connection con;

    public Connection getCon(){
        return con;
    }

    public void connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("db.properties")){
            props.load(fis);

            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(props.getProperty("db.url"));
            dataSource.setUser(props.getProperty("db.user"));
            dataSource.setPassword(props.getProperty("db.password"));

            con = dataSource.getConnection();

        }
    }
}
