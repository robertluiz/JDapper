package jdapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JDapper {

	private final Connection conn;
	private static final String RAW_INSERT = "INSERT INTO %s (%s) VALUES (%s);";
	private static final String RAW_UPDATE = "UPDATE %s SET %s WHERE Id = '%s';";
	private static final String RAW_DELETE = "DELETE FROM %s WHERE Id = '%s'";
	private static final String RAW_SEARCH = "SELECT * FROM %s WHERE %s";

	public JDapper(Connection conn) {
		this.conn = conn;
	}

	public <T> List<T> query(String sql, Class<T> type, Object... params) throws Exception {
		List<T> results = new ArrayList<T>();

		Field[] fields = type.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
		}

		PreparedStatement statement = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			statement.setObject(i + 1, params[i]);
		}

		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			T obj = type.getConstructor().newInstance();

			for (Field field : fields) {
				Object data = rs.getObject(field.getName());
				field.set(obj, data);
			}

			results.add(obj);
		}

		return results;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> search(Object data) throws Exception {

		List<T> results = new ArrayList<T>();
		Class<T> type = (Class<T>) data.getClass();
		Field[] fields = type.getDeclaredFields();

		String columnNames = "";

		for (Field field : fields) {
			field.setAccessible(true);
			String value = "";
			value = String.valueOf(field.get(data));
			if (!value.equals("") && !value.equals("null") && !field.getName().equals("Id")) {
				columnNames += "OR " + field.getName() + " LIKE \'%" + value + "%\' ";
			}
		}
		columnNames = columnNames.substring(2);

		String sql = String.format(RAW_SEARCH, type.getName(), columnNames);

		PreparedStatement statement = conn.prepareStatement(sql);

		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			T obj = type.getConstructor().newInstance();

			for (Field field : fields) {
				Object dt = rs.getObject(field.getName());
				field.set(obj, dt);
			}

			results.add(obj);
		}

		return results;
	}

	public void insert(Object data) throws Exception {
		
		Class<?> type = data.getClass();
		Field[] fields = type.getDeclaredFields();

		String columnNames = "";
		String values = "";
		for (Field field : fields) {
			field.setAccessible(true);
			columnNames += "," + field.getName();
			values += ",?";
		}
		columnNames = columnNames.substring(1);
		values = values.substring(1);

		String sql = String.format(RAW_INSERT, type.getName(), columnNames, values);

		PreparedStatement statement = conn.prepareStatement(sql);

		for (int i = 0; i < fields.length; i++) {
			statement.setObject(i + 1, fields[i].get(data));
		}

		statement.execute();
	}

	public void insert(List<Object> data) throws Exception {
		for (Object ob : data) {
			insert(ob);
		}
	}

	public void update(Object data) throws Exception {

		Class<?> type = data.getClass();
		Field[] fields = type.getDeclaredFields();

		String columnNames = "";
		String Id = "";
		for (Field field : fields) {
			field.setAccessible(true);
			String value = "";
			value = String.valueOf(field.get(data));
			if (!value.equals("") && !value.equals("null") && !field.getName().equals("Id")) {
				columnNames += ", " + field.getName() + " = \'" + value + "\' ";
			}
			if (field.getName().equals("Id")) {

				Id = String.valueOf(field.get(data));
			}
		}

		if (Id.equals("") || Id.equals("null")) {
			throw new IllegalArgumentException("Id not Found!");
		}
		columnNames = columnNames.substring(1);

		String sql = String.format(RAW_UPDATE, type.getName(), columnNames, Id);

		PreparedStatement statement = conn.prepareStatement(sql);

		statement.execute();

	}

	public void update(List<Object> data) throws Exception {

		for (Object ob : data) {
			update(ob);
		}
	}

	public void delete(Object data) throws Exception {

		Class<?> type = data.getClass();
		Field[] fields = type.getDeclaredFields();

		String Id = "";
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("Id")) {

				Id = String.valueOf(field.get(data));
			}
		}

		if (Id.equals("") || Id.equals("null")) {
			throw new IllegalArgumentException("Id not Found!");
		}

		String sql = String.format(RAW_DELETE, type.getName(), Id);

		PreparedStatement statement = conn.prepareStatement(sql);

		statement.execute();

	}

	public void delete(List<Object> data) throws Exception {

		for (Object ob : data) {
			delete(ob);
		}

	}

}
