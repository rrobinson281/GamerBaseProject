package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class FriendsList{
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public FriendsList(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
	}
	
	public ArrayList<String> readFriendsList(){
		ArrayList<String> friends = new ArrayList<String>();
		String query = "SELECT * FROM ('"+this.username+"')";
		try (Statement stmt = dbService.getConnection().createStatement()){
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				friends.add(rs.getString("Friendee"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	public void addFriend(String friendName) {
		CallableStatement add = null;
		try {
			add = dbService.getConnection().prepareCall("{call CreateFriendship(?, ?)}");
			add.setString(1, this.username);
			add.setString(2, friendName);
			add.executeUpdate();
			System.out.println("Friend added");
		} catch (SQLException e) {
			System.out.println("Friend failed to add");
			e.printStackTrace();
		}
	}
	
	public void removeFriend(String friendName) {
		CallableStatement remove = null;
		try {
			remove = dbService.getConnection().prepareCall("{call RemoveFriend(?, ?)}");
			remove.setString(2, this.username);
			remove.setString(1, friendName);
			remove.executeUpdate();
			System.out.println("Friend removed");
		} catch (SQLException e) {
			System.out.println("Friend failed to remove");
			e.printStackTrace();
		}
	}
}
