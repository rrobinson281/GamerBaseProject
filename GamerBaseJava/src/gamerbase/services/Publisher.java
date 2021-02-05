package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class Publisher {
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public Publisher(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
	}
	
	public void createPublisher(String name) {
		CallableStatement newPub = null;
		try {
			newPub = dbService.getConnection().prepareCall("{call CreatePublisher(?)}");
			newPub.setString(1, name);
			newPub.executeUpdate();
			System.out.println("Publisher created successfully");
		} catch (SQLException e) {
			System.out.println("Creation failed");
			e.printStackTrace();
		}
	}
	
	public void updatePublisher(int pubID, String newName) {
		CallableStatement updatePub = null;
		try {
			updatePub = dbService.getConnection().prepareCall("{call UpdatePublisher(?, ?)}");
			updatePub.setInt(1, pubID);
			updatePub.setString(2, newName);
			updatePub.executeUpdate();
			System.out.println("Update Successful");
		}catch (SQLException e) {
			System.out.println("Update failed");
			e.printStackTrace();
		}
	}
}
