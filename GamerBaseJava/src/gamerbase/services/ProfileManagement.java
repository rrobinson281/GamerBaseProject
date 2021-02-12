package gamerbase.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProfileManagement {
	private String username;
	private DatabaseConnectionService dbService = null;
	
	public ProfileManagement(String name, DatabaseConnectionService dbService) {
		this.username = name;
		this.dbService = dbService;
		System.out.println("Profile selected");
	}
	
	public void viewProfile() {
		ArrayList<String> profile = new ArrayList<String>();
		String query = "SELECT * FROM fn_UserProfiles('"+this.username+"')";
		try (Statement stmt = dbService.getConnection().createStatement()){
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {//loops in case of multiply profiles owned by user
				profile.add(rs.getString("OwnerUsername"));
				profile.add(rs.getString("ProfileType"));
				profile.add(rs.getString("ProfileName"));
			}
		} catch (SQLException e) {
			System.out.println("Profile fetch failed");
			//e.printStackTrace();
		}
		System.out.println("You own "+profile.size()/3+" profile(s).");//prints number of profiles owned
		for(int i = 0; i < profile.size(); i+=3) {//loops when user has multiple profiles
			System.out.println("Username:      "+profile.get(2 + i));
			System.out.println("ProfileType:   "+profile.get(1 + i));
//			System.out.println("OwnerUsername: "+);
		}
	}
	
	public void viewUsers() {
		Connection con = dbService.getConnection();
		String queryString = "SELECT * From [User]";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(queryString);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getString(rs.findColumn("Username")));
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewFriendProfile(String friendUsername) {
		ArrayList<String> profile = new ArrayList<String>();
//		String query = "SELECT * FROM fn_UserProfiles('"+friendUsername+"')";
		Connection con = dbService.getConnection();
		String query = "SELECT * FROM fn_UserProfiles(?)";
		PreparedStatement stmt;
		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, friendUsername);
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {//loops in case of multiply profiles owned by user
				profile.add(rs.getString("OwnerUsername"));
				profile.add(rs.getString("ProfileType"));
				profile.add(rs.getString("ProfileName"));
			}
		} catch (SQLException e) {
			System.out.println("Profile fetch failed");
			//e.printStackTrace();
		}
		System.out.println(profile.size()/3+" profile(s).");//prints number of profiles owned
		for(int i = 0; i < profile.size(); i+=3) {//loops when user has multiple profiles
			System.out.println("Username:      "+profile.get(2 + i));
			System.out.println("ProfileType:   "+profile.get(1 + i));
//			System.out.println("OwnerUsername: "+);
		}
	}
	public void addProfile(String profileType, String profileName) {
		CallableStatement add = null;
		try {
			add = dbService.getConnection().prepareCall("{call CreateProfile(?, ?, ?)}");
			add.setString(1, profileType);
			add.setString(2, profileName);
			add.setString(3, this.username);
			int result = add.executeUpdate();
			if(result == 1){
				System.out.print("Profile Added successfully");
				return;
			}else {
				System.out.print("Profile failed to add");
				return;
			}
		} catch (SQLException e) {
			System.out.print("Profile failed to add");
		}
	}
	
	public void deleteProfile(String profileName, String profileType) {
		CallableStatement remove = null;
		try {
			remove = dbService.getConnection().prepareCall("{call deleteProfile(?, ?)}");
			remove.setString(1, profileName);
			remove.setString(2, profileType);
			int result = remove.executeUpdate();
			if(result == 1){
				System.out.print("Profile Removed Successfully");
				return;
			}else {
				System.out.print("Profile failed to remove");
				return;
			}
		} catch (SQLException e) {
			System.out.print("Profile failed to remove");
			e.printStackTrace();
		}
	}
	
	public void editProfile(String profileName, String profileType, String newType, String newName) {
		CallableStatement edit = null;
		try {
			edit = dbService.getConnection().prepareCall("{call EditProfile(?, ?, ?)}");
			edit.setInt(1, findID(profileName, profileType));
			if(newType.equals("")) newType = null;
			if(newName.equals("")) newName = null;
			edit.setString(2, newType);
			edit.setString(3, newName);
			int result = edit.executeUpdate();
			if(result == 1){
				System.out.print("Profile Edit Successful");
				return;
			}
		} catch (SQLException e) {
			System.out.print("Profile Failed to Update");
			e.printStackTrace();
		}
	}
	
	public int findID(String profileName, String profileType) {
		int id = 0;
		Connection con = dbService.getConnection();
		String query = "SELECT * FROM fn_FetchProfileID(?,?,?)";
		PreparedStatement stmt;
		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, this.username);
			stmt.setString(2, profileType);
			stmt.setString(2, profileName);
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			System.out.println("Profile fetch failed");
			//e.printStackTrace();
		}
		return id;
	}
}
