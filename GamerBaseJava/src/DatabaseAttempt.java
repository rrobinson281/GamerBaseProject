import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import gamerbase.services.*;

public class DatabaseAttempt {
    private BufferedReader reader;
    private String table;
    private String operation;
    private String command;
    private DatabaseConnectionService connection;
    private static HandleInput handle;
    private static String user = "";

    public DatabaseAttempt(BufferedReader reader) {
        this.reader = reader;
        this.table = "";
        this.operation = "";
        this.command = "";
    }

    public void go(DatabaseConnectionService connection) throws IOException{
    	
		System.out.print("Use X or space to exit commands. Press Y to continue: ");
		if(this.reader.readLine().equals("Y")) {
			ListServices listHandler = new ListServices(connection, user);
			GameServices gameHandler = new GameServices(connection);
			ConsoleServices consoleHandler = new ConsoleServices(connection);
			OwnedListServices ownedListHandler = new OwnedListServices(connection, user);
			FriendsList fl = new FriendsList(user, connection);
			Ratings ratingHandler = new Ratings(user, connection);
			while(true) {
				String gameName ="";
				String consoleName = "";
				String gameDesc="";
				String gameGenre="";
				String gameRelease="";
				String gamePub="";
				System.out.println();
				System.out.println("Enter command g for Game Commands");
				System.out.println("Enter command r for Game Commands");
				System.out.println("Enter command u for User Commands");
				System.out.println("Enter command f for Friend Commands");
				System.out.println("Enter command l for Lists Commands");
				System.out.print("Enter command or press x to exit: ");
				command = this.reader.readLine();
				switch (command) {
				  case "g":
					System.out.println("This the the current list of games");
					gameHandler.ReadAllGames();
				    System.out.println("Game Commands:");
				    System.out.println("  CreateGame -Add a game to the system");
				    System.out.println("  ViewMyGames -View games that you own");
				    System.out.println("  AddGameToOwnList -Add a game to the list of games you own");
				    System.out.println("  RemoveGameFromOwnList -Remove a game from your owned list");
				    System.out.println("  UpdateGame -Update a game");
				    System.out.println("  SortGames -Sort through games in the system");
				    System.out.print("Enter command or press x to exit: ");
					command = this.reader.readLine();
					if(inputReaderIsX(command)) {break;}
					switch(command.toLowerCase()) {
						case "creategame":
							System.out.print("Enter the name of the game: ");
							gameName = this.reader.readLine();
							System.out.print("Enter a description of the game(this can be left empty): ");
							gameDesc = this.reader.readLine();
							System.out.print("Enter the genre of the game(this can be left empty): ");
							gameGenre = this.reader.readLine();
							System.out.print("Enter the release date of the game(this can be left empty): ");
							gameRelease = this.reader.readLine();
							System.out.print("Enter the publisher of the game(this can be left empty): ");
							gamePub = this.reader.readLine();
							gameHandler.CreateGame(gameName, gameDesc, gameGenre, gameRelease, gamePub);
							break;
						case "viewmygames":
							System.out.println("You own these games: ");
							ownedListHandler.ViewOwnedGames();
							break;
						case "addgametoownlist":
							System.out.print("Enter the name of the game you want to add to your list: ");
							gameName = this.reader.readLine();
							ownedListHandler.AddOwnedGame(gameName);
							break;
						case "removegamefromownlist":
							System.out.print("Enter the name of the game you want to add to your list: ");
							gameName = this.reader.readLine();
							ownedListHandler.RemoveOwnedGame(gameName);
							break;
						case "updategame":
							System.out.println("not yet implemented");
							break;
						case "sortgames":
							System.out.print("Enter the name of the game(this can be left empty): ");
							gameName = this.reader.readLine();
							System.out.print("Enter the release date of the game(this can be left empty): ");
							gameRelease = this.reader.readLine();
							System.out.print("Enter the genre of the game(this can be left empty): ");
							gameGenre = this.reader.readLine();
							System.out.print("Enter the Publisher of the game(this can be left empty): ");
							gamePub = this.reader.readLine();
							gameHandler.SortGames(gameName, gamePub, gameGenre, gameRelease);
							break;
					}
				  break;
//					System.out.println("Entered " + command);
				  
				  case "r":
					  System.out.println("Ratings Commands: ");
					  System.out.println("   (C) Create Rating");
					  System.out.println("   (R) Remove Rating");
					  System.out.println("   (U) Update Rating");
					  String r = this.reader.readLine();
					  if(r.equals("C")){
						  System.out.println("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  System.out.println("Please Enter Review: \n");
						  String review = this.reader.readLine();
						  System.out.println("Please Enter Rating: ");
						  String rating = this.reader.readLine();
						  float rate = Float.parseFloat(rating);
						  ratingHandler.createRating(gameName, review, rate);
					  }else if(r.equals("R")) {
						  System.out.println("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  ratingHandler.deleteRating(gameName);
					  }else if(r.equals("U")) {
						  System.out.println("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  System.out.println("Please Enter Review \n");
						  String review = this.reader.readLine();
						  System.out.println("Please Enter Rating: ");
						  String rating = this.reader.readLine();
						  float rate = Float.parseFloat(rating);
						  ratingHandler.updateRating(gameName, review, rate);
						  break;
					  }else {
						  System.out.println("Invalid");
					  }
					  break;
					  
				  case "f":		  
					  
					  System.out.println("Friends List Commands: ");
					  System.out.println("  (V) View Friends");
					  System.out.println("  (A) Add Friend");
					  System.out.println("  (R) Remove Friend");
					  System.out.print("  (B) Back \n Command: ");
					  command = this.reader.readLine();
					  if(command.equals("V")) {
						  ArrayList<String> friends = fl.readFriendsList();
						  System.out.println("Friends: ");
						  for(int i = 0; i < friends.size(); i++) System.out.println(friends.get(i));
					  }else if(command.equals("A")){
						  System.out.println("Please Enter Friend Name: ");
						  String friendName = this.reader.readLine();
						  fl.addFriend(friendName);
					  }else if(command.equals("R")) {
						  System.out.println("Please Enter Friend Name: ");
						  String name = this.reader.readLine();
						  fl.removeFriend(name);
					  }else if(command.equals("B")) {
						  break;
					  }else {
						  System.out.println("Invalid");
					  }
					  break;
				  case "l":
					  System.out.println("List Commands");
					  System.out.println("  (GL) View Game Commands");
					  System.out.println("  (CL) View Console Commands");
					  command = this.reader.readLine().toLowerCase();
					  if(inputReaderIsX(command)) {break;}
					  switch(command) {
					  	  case "gl":
					  		  System.out.println("These are the Current Game lists: ");
					  		  listHandler.readAllGameLists();
					  		  System.out.println("Game List Commands");
							  System.out.println("  (ViewSpecificList) View A specific Game");
							  System.out.println("  (ViewMyGameLists) View Your Game Lists");
							  System.out.println("  (CreateGameList) Create a Game List");
							  System.out.println("  (AddGameToList) Add a Game to a List");
//							  System.out.println("  (EditGameList) Edit a Game on a List");
							  System.out.println("  (RemoveGameList) Remove a Game from a List");
							  System.out.println("  (DeleteGameList) Delete a Game List");
							  command = this.reader.readLine();
							  String listName = "";
							  if(inputReaderIsX(command)) {break;}
							  switch(command.toLowerCase()) {
							  	case "viewallgamelists":
							  		
							  		break;
							  	case "viewmygamelists":
							  		listHandler.readUserLists();
							  		break;
							  	case "viewspecificlist":
							  		System.out.println("Enter the name of the list you want to view: ");
							  		listName = this.reader.readLine();
							  		listHandler.viewGameList(listName);
							  		break;
							  	case "creategamelist":
							  		System.out.print("Enter the name of the game list: ");
									listName = this.reader.readLine();
							  		listHandler.createList(listName);
							  		gameHandler.ReadAllGames();
							  		System.out.println("Add your first game!: ");
							  		gameName = this.reader.readLine();
							  		listHandler.addGameToList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "addgametolist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.println("Enter the Name of the list you want to add a game to?");
							  		listName = this.reader.readLine();
							  		gameHandler.ReadAllGames();
							  		System.out.println("Enter the name of the Game you want to add");
							  		gameName = this.reader.readLine();
							  		listHandler.addGameToList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "editgamelist":
							  		break;
							  	case "removegamelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.println("Enter the Name of the list you want to remove a game from: ");
							  		listName = this.reader.readLine();
							  		gameHandler.ReadAllGames();
							  		System.out.println("Enter the name of the Game you want to remove");
							  		gameName = this.reader.readLine();
							  		listHandler.removeGameFromList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "deletegamelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.println("Enter the Name of the list you want to Delete: ");
							  		listName = this.reader.readLine();
							  		listHandler.deleteList(listName);
							  		break;
							  }
					  		  break;
					  	  case "cl":
					  		System.out.println("These are the Current Console lists: ");
					  		  listHandler.readAllConsoleLists();
					  		  System.out.println("Console List Commands");
							  System.out.println("  (ViewSpecificList) View A specific Console");
							  System.out.println("  (ViewMyConsoleLists) View Your Console Lists");
							  System.out.println("  (CreateConsoleList) Create a Console List");
							  System.out.println("  (AddConsoleToList) Add a Console to a List");
//							  System.out.println("  (EditConsoleList) Edit a Console on a List");
							  System.out.println("  (RemoveConsoleList) Remove a Console from a List");
							  System.out.println("  (DeleteConsoleList) Delete a Console List");
							  command = this.reader.readLine();
							  listName = "";
							  if(inputReaderIsX(command)) {break;}
							  switch(command.toLowerCase()) {
							  	case "viewallconsolelists":
							  		
							  		break;
							  	case "viewmyconsolelists":
							  		listHandler.readUserConsoleLists();
							  		break;
							  	case "viewspecificlist":
							  		System.out.println("Enter the name of the list you want to view: ");
							  		listName = this.reader.readLine();
							  		listHandler.viewConsoleList(listName);
							  		break;
							  	case "createconsolelist":
							  		System.out.print("Enter the name of the console list: ");
									listName = this.reader.readLine();
							  		listHandler.createList(listName);
							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Add your first console!: ");
							  		consoleName = this.reader.readLine();
							  		listHandler.addConsoleToList(listName, consoleName, consoleHandler.getGameMap());
							  		break;
							  	case "addconsoletolist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserConsoleLists();
							  		System.out.println("Enter the Name of the list you want to add a console to?");
							  		listName = this.reader.readLine();
							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Enter the name of the Console you want to add");
							  		consoleName = this.reader.readLine();
							  		listHandler.addConsoleToList(listName, consoleName, consoleHandler.getGameMap());
							  		break;
							  	case "editconsolelist":
							  		break;
							  	case "removeconsolelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserConsoleLists();
							  		System.out.println("Enter the Name of the list you want to remove a console from: ");
							  		listName = this.reader.readLine();
							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Enter the name of the Console you want to remove");
							  		consoleName = this.reader.readLine();
							  		listHandler.removeConsoleFromList(listName, consoleName, consoleHandler.getGameMap());
							  		break;
							  	case "deleteconsolelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserConsoleLists();
							  		System.out.println("Enter the Name of the list you want to Delete: ");
							  		listName = this.reader.readLine();
							  		listHandler.deleteList(listName);
							  		break;
							  }
					  		  break;
					  }
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
        test.go(connection);
        reader.close();

    }

	public static boolean loginToDatabase(BufferedReader reader, DatabaseAttempt test, DatabaseConnectionService connection) {
    	try {
			if(reader.readLine().toLowerCase().equals("new")) {
				//login operations
				System.out.print("Insert Username: ");
				test.command = reader.readLine();
				String username = test.command;
				user = username;
				test.command = "";
				System.out.print("Insert Password: ");
				test.command = reader.readLine();
				String password = test.command;
				register(username,password,connection);
				return true;
			}else {
				System.out.print("Insert Username: ");
				test.command = reader.readLine();
				String username = test.command;
				user = username;
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
    public static boolean register(String username, String password, DatabaseConnectionService dbCon) {
    	Connection con = dbCon.getConnection();
		CallableStatement cs;
		try {
			cs = con.prepareCall("{? = call RegisterUser(?,?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, username);
			cs.setString(3, password);
			cs.execute();
			int value = cs.getInt(1);
			if(value == 1) {
				System.out.println("Null values are not allowed in the Login Table.");
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
}