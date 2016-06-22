import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;

import jdapper.JDapper;

public class Main {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "test"; //"world";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "bob@123";
		Class.forName(driver).newInstance();
		Connection conn = DriverManager.getConnection(url + dbName, userName, password);
		JDapper jd = new JDapper(conn);

		Stuff s = new Stuff();
		//s.Id = 0;
		s.string1 = "Hi youtube";
		s.number1 = 42;
		
		//jd.update(s);
		jd.insert(s);
	}
}
