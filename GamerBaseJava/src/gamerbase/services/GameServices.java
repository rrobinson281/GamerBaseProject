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
		String queryString = "SELECT * From Game";
		String queryString2 = "select * from Publisher";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			int index = 0;
			while(rs.next()) {
				String gameName = rs.getString(rs.findColumn("name"));
				int id = rs.getInt(rs.findColumn("ID"));
				GameIdMap.put(gameName, id);
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
				PublisherIdMap.put(publisherName, rs.getInt(rs.findColumn("ID")));
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ReadAllGames() {
		Connection con = dbService.getConnection();
		String queryString = "SELECT * From Game";
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
		Integer pubID = Publisher.equals("") ? null :PublisherIdMap.get(Publisher);
		if(pubID == null) {
			System.out.println("Publisher entered incorrectly or not in game list");
		}
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
			cs = con.prepareCall("{? = call fn_GetGameID(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, name);
			cs.execute();
			GameIdMap.put(name, cs.getInt(1));
//			GameIdMap.put(name, GameIdMap.size());
			System.out.println("Game added sucessfully!");
			System.out.println();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean EditGame(String gameName, String newName, String newDesc, String newGenre, String newRelease, String newPub) {
		Integer gameID = GameIdMap.get(gameName);
		Integer pubID = newPub.equals("") ? PublisherIdMap.get("null") :PublisherIdMap.get(newPub);
		newName = newName.equals("") ? null : newName;
		newDesc = newDesc.equals("") ? null : newDesc;
		newGenre = newGenre.equals("") ? null : newGenre;
		newRelease = newRelease.equals("") ? null : newRelease;
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		if(pubID == null) {
			System.out.println("Publisher entered incorrectly or not in game list");
		}
		CallableStatement stmt;
		try {
			stmt = dbService.getConnection().prepareCall("{? = call UpdateGame(?, ?, ?, ?, ?, ?)}");
			stmt.setInt(2, gameID);
			stmt.setString(3, newName);
			stmt.setString(4, newDesc);
			stmt.setString(5, newGenre);
			stmt.setString(6, newRelease);
			stmt.setInt(7, pubID);
			stmt.executeUpdate();
			int value = stmt.getInt(1);
			if(value == 1) {
				System.out.println("GameName cannot be empty.");
				return false;
			}
			
			if(newName != null) {
				GameIdMap.remove(gameName);
				GameIdMap.put(newName, gameID);
			}
			System.out.println("Game updated sucsessfully");
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public Map<String, Integer> getGameMap(){
		return GameIdMap;
	}
	public boolean createPlayedOn(String gameName, String consoleName, Map<String,Integer> consoleMap) {
		Integer gameID = GameIdMap.get(gameName);
		Integer consoleID = consoleMap.get(consoleName);
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		if(consoleID == null) {
			System.out.println("Console Name entered incorrectly or not in console list");
			return false;
		}
		CallableStatement stmt;
		try {
			stmt = dbService.getConnection().prepareCall("{? = call CreatePlayedOn(?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, gameID);
			stmt.setInt(3, consoleID);
			stmt.executeUpdate();
			int value = stmt.getInt(1);
			if(value == 1) {
				System.out.println("GameID cannot be empty.");
				return false;
			}
			if(value == 2) {
				System.out.println("ConsoleID cannot be empty.");
				return false;
			}
			if(value == 3) {
				System.out.println("You can already play " + gameName + " on " + consoleName);
			}
			System.out.println("Played on connection created!");
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean removeGamePlayedOn(String gameName, String consoleName, Map<String,Integer> consoleMap) {
		Integer gameID = GameIdMap.get(gameName);
		Integer consoleID = consoleMap.get(consoleName);
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		if(consoleID == null) {
			System.out.println("Console Name entered incorrectly or not in console list");
			return false;
		}
		CallableStatement stmt;
		try {
			stmt = dbService.getConnection().prepareCall("{? = call RemovePlayedOn(?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, gameID);
			stmt.setInt(3, consoleID);
			stmt.executeUpdate();
			int value = stmt.getInt(1);
			if(value == 1) {
				System.out.println("GameID cannot be empty.");
				return false;
			}
			if(value == 2) {
				System.out.println("ConsoleID cannot be empty.");
				return false;
			}
			if(value == 3) {
				System.out.println("Cannot remove game from a console it can't be played on!");
				return false;
			}
			System.out.println("Played on removed!");
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean gamePlayedOn(String gameName) {
		Integer gameID = GameIdMap.get(gameName);
		if(gameID == null) {
			System.out.println("Game Name entered incorrectly or not in game list");
			return false;
		}
		Connection con = dbService.getConnection();
		String queryString = "select * from  fn_PlayableOnConsole(?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setInt(1,gameID);
			ResultSet rs = stmt.executeQuery();
			System.out.println(gameName + " is playable on: ");
			while(rs.next()) {
				String consoleName = rs.getString(rs.findColumn("Console"));
				System.out.println(consoleName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
