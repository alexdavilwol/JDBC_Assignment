package com.alex;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCAssignmentTest {

	public static void main(String[] args) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcassignment", "root",
				"davwolDW1!");
		Statement st = con.createStatement();

//		st.execute("create table jdbc_employee(no numeric(10),firstname varchar(50),lastname varchar(50), email varchar(50), address varchar(100),primary key(no))");
//		st.execute("insert into jdbc_employee values(101,'fatmana','Citisili','fatmana@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(102,'Alex','Davila','alex@hcl.com', 'Cary')");
//		st.execute("insert into jdbc_employee values(103,'David','Clark','David@gmail.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(104,'Michael','bobby','michael@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(105,'Helen','jensen','helen@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(106,'Sharon','Crystal','sharen@gmail.com', 'Cary')");
//		st.execute("insert into jdbc_employee values(107,'Christina','Aguilera','christina@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(108,'Yeetma','Bawls','yeetma@hotmail.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(109,'Tony','Tiger','tony@hcl.com', 'Texas')");
//		st.execute("insert into jdbc_employee values(110,'Lauren','Cristobal','lauren@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(111,'Jen','Jensen','jen@hotmail.com', 'Florida')");
//		st.execute("insert into jdbc_employee values(112,'Man','boy','man@hcl.com', 'Austin')");
//		st.execute("insert into jdbc_employee values(113,'Woman','girl','women@comcast.net', 'Austin')");
//		st.execute("insert into jdbc_employee values(114,'Bob','Smith','bob@hcl.com', 'India')");
//		st.execute("insert into jdbc_employee values(115,'Jones','Smith','jones@hcl.com', 'Austin')");

		Scanner in = new Scanner(System.in);
		System.out.println("What would you like to do? (insert, update, delete)");
		String command = in.next();

		PreparedStatement insert = con.prepareStatement("insert into jdbc_employee values(?,?,?,?,?)");
		String sqlInsert = "insert into jdbc_employee " + " (no, firstname, lastname, email, address)"
				+ " values (?,?,?,?,?)";
		insert = con.prepareStatement(sqlInsert);

		PreparedStatement update = con.prepareStatement("update jdbc_employee set ? = ? where no = ?;");
		// String sqlUpdate = "update jdbc_employee set "+""
		//CallableStatement update = con.prepareCall("{call update_call(?,?,?)}");

		PreparedStatement delete = con.prepareStatement("delete from jdbc_employee where no=?");

		switch (command) {
		case "insert":
			System.out.println("Enter your employee ID");
			Integer id = in.nextInt();

			System.out.println("Enter your first name");
			String firstname = in.next();

			System.out.println("Enter your last name");
			String lastname = in.next();

			System.out.println("Enter your email Id");
			String email = in.next();

			System.out.println("Enter your address");
			String address = in.next();

			insert.setInt(1, id);
			insert.setString(2, firstname);
			insert.setString(3, lastname);
			insert.setString(4, email);
			insert.setString(5, address);
			insert.executeUpdate();

			System.out.println("Employee inserted.");
			break;

		case "update":
			System.out.println("Which employee ID would you like to update?");
			Integer updateId = in.nextInt();

			System.out.println("Which column would you like to update?");
			String updateColumn = in.next();

			System.out.println("What information did you want to update with?");
			String updateValue = in.next();
			
			String sqlUpdate = "update jdbc_employee set "+ updateColumn +" = "+"'"+ updateValue +"'" + " where no = "+updateId;

			update.execute(sqlUpdate);
			
			System.out.println("Employee "+updateId+" was updated with the mysql statement--> "+ sqlUpdate);
			break;

		case "delete":
			System.out.println("Which employee ID would you like to delete?");
			Integer deleteId = in.nextInt();

			delete.setInt(1, deleteId);
			delete.executeUpdate();
			
			System.out.println("Employee "+deleteId+" deleted");
			break;
		default:
			System.out.println("Unrecognizable command. Please choose either insert, delete, or update");

		}
		
		System.out.println("-----------------------------------------------------");
		
		System.out.println("Who would you like to look up? Enter Either their firstname or lastname");
		String name = in.next();
		
		PreparedStatement fnameLookup = con.prepareStatement("select * from jdbc_employee where firstname = ?;");
		PreparedStatement lnameLookup = con.prepareStatement("select * from jdbc_employee where lastname = ?;");
		
		fnameLookup.setString(1, name);
		lnameLookup.setString(1, name);
		
		ResultSet fnameFound = fnameLookup.executeQuery();
		ResultSet lnameFound = lnameLookup.executeQuery();
		
		ResultSetMetaData rsmdFirst = fnameFound.getMetaData();
		ResultSetMetaData rsmdLast = lnameFound.getMetaData();
		
		
		if(fnameLookup != null) {
			
			int columnCount =rsmdFirst.getColumnCount();
			
			while(fnameFound.next()) {
				for(int i=1; i<=columnCount; i++) {
					if(i>1)
						System.out.print(", ");
					String columnValue = fnameFound.getString(i);
					System.out.print(rsmdFirst.getColumnName(i)+": "+columnValue);
				}
				System.out.println("");
			}
			
		}
		if(lnameLookup!=null) {
			
			int columnCount =rsmdLast.getColumnCount();
			
			while(lnameFound.next()) {
				for(int i=1; i<=columnCount; i++) {
					if(i>1)
						System.out.print(", ");
					String columnValue = lnameFound.getString(i);
					System.out.print(rsmdLast.getColumnName(i)+": "+columnValue);
				}
				System.out.println("");
			}
			
		}
		

		System.out.println("-----------------------------------------------------");
		
		System.out.println("Who would you like to look up using the callable statement? Enter their employee no");
		int empno = in.nextInt();
		
		CallableStatement cst=con.prepareCall("{call lookup_empno(?)}");
		cst.setInt(1, empno);
		ResultSet callableSearch =cst.executeQuery();
		
		while(callableSearch.next()) {
			System.out.println("no: "+callableSearch.getInt(1)+" firstname: "+
					callableSearch.getString(2)+" lastname: "+callableSearch.getString(3)+
					" email: "+callableSearch.getString(4)+" address: "+callableSearch.getString(5));
		}
		
	}

}
