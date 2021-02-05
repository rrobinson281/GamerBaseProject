package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Ratings {
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public Ratings(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
	}
	
	public void createRating(int gameID, String review, float rating) {
		CallableStatement newRating = null;
		try {
			newRating = dbService.getConnection().prepareCall("{call CreateRating(?, ?, ?, ?)}");
			newRating.setInt(1, gameID);
			newRating.setString(2, this.username);
			newRating.setString(3, review);
			newRating.setFloat(4, rating);
			newRating.executeUpdate();
			System.out.println("Rating created successfully");
		} catch (SQLException e) {
			System.out.println("Creation failed");
			e.printStackTrace();
		}
	}
	
	public void deleteRating(int gameID) {
		CallableStatement deleteRating = null;
		try {
			deleteRating = dbService.getConnection().prepareCall("{call DeleteRating(?, ?)}");
			deleteRating.setInt(1, gameID);
			deleteRating.setString(2, this.username);
			deleteRating.executeUpdate();
			System.out.println("Deletion successful");
		}catch (SQLException e) {
			System.out.println("Deletion failed");
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> updateRating(int gameID, String review, float rating) {
		ArrayList<String> friends = new ArrayList<String>();
		String query = "SELECT * From "+username;
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
	
	public void readRatings() {
		
	}
}
