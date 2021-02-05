import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gamerbase.services.*;

public class DatabaseAttempt {
    private BufferedReader reader;
    private String table;
    private String operation;
    private String command;
    private DatabaseConnectionService connection;
    private static HandleInput handle;

    public DatabaseAttempt(BufferedReader reader) {
        this.reader = reader;
        this.table = "";
        this.operation = "";
        this.command = "";
    }

    public void go() throws IOException{
    	
		System.out.print("Use X or space to exit commands. Press Y to continue: ");
		if(this.reader.readLine().equals("Y")) {
			
			while(true) {
				System.out.println();
				System.out.println("Enter command g for Game Commands");
				System.out.println("Enter command u for User Commands");
				System.out.println("Enter command f for Friend Commands");
				System.out.println("Enter command L for Lists Commands");
				System.out.print("Enter command or press x to exit: ");
				command = this.reader.readLine();
				switch (command) {
				  case "g":
				    System.out.println("Game Commands:");
				    System.out.println("  CreateGame");
				    System.out.println("  AddGameToOwnList");
				    System.out.println("  RemoveGameFromOwnList");
				    System.out.println("  UpdateGame");
				    System.out.print("Enter command or press x to exit: ");
					command = this.reader.readLine();
					if(inputReaderIsX(command)) {break;}
					System.out.println("Entered " + command);
				    
				    break;
				  case "u":
				    System.out.println("user");
				    break;
				  case "x":
				    System.out.println("exit");
				    break;
				}
				if(inputReaderIsX(command)) {
					connection.closeConnection();
					break;
				}
				System.out.println();
	            break;
			}
		}
   
        System.out.print("Bye");
   }

    public boolean inputReaderIsX(String value) {
        if(value.toUpperCase().equals("X") || value.equals("")) {
            return true;
        }
        return false;
    }

    public String getTable() {
        return table;
    }

    public String getOperation() {
        return operation;
    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        System.out.println("hello");
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        DatabaseAttempt test = new DatabaseAttempt(reader);
        DatabaseConnectionService connection = new DatabaseConnectionService("titan.csse.rose-hulman.edu", "GamerBase-S1G8");
        System.out.print("Type NEW if you are a new user otherwise press enter");
        if(connection.connect("fryaj1", "fryana1221")) {
        	if(loginToDatabase(reader, test, connection)) {
        		System.out.println("Login successful");
        	}else {
        		System.out.println("Failed to login");
        		System.exit(0);
        	}
        }else {
        	System.out.println("Did not connect");
        	connection.closeConnection();
        }

        handle = new HandleInput(connection);
        test.go();
        reader.close();

    }

	public static boolean loginToDatabase(BufferedReader reader, DatabaseAttempt test, DatabaseConnectionService connection) {
    	try {
			if(reader.readLine().toLowerCase().equals("new")) {
				//login operations
				System.out.println("Unavailable");
				return false;
			}else {
				System.out.print("Insert Username: ");
				test.command = reader.readLine();
				String username = test.command;
				test.command = "";
				System.out.print("Insert Password: ");
				test.command = reader.readLine();
				String password = test.command;
				if(login(username, password, connection)){//successful login
					return true;
				}else {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }
	
    public static boolean login(String username, String password, DatabaseConnectionService dbCon) {
        String queryString = "SELECT password From [User] where Username = ?";
        try {
            PreparedStatement stmt =  dbCon.getConnection().prepareStatement(queryString);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String stored = rs.getString(rs.findColumn("Password"));
                //System.out.println("Given Password: " + password + " Stored Password: " + stored);
                if(password.equals(stored)) {
                    System.out.println("User " + username + " logged in sucessfully");
                    //user = username;
                    return true;
                }
                else {
                    System.out.println("Couldn't log in");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}