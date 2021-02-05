package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import gamerbase.services.DatabaseConnectionService;

public class HandleInput {
	private DatabaseConnectionService dbService = null;
	
	public HandleInput(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public void handle(String tableName, String command) {
		if(command.compareTo("READ") == 0) {
			System.out.println(read(tableName));
		}else if(command.compareTo("CREATE") == 0) {
			create(tableName);
		}else if(command.compareTo("DELETE") == 0) {
			delete(tableName);
		}else if(command.compareTo("UPDATE") == 0) {
			update(tableName);
		}else {
			System.out.print("Command Not Recognized");
		}
	}
	
	public ArrayList<String> read(String tableName) {
		ArrayList<String> list = new ArrayList<String>();
		String query = "SELECT * From "+tableName;
		try (Statement stmt = dbService.getConnection().createStatement()){
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				System.out.println(rs.getString("Name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void create(String tableName) {
		if(tableName.compareTo("Game") == 0) {
			Date d = new Date(2011, 11, 18);
			createGame("Minecraft", "Mine time", "Adventure", d, 4);
		}else if(tableName.compareTo("Rates") == 0) {
			createRating(0, "Mohan", "Terrible", 1);
		}
	}
	
	private void createRating(int gameID, String user, String review, float rating) {
		CallableStatement newRating = null;
		try {
			newRating = dbService.getConnection().prepareCall("{call CreateRating(?, ?, ?, ?)}");
			newRating.setInt(1, gameID);
			newRating.setString(2, user);
			newRating.setString(3, review);
			newRating.setFloat(4, rating);
			newRating.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createGame(String name, String descript, String genre, Date release, int pubID) {
		CallableStatement addGame = null;
		try {
			addGame = dbService.getConnection().prepareCall("{call CreateGame(?, ?, ?, ?, ?)}");
			addGame.setString(1, name);
			addGame.setString(2, descript);
			addGame.setString(3, genre);
			addGame.setDate(4, release);
			addGame.setInt(5, pubID);
			addGame.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String tableName) {
		if(tableName.compareTo("Rates") == 0) {
			CallableStatement deleteRating = null;
			try {
				deleteRating = dbService.getConnection().prepareCall("{call DeleteRating(?, ?)}");
				deleteRating.setInt(1, 0);
				deleteRating.setString(2, "Mohan");
				deleteRating.executeUpdate();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update(String tableName) {
		if(tableName.compareTo("Rates") == 0) {
			CallableStatement updateRating = null;
			try {
				updateRating = dbService.getConnection().prepareCall("{call UpdateRating(?, ?, ?, ?)}");
				updateRating.setInt(1, 0);
				updateRating.setString(2, "Mohan");
				updateRating.setString(3, "Good");
				updateRating.setFloat(4, 3);
				updateRating.executeUpdate();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	class FriendsList{
		String username;
		public FriendsList(String name) {
			this.username = name;
		}
		
		public ArrayList<String> readFriendsList(String username){
			ArrayList<String> friends = new ArrayList<String>();
			String query = "SELECT * From "+username;
			try (Statement stmt = dbService.getConnection().createStatement()){
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					friends.add(rs.getString("Name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return friends;
		}
		
		public void addFriend(String friendName) {
			String query = "EXEC CreateFriendship("+username+" ,"+ friendName+")";
			try (Statement stmt = dbService.getConnection().createStatement()){
				stmt.executeQuery(query);
				System.out.println("Friend added");
			} catch (SQLException e) {
				System.out.println("Failed to add");
				e.printStackTrace();		
			}
		}
		
		public void removeFriend(String friendName) {
			String query = "EXEC removeFriend(" + friendName+ " ,"+this.username+")";
			try (Statement stmt = dbService.getConnection().createStatement()){
				stmt.executeQuery(query);
				System.out.println("Friend removed");
			} catch (SQLException e) {
				System.out.println("Failed to remove");
				e.printStackTrace();		
			}
		}
	}
}
