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
			ProfileManagement p = new ProfileManagement(user, connection);
			while(true) {
				String gameName ="";
				String consoleName = "";
				String gameDesc="";
				String gameGenre="";
				String gameRelease="";
				String gamePub="";
				System.out.println();
				System.out.println("Enter command g for Game Commands");
				System.out.println("Enter command c for Console Commands");
				System.out.println("Enter command r for Rating Commands");
				System.out.println("Enter command u for User Commands");
				System.out.println("Enter command f for Friend Commands");
				System.out.println("Enter command l for Lists Commands");
				System.out.println("Enter command p for Profile Commands");
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
				    System.out.println("  AddConsolePlayedOn - Add a console that the game can be played on");
				    System.out.println("  RemoveConsolePlayedOn - Remove a console that the game can be played on");
				    System.out.println("  ViewPlayableOn - View the consoles you can play a game on");
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
							System.out.print("Enter the name of the game: ");
							gameName = this.reader.readLine();
							System.out.print("Enter the new name of the game(leave empty for no change): ");
							String newName = this.reader.readLine();
							System.out.print("Enter a description of the game(leave empty for no change): ");
							String newDesc = this.reader.readLine();
							System.out.print("Enter the genre of the game(leave empty for no change): ");
							String newGenre = this.reader.readLine();
							System.out.print("Enter the release date of the game(leave empty for no change): ");
							String newRelease = this.reader.readLine();
							System.out.print("Enter the publisher of the game(leave empty for no change): ");
							String newPub = this.reader.readLine();
							gameHandler.EditGame(gameName, newName, newDesc, newGenre, newRelease, newPub);
							break;
						case "addconsoleplayedon":
							System.out.print("Enter the name of the game: ");
							gameName = this.reader.readLine();
							consoleHandler.ReadAllConsoles();
							System.out.print("Enter the name of the console this game can be played on: ");
							consoleName = this.reader.readLine();
							gameHandler.createPlayedOn(gameName, consoleName, consoleHandler.getConsoleMap());
							break;
						case "removeconsoleplayedon":
							System.out.print("Enter the name of the game: ");
							gameName = this.reader.readLine();
							consoleHandler.ReadAllConsoles();
							System.out.print("Enter the name of the console this game can no longer be played on: ");
							consoleName = this.reader.readLine();
							gameHandler.removeGamePlayedOn(gameName, consoleName, consoleHandler.getConsoleMap());
							break;
						case "viewplayableon":
							System.out.print("Enter the name of the game: ");
							gameName = this.reader.readLine();
							gameHandler.gamePlayedOn(gameName);
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
				  case "c":
						System.out.println("This the the current list of Consoles");
						consoleHandler.ReadAllConsoles();
					    System.out.println("Console Commands:");
					    System.out.println("  CreateConseole -Add a console to the system");
					    System.out.println("  UpdateConsole -Update a console's information");
//					    System.out.println("  DeleteConsole -Remove a console from the system");
					    System.out.print("Enter command or press x to exit: ");
						command = this.reader.readLine();
						if(inputReaderIsX(command)) {break;}
						switch(command.toLowerCase()){
							case "createconseole":
								System.out.print("Enter the name of the console you want to create: ");
								consoleName = this.reader.readLine();
								consoleHandler.CreateConsole(consoleName);
								break;
							case "updateconsole":
								System.out.print("Enter the name of the console you want to update: ");
								consoleName = this.reader.readLine();
								System.out.print("Enter the new name for the console(leave empty for no change): ");
								String newName = this.reader.readLine();
								consoleHandler.UpdateConsole(consoleName, newName);
								break;
							case "deleteconsole":
								
								break;
						}
						break;
				  case "r":
					  System.out.println("Ratings Commands: ");
					  System.out.println("   CreateRating -Create a rating for a game");
					  System.out.println("   RemoveRating -Remove a rating you've made");
					  System.out.println("   UpdateRating -Update a rating you've made");
					  System.out.println("   ViewRatings -View all user ratings");
					  System.out.println("   ViewUserRatings -View a specific user's ratings");
					  String r = this.reader.readLine().toLowerCase();
					  if(r.equals("createrating")){
						  System.out.print("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  System.out.println("Please Enter Review: ");
						  String review = this.reader.readLine();
						  System.out.print("Please Enter Rating: ");
						  String rating = this.reader.readLine();
						  float rate = Float.parseFloat(rating);
						  ratingHandler.createRating(gameName, review, rate);
					  }else if(r.equals("removerating")) {
						  System.out.println("Your Reviews: ");
						  ratingHandler.readUserRatings(user);
						  System.out.print("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  ratingHandler.deleteRating(gameName);
					  }else if(r.equals("updaterating")) {
						  System.out.println("Your Reviews");
						  ratingHandler.readUserRatings(user);
						  System.out.print("Please Enter Game Name: ");
						  gameName = this.reader.readLine();
						  System.out.print("Please Enter New Review (leave empty if no change) \n");
						  String review = this.reader.readLine();
						  System.out.print("Please Enter New Rating (leave empty if no change): ");
						  String rating = this.reader.readLine();
						  float rate = Float.parseFloat(rating);
						  ratingHandler.updateRating(gameName, review, rate);
						  break;
					  }
					  else if(r.equals("viewratings")){
						  ratingHandler.readRatings();
					  }
					  else if(r.equals("viewuserratings")) {
						  p.viewUsers();
						  System.out.print("Enter the name of the user you want to view the ratings of: ");
						  String username = this.reader.readLine();
						  ratingHandler.readUserRatings(username);
					  }
					  else {
						  System.out.println("Invalid");
					  }
					  break;
					  
				  case "f":		  
					  
					  System.out.println("Friends List Commands: ");
					  System.out.println("  ViewFriends -View your friend list");
					  System.out.println("  ViewFriendOwnedList -View a friends list of owned games");
					  System.out.println("  AddFriend -Add a friend");
					  System.out.println("  RemoveFriend -Remove a friend");
					  System.out.print("  (B) Back \n Command: ");
					  command = this.reader.readLine().toLowerCase();
					  if(command.equals("viewfriends")) {
						  ArrayList<String> friends = fl.readFriendsList();
						  System.out.println("Friends: ");
						  for(int i = 0; i < friends.size(); i++) System.out.println(friends.get(i));
					  }else if(command.equals("viewfriendownedlist")){
						  ArrayList<String> friends = fl.readFriendsList();
						  for(int i = 0; i < friends.size(); i++) System.out.println(friends.get(i));
						  System.out.print("Please Enter Friend Name: ");
						  String name = this.reader.readLine();
						  ownedListHandler.ViewOwnedGames(name);
					  }
					  else if(command.equals("addfriend")){
						  System.out.print("Please Enter Friend Name: ");
						  String friendName = this.reader.readLine();
						  fl.addFriend(friendName);
					  }else if(command.equals("removefriend")) {
						  System.out.println("Current friends: ");
						  ArrayList<String> friends = fl.readFriendsList();
						  for(int i = 0; i < friends.size(); i++) System.out.println(friends.get(i));
						  System.out.print("Please Enter Friend Name: ");
						  String name = this.reader.readLine();
						  fl.removeFriend(name);
					  }else if(command.equals("b")) {
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
							  System.out.println("  ViewSpecificList -View A specific Game");
							  System.out.println("  ViewMyGameLists -View Your Game Lists");
							  System.out.println("  CreateGameList -Create a Game List");
							  System.out.println("  AddGameToList -Add a Game to a List");
//							  System.out.println("  EditGameList) Edit a Game on a List");
							  System.out.println("  RemoveGameList -Remove a Game from a List");
							  System.out.println("  DeleteGameList -Delete a Game List");
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
							  		gameHandler.ReadAllGames();
							  		System.out.print("Add your first game!: ");
							  		gameName = this.reader.readLine();
							  		listHandler.initGameList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "addgametolist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.print("Enter the Name of the list you want to add a game to: ");
							  		listName = this.reader.readLine();
							  		gameHandler.ReadAllGames();
							  		System.out.print("Enter the name of the Game you want to add: ");
							  		gameName = this.reader.readLine();
							  		listHandler.addGameToList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "editgamelist":
							  		break;
							  	case "removegamelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.print("Enter the Name of the list you want to remove a game from: ");
							  		listName = this.reader.readLine();
//							  		gameHandler.ReadAllGames();
							  		listHandler.viewGameList(listName);
							  		System.out.print("Enter the name of the Game you want to remove");
							  		gameName = this.reader.readLine();
							  		listHandler.removeGameFromList(listName, gameName, gameHandler.getGameMap());
							  		break;
							  	case "deletegamelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserLists();
							  		System.out.print("Enter the Name of the list you want to Delete: ");
							  		listName = this.reader.readLine();
							  		listHandler.deleteList(listName);
							  		break;
							  }
					  		  break;
					  	  case "cl":
					  		System.out.println("These are the Current Console lists: ");
					  		  listHandler.readAllConsoleLists();
					  		  System.out.println("Console List Commands");
							  System.out.println("  ViewSpecificList -View A specific Console");
							  System.out.println("  ViewMyConsoleLists -View Your Console Lists");
							  System.out.println("  CreateConsoleList -Create a Console List");
							  System.out.println("  AddConsoleToList -Add a Console to a List");
//							  System.out.println("  EditConsoleList) Edit a Console on a List");
							  System.out.println("  RemoveConsoleList -Remove a Console from a List");
							  System.out.println("  DeleteConsoleList -Delete a Console List");
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
							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Add your first console!: ");
							  		consoleName = this.reader.readLine();
							  		listHandler.consoleListInit(listName, consoleName, consoleHandler.getConsoleMap());
							  		break;
							  	case "addconsoletolist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserConsoleLists();
							  		System.out.println("Enter the Name of the list you want to add a console to?");
							  		listName = this.reader.readLine();
							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Enter the name of the Console you want to add");
							  		consoleName = this.reader.readLine();
							  		listHandler.addConsoleToList(listName, consoleName, consoleHandler.getConsoleMap());
							  		break;
							  	case "editconsolelist":
							  		break;
							  	case "removeconsolelist":
							  		System.out.println("Your Lists: ");
							  		listHandler.readUserConsoleLists();
							  		System.out.println("Enter the Name of the list you want to remove a console from: ");
							  		listName = this.reader.readLine();
							  		listHandler.viewConsoleList(listName);
//							  		consoleHandler.ReadAllConsoles();
							  		System.out.println("Enter the name of the Console you want to remove");
							  		consoleName = this.reader.readLine();
							  		listHandler.removeConsoleFromList(listName, consoleName, consoleHandler.getConsoleMap());
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
				  case "p":
					  System.out.println("(V) View Profiles");
					  System.out.println("(F) View Other Profiles");
					  System.out.println("(A) Add Profile");
					  System.out.println("(D) Delete Profile");
					  System.out.println("(E) Edit Profile");
					  String command = this.reader.readLine();
					  command = command.toLowerCase();
					  switch(command) {
					  	case("v"):
					  		p.viewFriendProfile(user);
					  		break;
					  	case("f"):
					  		p.viewUsers();
					  		System.out.println("Please Enter Their Username: ");
					  		String friend = this.reader.readLine();
					  		p.viewFriendProfile(friend);
					  		break;
					  	case("a"):
					  		System.out.println("Please Enter Profile Type (Ex. Twitch, Playstation, etc.): ");
					  		String type = this.reader.readLine();
					  		System.out.println("Please Enter Profile Name: ");
					  		String name = this.reader.readLine();
					  		p.addProfile(type, name);
					  		break;
					  	case("d"):
					  		System.out.println("Please Enter Profile Type (Ex. Twitch, Playstation, etc.): ");
					  		type = this.reader.readLine();
					  		System.out.println("Please Enter Profile Name: ");
					  		name = this.reader.readLine();
					  		p.deleteProfile(name, type);
					  		break;
					  	case("e"):
					  		System.out.println("Your current profiles: ");
					  		p.viewFriendProfile(user);
					  		System.out.println("Please Enter Current Profile Type (Ex. Twitch, Playstation, etc.): ");
					  		type = this.reader.readLine();
					  		System.out.println("Please Enter Current Profile Name: ");
					  		name = this.reader.readLine();
					  		System.out.println("Please Enter New Profile Type (Leave null if no change): ");
					  		String newType = this.reader.readLine();
					  		System.out.println("Please Enter New Profile Name: (Leave null if no change): ");
					  		String newName = this.reader.readLine();
					  		p.editProfile(name, type, newType, newName);
					  		break;
					  		}
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