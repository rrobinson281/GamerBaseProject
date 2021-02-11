package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


public class GameServices {
	private DatabaseConnectionService dbService = null;
	private Map<String, Integer> GameIdMap = null;
	private Map<String, Integer> PublisherIdMap = null;
	public GameServices(DatabaseConnectionService dbCon) {
		// TODO Auto-generated constructor stub
		this.dbService = dbCon;
		GameIdMap = new HashMap<>();
		PublisherIdMap =new HashMap<>();
		Connection con = dbService.getConnection();
		String queryString = "SELECT Name From Game";
		String queryString2 = "select Name from Publisher";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
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
		try {
			stmt = con.prepareStatement(queryString2);
			ResultSet rs = stmt.executeQuery();
			int index = 0;
			while(rs.next()) {
				String publisherName = rs.getString(rs.findColumn("name"));
				PublisherIdMap.put(publisherName, index);
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReadAllGames() {
		Connection con = dbService.getConnection();
		String queryString = "SELECT Name From Game";
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
	
	public boolean SortGames(String name, String publisher, String genre, String releaseDate) {
		name = name.equals("") ? null : name;
		publisher = publisher.equals("") ? null : publisher;
		genre = genre.equals("") ? null : genre;
		releaseDate = releaseDate.equals("") ? null : releaseDate;
		Connection con = dbService.getConnection();
		String queryString = "select * from  fn_SortGames(?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setString(1,name);
			stmt.setString(2,publisher);
			stmt.setString(3,genre);
			stmt.setString(4,releaseDate);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("name"));
				String desc = rs.getString(rs.findColumn("Description"));
				String gamePublisher = rs.getString(rs.findColumn("Publisher"));
				String gameGenre = rs.getString(rs.findColumn("Genre"));
				String gameDate = rs.getString(rs.findColumn("ReleaseDate"));
				System.out.println("Game: "+gameName);
				System.out.println("Description : "+desc);
				System.out.println("Publisher: "+gamePublisher);
				System.out.println("Genre: "+gameGenre);
				System.out.println("ReleaseDate: "+gameDate+"\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean CreateGame(String name, String description, String genre, String releaseDate, String Publisher) {
		Integer pubID = Publisher.equals("") ? PublisherIdMap.get("null") :PublisherIdMap.get(Publisher);
		description = description.equals("") ? null : description;
		genre = genre.equals("") ? null : genre;
		releaseDate = releaseDate.equals("") ? null : releaseDate;
		Connection con = dbService.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call CreateGame(?,?,?,?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, name);
			cs.setString(3, description);
			cs.setString(4, genre);
			cs.setString(5, releaseDate);
			cs.setInt(6, pubID);
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
			GameIdMap.put(name, GameIdMap.size());
			System.out.println("Game added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public Map<String, Integer> getGameMap(){
		return GameIdMap;
	}
}
