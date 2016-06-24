JDapper-Lite
=======

Simple Dapper implemented in Java


Example usage:
--------

Download .jar release [link](https://github.com/robertluiz/JDapper/tree/master/releases/)

Inicialize:

```java

Connection conn = DriverManager.getConnection(url + dbName, userName, password);
JDapper jd = new JDapper(conn);

```

Class:

```java
public class Stuff {
	
	private int Id = 0;
	private String string1 = "";
	private  String anotherString = "";
	private  int number1 = 0;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getAnotherString() {
		return anotherString;
	}
	public void setAnotherString(String anotherString) {
		this.anotherString = anotherString;
	}
	public int getNumber1() {
		return number1;
	}
	public void setNumber1(int number1) {
		this.number1 = number1;
	}
}

```

Go:
```java
Stuff s = new Stuff();
    s.setNumber1(78);
		s.setString1("OK");
		s.setAnotherString("Good");
		jd.insert(s);
		
```


