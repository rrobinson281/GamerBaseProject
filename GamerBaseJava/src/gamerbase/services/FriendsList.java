package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class FriendsList{
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public FriendsList(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
	}
	
	public ArrayList<String> readFriendsList(){
		ArrayList<String> friends = new ArrayList<String>();
//		String query = "SELECT * FROM fn_ViewFriendsList('"+this.username+"')";
		Connection con = dbService.getConnection();
		String query = "SELECT * from fn_ViewFriendsList(?)";
		PreparedStatement stmt;
		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, this.username);
			ResultSet rs = stmt.executeQuery();
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
			add = dbService.getConnection().prepareCall("{? = call CreateFriendship(?, ?)}");
			add.registerOutParameter(1, Types.INTEGER);
			add.setString(2, this.username);
			add.setString(3, friendName);
			add.executeUpdate();
			int result = add.getInt(1);
			if(result == 1) {
				System.out.println("The friender cannot be null");
				return;
			}
			else if(result == 2) {
				System.out.println("The Person you are trying to friend isn't in the database");
				return;
			}
			else if(result ==3) {
				System.out.println("You two are already friends!");
				return;
			}
			
			System.out.println("Friend added");
		} catch (SQLException e) {
			System.out.println("Friend failed to add");
			e.printStackTrace();
		}
	}
	
	public void removeFriend(String friendName) {
		CallableStatement remove = null;
		try {
			remove = dbService.getConnection().prepareCall("{? = call RemoveFriend(?, ?)}");
			remove.registerOutParameter(1, Types.INTEGER);
			remove.setString(3, this.username);
			remove.setString(2, friendName);
			remove.executeUpdate();
			int result = remove.getInt(1);
			if(result == 1) {
				System.out.println("Must be friends with the user to remove them from your friend list");
				return;
			}
			System.out.println("Friend removed");
		} catch (SQLException e) {
			System.out.println("Friend failed to remove");
			e.printStackTrace();
		}
	}
}
