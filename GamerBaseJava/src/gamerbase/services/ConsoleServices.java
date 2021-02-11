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
			System.out.println(ConsoleIdMap.toString());
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
				System.out.println("GameName cannot be null or empty.");
				return false;
			}
			else if(value == 2) {
				System.out.println("Publisher has to be an existing publisher or null.");
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
	public Map<String, Integer> getGameMap(){
		return ConsoleIdMap;
	}
}
