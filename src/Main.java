import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3307/";
		String dbName = "world";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "jjnguy";
		String password = "asdf1234";
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName, 
					userName, password);
			JDapper jd = new JDapper(conn);

			String sql = "SELECT * FROM Country LIMIT 20";
			List<Country> results = jd.query(sql, Country.class);
			for (Country country : results) {
				System.out.println(country.name);
			}
			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}