package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class OwnedListServices {
	private DatabaseConnectionService dbService = null;
	private Map<String, Integer> GameIdMap = null;
	private String user = null;
	public OwnedListServices(DatabaseConnectionService dbService, String user) {
		this.dbService = dbService;
		this.GameIdMap = new HashMap<>();
		this.user = user;
		Connection con = dbService.getConnection();
		String queryString = "SELECT Name, ID From Game";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("name"));
				int id = rs.getInt("ID");
				GameIdMap.put(gameName, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean AddOwnedGame(String gameName) {
		Integer gameID = GameIdMap.get(gameName);
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		Connection con = dbService.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call AddOwnedGame(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, gameID);
			cs.setString(3, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("The game must exist in the system.");
				return false;
			}
			else if(value == 2) {
				System.out.println("The user must be an existing user.");
				return false;
			}
			if(value==0)System.out.println("Game added to owned list sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean RemoveOwnedGame(String gameName) {
		Integer gameID = GameIdMap.get(gameName);
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		Connection con = dbService.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call RemoveOwnedGame(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, gameID);
			cs.setString(3, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("You must own the game to remove it!.");
				return false;
			}
			if(value==0)System.out.println("Game removed from owned list sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void ViewOwnedGames() {
		Connection con = dbService.getConnection();
		String queryString = "SELECT * From fn_viewOwnedGames(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setString(1, this.user);
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
	public void ViewOwnedGames(String username) {
		Connection con = dbService.getConnection();
		String queryString = "SELECT * From fn_viewOwnedGames(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setString(1, username);
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
}
