package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class ListServices {
	DatabaseConnectionService dbCon;
	String user;
	private Map<String, Integer> ListIdMap = null;
	public ListServices(DatabaseConnectionService dbCon, String user) {
		this.dbCon = dbCon;
		this.user = user;
		ListIdMap = new HashMap<String,Integer>();
		Connection con = dbCon.getConnection();
		String queryString = "SELECT * From list";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("name"));
				int listID = rs.getInt(rs.findColumn("ID"));
				ListIdMap.put(listName,listID);
			}
			
//			System.out.println(ListIdMap.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		 queryString = "SELECT * From fn_GetConsoleLists()";
//			try {
//				stmt = con.prepareStatement(queryString);
//				ResultSet rs = stmt.executeQuery();
//				while(rs.next()) {
//					String listName = rs.getString(rs.findColumn("ListName"));
//					int listID = rs.getInt(rs.findColumn("ListID"));
//					ListIdMap.put(listName,listID);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
	}
	
	public void readAllGameLists() {
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_GetGameLists()";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				System.out.println(listName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void readUserLists() {
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_UserGameLists(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setString(1, this.user);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				System.out.println(listName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void viewGameList(String listName) {
		int listId = ListIdMap.get(listName);
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_ViewGameLists(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setInt(1, listId);
			ResultSet rs = stmt.executeQuery();
			System.out.println(listName +": ");
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("GameName"));
				System.out.println(gameName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean createList(String listName) {
		if(listName.contentEquals("")) return false;
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call CreateList(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, listName);
			cs.setString(3, this.user);
			cs.execute();
			cs = con.prepareCall("{? = call fn_GetListID(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, listName);
			cs.execute();
			ListIdMap.put(listName, cs.getInt(1));
			System.out.println("List added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean addGameToList(String listName, String gameName, Map<String, Integer> gameMap) {
		int listId = ListIdMap.get(listName);
		int gameId = gameMap.get(gameName);
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call AddGameToList(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, listId);
			cs.setInt(3, gameId);
//			cs.setString(4, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("The listname given wasn't valid");
				return false;
			}
			else if(value == 2) {
				System.out.println("The game given must be in the games list");
				return false;
			}
			else if(value == 3) {
				System.out.println("Cannot add to other people's lists");
				return false;
			}
			System.out.println("Game added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean removeGameFromList(String listName, String gameName, Map<String,Integer> gameMap) {
		int listId = ListIdMap.get(listName);
		int gameId = gameMap.get(gameName);
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call RemoveGameFromList(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, listId);
			cs.setInt(3, gameId);
//			cs.setString(4, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("Game Must appear on the list to remove");
				return false;
			}
			System.out.println("Game removed sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean deleteList(String listName) {
		int listId = ListIdMap.get(listName);
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call DeleteList(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, listId);
			cs.execute();
			ListIdMap.remove(listName);
			System.out.println("List removed sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void readAllConsoleLists() {
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_GetConsoleLists()";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				System.out.println(listName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void readUserConsoleLists() {
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_UserConsoleLists(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setString(1, this.user);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				System.out.println(listName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewConsoleList(String listName) {
		int listId = ListIdMap.get(listName);
		Connection con = this.dbCon.getConnection();
		String queryString = "SELECT * From fn_ViewConsoleLists(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setInt(1, listId);
			ResultSet rs = stmt.executeQuery();
			System.out.println(listName +": ");
			while(rs.next()) {
				String consoleName = rs.getString(rs.findColumn("ConsoleName"));
				System.out.println(consoleName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean addConsoleToList(String listName, String consoleName, Map<String, Integer> consoleMap) {
		int listId = ListIdMap.get(listName);
		System.out.println(consoleMap.toString());
		int consoleId = consoleMap.get(consoleName);
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call AddConsoleToList(?,?)}"); //
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, consoleId);
			cs.setInt(3, listId);
//			cs.setString(4, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("The listname given wasn't valid");
				return false;
			}
			else if(value == 2) {
				System.out.println("The console given must be in the consoles list");
				return false;
			}
			else if(value == 3) {
				System.out.println("Cannot add to other people's lists");
				return false;
			}
			System.out.println("Console added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeConsoleFromList(String listName, String consoleName, Map<String,Integer> consoleMap) {
		int listId = ListIdMap.get(listName);
		int consoleId = consoleMap.get(consoleName);
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call RemoveConsoleFromList(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, listId);
			cs.setInt(3, consoleId);
//			cs.setString(4, this.user);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("Console Must appear on the list to remove");
				return false;
			}
			System.out.println("Console removed sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
