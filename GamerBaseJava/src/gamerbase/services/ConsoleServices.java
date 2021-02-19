package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class ConsoleServices {
	private DatabaseConnectionService dbService = null;
	private Map<String, Integer> ConsoleIdMap = null;
	public ConsoleServices(DatabaseConnectionService dbCon) {
		// TODO Auto-generated constructor stub
		this.dbService = dbCon;
		ConsoleIdMap = new HashMap<>();
		Connection con = dbService.getConnection();
		String queryString = "SELECT Name From Console";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String consoleName = rs.getString(rs.findColumn("name"));
				CallableStatement cs = con.prepareCall("{? = call fn_GetConsoleID(?)}");
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, consoleName);
				cs.execute();
				ConsoleIdMap.put(consoleName, cs.getInt(1));
			}
//			System.out.println(ConsoleIdMap.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReadAllConsoles() {
		Connection con = dbService.getConnection();
		String queryString = "SELECT Name From Console";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("name"));
				System.out.println(gameName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean CreateConsole(String consoleName) {
		Connection con = dbService.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call CreateConsole(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, consoleName);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("ConsoleName cannot be null or empty.");
				return false;
			}
			if(value == 2) {
				System.out.println("That console already exists!");
				return false;
			}
			cs = con.prepareCall("{? = call fn_GetConsoleID(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, consoleName);
			cs.execute();
			ConsoleIdMap.put(consoleName, cs.getInt(1));
			
			System.out.println("Console added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public Map<String, Integer> getConsoleMap(){
		return ConsoleIdMap;
	}
	public boolean UpdateConsole(String consoleName, String newName) {
		Integer consoleID = ConsoleIdMap.get(consoleName);
		newName = newName.equals("")?null:newName;
		if(consoleID == null) {
			System.out.println("Console Name entered incorrectly or not in console list");
			return false;
		}
		CallableStatement stmt;
		try {
			stmt = dbService.getConnection().prepareCall("{? = call UpdateConsole(?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, consoleID);
			stmt.setString(3, newName);
			stmt.executeUpdate();
			int value = stmt.getInt(1);
			if(value == 1) {
				System.out.println("Console Name cannot be empty.");
				return false;
			}
			if(value == 2) {
				System.out.println("Console Name already taken.");
				return false;
			}
			if(newName != null) {
				ConsoleIdMap.remove(consoleName);
				ConsoleIdMap.put(newName, consoleID);
			}
			System.out.println("Updated console!");
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean RemoveConsole(String consoleName) {
		Integer consoleID = ConsoleIdMap.get(consoleName);
		if(consoleID == null) {
			System.out.println("Console Name entered incorrectly or not in console list");
			return false;
		}
		CallableStatement stmt;
		try {
			stmt = dbService.getConnection().prepareCall("{? = call UpdateConsole(?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, consoleID);
			stmt.executeUpdate();
			int value = stmt.getInt(1);
			if(value == 1) {
				System.out.println("Console Name cannot be empty.");
				return false;
			}
			System.out.println("Updated console!");
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean ViewGamesPlayable(String consoleName) {
		Integer consoleID = ConsoleIdMap.get(consoleName);
		String queryString = "SELECT * From fn_AvailableGamesOnConsole(?)";
		PreparedStatement stmt;
		try {
			stmt = dbService.getConnection().prepareStatement(queryString);
			stmt.setInt(1, consoleID);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Games available on the " + consoleName + ": " );
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("Name"));
				System.out.println(gameName);
			}
			System.out.println();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
