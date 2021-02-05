package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Ratings {
	private String username;
	private DatabaseConnectionService dbService = null;
	private Map<String, Integer> GameIdMap = null;
	
	public Ratings(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
		this.GameIdMap = new HashMap<>();
		String queryString = "SELECT Name From Game";
		String queryString2 = "select Name from Publisher";
		PreparedStatement stmt;
		try {
			stmt = this.dbService.getConnection().prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			int index = 0;
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("name"));
				GameIdMap.put(gameName, index);
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createRating(String gameName, String review, float rating) {
		review = review.equals("") ? null : review;
		int gameID = GameIdMap.get(gameName);
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
	
	public void deleteRating(String gameName) {
		int gameID = GameIdMap.get(gameName);
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
	
	public ArrayList<String> updateRating(String gameName, String review, float rating) {
		ArrayList<String> friends = new ArrayList<String>();
		int gameID = GameIdMap.get(gameName);
		CallableStatement updateRating = null;
		try {
			updateRating = dbService.getConnection().prepareCall("{call UpdateRating(?, ?, ?, ?)}");
			updateRating.setInt(1, gameID);
			updateRating.setString(2, this.username);
			updateRating.setString(3, review);
			updateRating.setFloat(4, rating);
			updateRating.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	public void readRatings() {
		
	}
}
