package gamerbase.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserService {
	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	private DatabaseConnectionService dbService = null;

	public UserService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean useApplicationLogins() {
		return true;
	}
	
	public boolean login(String username, String password) {
		String queryString = "SELECT PasswordSalt, PasswordHash From [User] where Username = ?";
		try {
			PreparedStatement stmt =  this.dbService.getConnection().prepareStatement(queryString);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String salt = rs.getString(rs.findColumn("PasswordSalt"));
				String hash = rs.getString(rs.findColumn("PasswordHash"));
				byte[] realSalt = Base64.getDecoder().decode(salt);
				String pass = hashPassword(realSalt,password);
				if(pass.equals(hash)) return true;
				else {
					System.out.println("Login Failed.");
					return false;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean register(String username, String password) {
		byte[] salt = getNewSalt();
		String hash = hashPassword(salt,password);
//		System.out.println("Hashed password: "+hash);
//		System.out.println("Byte array generated "+salt);
//		System.out.println("String given to database " + getStringFromBytes(salt));
		Connection con = dbService.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call RegisterUserSecure(?,?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, username);
			cs.setString(3, getStringFromBytes(salt));
			cs.setString(4, hash);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("Registration Failed.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}

}
