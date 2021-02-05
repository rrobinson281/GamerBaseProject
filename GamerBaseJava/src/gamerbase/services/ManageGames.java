package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;

public class ManageGames {
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public ManageGames(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
	}
	
	public void createGame(String name, String descript, String genre, Date release, int pubID) {
		CallableStatement addGame = null;
		try {
			addGame = dbService.getConnection().prepareCall("{call CreateGame(?, ?, ?, ?, ?)}");
			addGame.setString(1, name);
			addGame.setString(2, descript);
			addGame.setString(3, genre);
			addGame.setDate(4, release);
			addGame.setInt(5, pubID);
			addGame.executeUpdate();
			System.out.println("Game create successfully");
		} catch (SQLException e) {
			System.out.println("Game failed to create");
			e.printStackTrace();
		}
	}
	
	public void AddOwnedGame(int gameID) {
		CallableStatement addGame = null;
		try {
			addGame = dbService.getConnection().prepareCall("{call AddOwnedGame(?, ?)}");
			addGame.setInt(1, gameID);
			addGame.setString(2, this.username);
			addGame.executeUpdate();
			System.out.println("Game create successfully");
		} catch (SQLException e) {
			System.out.println("Game failed to create");
			e.printStackTrace();
		}
	}
	
	public void AddGameToList(int gameID, int listID) {
		CallableStatement addGame = null;
		try {
			addGame = dbService.getConnection().prepareCall("{call AddGameToList(?, ?, ?)}");
			addGame.setInt(1, listID);
			addGame.setInt(2, gameID);
			addGame.setString(3, this.username);
			addGame.executeUpdate();
			System.out.println("Game added successfully");
		} catch (SQLException e) {
			System.out.println("Game failed to add");
			e.printStackTrace();
		}
	}
	
	public void RemoveOwnedGame(int gameID) {
		CallableStatement removeGame = null;
		try {
			removeGame = dbService.getConnection().prepareCall("{call RemoveOwnedGame(?, ?)}");
			removeGame.setInt(2, gameID);
			removeGame.setString(1, this.username);
			removeGame.executeUpdate();
			System.out.println("Game removed successfully");
		} catch (SQLException e) {
			System.out.println("Game failed to be removed");
			e.printStackTrace();
		}
	}
}
