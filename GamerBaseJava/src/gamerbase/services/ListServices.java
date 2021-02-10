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
		String queryString = "SELECT * From fn_GetGameLists()";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				int listID = rs.getInt(rs.findColumn("ListID"));
				ListIdMap.put(listName,listID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 queryString = "SELECT * From fn_GetConsoleLists()";
			try {
				stmt = con.prepareStatement(queryString);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					String listName = rs.getString(rs.findColumn("ListName"));
					int listID = rs.getInt(rs.findColumn("ListID"));
					ListIdMap.put(listName,listID);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
			System.out.println(this.user);
			while(rs.next()) {
				String listName = rs.getString(rs.findColumn("ListName"));
				System.out.println(listName);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createGameList(String listName) {
		if(listName.contentEquals("")) return false;
		Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call CreateList(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, listName);
			cs.setString(3, this.user);
			cs.execute();
			ListIdMap.put(listName, ListIdMap.size());
			System.out.println("List added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
