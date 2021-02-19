import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class main {
	private static String URL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}}";

    public static void read(String inputFile) throws IOException  {
    	Connection connection = null;
    	String fullUrl = URL
				.replace("${dbServer}", "titan.csse.rose-hulman.edu")
				.replace("${dbName}", "GamerBase-S1G8-DEMO")
				.replace("${user}", "fryaj1")
				.replace("${pass}", "fryana1221");
		
		try {
			connection = DriverManager.getConnection(fullUrl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CallableStatement cs;
    	Map<String, Integer> publisherIdMap = new HashMap<>();
    	Map<String, Integer> consoleIdMap = new HashMap<>();
    	Map<String, Integer> gameIdMap = new HashMap<>();
    	Map<String, Integer> listIdMap = new HashMap<>();
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            //sheet 3 is the initial publishers in the database
            //col 0 is the publisher name
            Sheet sheet = w.getSheet(3);
            System.out.println("working with publishers");
            for (int i = 0; i < sheet.getRows(); i++) {
            	for (int j = 0; j < sheet.getColumns(); j++) {   
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String pubName = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                //because there is only one row we can add to the database in here
	                try {
						cs = connection.prepareCall("{call CreatePublisher(?)}");
						cs.setString(1, pubName);
		    			cs.execute();
		    			publisherIdMap.put(pubName,i);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    			System.out.println("Publisher created successfully");
                }
            	System.out.println();
            }
            System.out.println(publisherIdMap.toString());
            
            
            //sheet 2 is the initial consoles in the database
            //col 0 is the console name
            sheet = w.getSheet(2);
            System.out.println("working with consoles");
            for (int i = 0; i < sheet.getRows(); i++) {
            	for (int j = 0; j < sheet.getColumns(); j++) {   
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String consoleName = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                //because there is only one row we can add to the database in here
	                try {
						cs = connection.prepareCall("{call CreateConsole(?)}");
						cs.setString(1, consoleName);
		    			cs.execute();
		    			consoleIdMap.put(consoleName, i);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            	System.out.println();
            }
            System.out.println(consoleIdMap.toString());
            //sheet 1 is the initial games in the database
            //col 0 is Game name, col 1 is Description col 2 is Genre, col 3 is Release Date, col 4 is the publisher 
            //col 6 onwards is the consoles the game is playable on
            sheet = w.getSheet(1);
            System.out.println("working with Games");
            for (int i = 0; i < sheet.getRows(); i++) {
            	//init vars
            	String gameName = "";
            	String gameDesc = "";
            	String gameGenre = "";
            	String releaseDate = "";
            	String pubName = "";
            	ArrayList<String> consolesPlayedOn = new ArrayList<>();
            	for (int j = 0; j < sheet.getColumns(); j++) {   
                    Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                if(j < 5) {
	                	switch(j) {
	                	case 0:
	                		gameName = cellContents;
	                		break;
	                	case 1:
	                		gameDesc = cellContents;
	                		break;
	                	case 2:
	                		gameGenre = cellContents;
	                		break;
	                	case 3:
	                		releaseDate = cellContents;
	                		break;
	                	case 4:
	                		pubName = cellContents;
	                		break;
	                	}
	                }
	                else if(j > 5 && !cellContents.equals("")) {
	                	consolesPlayedOn.add(cellContents);
	                }
	                
	                }
                //add game to database 
                Integer pubID = publisherIdMap.get(pubName);
                if(pubID == null) pubID = 0;
                try {
                	gameDesc = gameDesc.equals("NULL") ? null : gameDesc;
                	gameGenre = gameGenre.equals("NULL") ? null : gameGenre;
                	releaseDate = releaseDate.equals("NULL") ? null : releaseDate;
        			cs = connection.prepareCall("{? = call CreateGame(?,?,?,?,?)}");
        			cs.registerOutParameter(1, Types.INTEGER);
        			cs.setString(2, gameName);
        			cs.setString(3, gameDesc);
        			cs.setString(4, gameGenre);
        			cs.setString(5, releaseDate);
        			cs.setInt(6, pubID);
        			cs.execute();
        			int value = cs.getInt(1);
        			if(value == 1) {
        				System.out.println("GameName cannot be null or empty.");
        				return;
        			}
        			else if(value == 2) {
        				System.out.println("Publisher has to be an existing publisher or null.");
        				return;
        			}
        			gameIdMap.put(gameName, i);
                }
                catch (SQLException e) {
					
					e.printStackTrace();
				}
                
                //add played on connections
                for(String consolePlayedOn : consolesPlayedOn) {
                	Integer gameID = gameIdMap.get(gameName);
            		Integer consoleID = consoleIdMap.get(consolePlayedOn);
            		try {
            			cs = connection.prepareCall("{? = call CreatePlayedOn(?, ?)}");
            			cs.registerOutParameter(1, Types.INTEGER);
            			cs.setInt(2, gameID);
            			cs.setInt(3, consoleID);
            			cs.execute();
            			int value = cs.getInt(1);
            			System.out.println("Played on connection created!");
            		}catch (SQLException e) {
            			e.printStackTrace();
            		}
                }
            	System.out.println();
            }
            System.out.println(gameIdMap.toString());
            
            //Sheet 4 is the users sheet
            //TODO: update number
            //col 0 is the name of the user col 1 is the user's salt, col 2 is the user's hash
            sheet = w.getSheet(4);
            System.out.println("working with users");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String username = "";
            	String salt = "";
            	String hash = "";
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                if(j < 5) {
	                	switch(j) {
	                	case 0:
	                		username = cellContents;
	                		break;
	                	case 2:
	                		salt = cellContents;
	                		break;
	                	case 3:
	                		hash = cellContents;
	                		break;
	                	}
	                }
            	}
            	System.out.println();
            	try {
        			cs = connection.prepareCall("{? = call RegisterUserSecure(?,?,?)}");
        			cs.registerOutParameter(1, Types.INTEGER);
        			cs.setString(2, username);
        			cs.setString(3,salt);
        			cs.setString(4, hash);
        			cs.execute();
        			int value = cs.getInt(1);
        			if(value == 1) {
        				System.out.println("Registration Failed.");
        			}
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
            }
            //Sheet 6 is the friends sheet
            //TODO: update number
            //col 0 is the name of the Friender col 1 is the name of the friendee
            sheet = w.getSheet(5);
            System.out.println("working with friendships");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String friender = "";
            	String friendee = "";
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                switch(j) {
		                case 0:
		               		friender = cellContents;
		               		break;
		               	case 1:
		               		friendee = cellContents;
		               		break;
	               	}
	                
            	}
            	System.out.println();
            	
            	String queryString = "insert into FriendsWith(Friender, Friendee) values (?, ?)";
        		PreparedStatement stmt;
        		try {
        			stmt = connection.prepareStatement(queryString);
        			stmt.setString(1, friender);
        			stmt.setString(2, friendee);
        			stmt.executeQuery();
        			stmt = connection.prepareStatement(queryString);
        			stmt.setString(2, friender);
        			stmt.setString(1, friendee);
        			stmt.executeQuery();
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		
            }
            //Sheet 7 is the profiles sheet
            //TODO: update number
            //col 0 is the name of the user col 1 is the account type and col 2 is the name of the account
            sheet = w.getSheet(6);
            System.out.println("working with profiles");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String username = "";
            	String accountName = "";
            	String accountType = "";
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
            		switch(j) {
	                case 0:
	                	username = cellContents;
	               		break;
	               	case 1:
	               		accountType = cellContents;
	               		break;
	               	case 2:
	               		accountName = cellContents;
	               		break;
            		}
            	}
            	System.out.println();
            	try {
        			cs = connection.prepareCall("{call CreateProfile(?, ?, ?)}");
        			cs.setString(1, accountType);
        			cs.setString(2, accountName);
        			cs.setString(3, username);
        			int result = cs.executeUpdate();
        			if(result == 1){
        				System.out.print("Profile Added successfully");
        			}else {
        				System.out.print("Profile failed to add");
        			}
        		} catch (SQLException e) {
        			System.out.print("Profile failed to add");
        		}
            }
          //Sheet 7 is the profiles sheet
            //TODO: update number
            //col 0 is the name of the user and col 1 onwards is the games that the user owns
            sheet = w.getSheet(7);
            System.out.println("working with owning games");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String username = "";
            	ArrayList<String> ownedGames = new ArrayList<>();
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
            		switch(j) {
	                case 0:
	                	username = cellContents;
	               		break;
	               	default:
	               		
	               		if(!cellContents.equals(""))ownedGames.add(cellContents);
	               		break;
            		}
            	}
            	System.out.println();
            	for(String game : ownedGames) {
        			Integer gameId = gameIdMap.get(game);
        			System.out.println(gameId);
        			try {
        				cs = connection.prepareCall("{? = call AddOwnedGame(?,?)}");
        				cs.registerOutParameter(1, Types.INTEGER);
        				cs.setInt(2, gameId);
        				cs.setString(3, username);
        				cs.execute();
        				int value = cs.getInt(1);
        				if(value == 1) {
        					System.out.println("The game must exist in the system.");
        				}
        				else if(value == 2) {
        					System.out.println("The user must be an existing user.");
        				}
        				if(value==0)System.out.println("Game added to owned list sucessfully!");
        				System.out.println();
        			} catch (SQLException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
            		
            }
            
            //Sheet 8 is the lists sheet
            //TODO: update number
            //col 0 is the name of the user and col 1 is the name of the list and col 3 onwards is the items on the list
            sheet = w.getSheet(8);
            System.out.println("working with lists");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String username = "";
            	String listName = "";
            	ArrayList<String> listItems = new ArrayList<>();
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
	                if(j > 2 && !cellContents.equals("")) {
                		listItems.add(cellContents);
                	}
	                if(j < 2) {
		                switch(j) {
			                case 0:
			                	username = cellContents;
			                	break;
			                case 1:
			                	listName = cellContents;
			                	break;
		                	
		                }
	                }
            	}
            	System.out.println();
            	try {
        			cs = connection.prepareCall("{? = call CreateList(?,?)}");
        			cs.registerOutParameter(1, Types.INTEGER);
        			cs.setString(2, listName);
        			cs.setString(3, username);
        			cs.execute();
        			int result = cs.getInt(1);
        			if(result == 1) { //both of these cases shouldn't be reached but just in case
        				System.out.println("User must be logged in to create a list");
        			}
        			else if(result == 2) {
        				System.out.println("Listname cannot be null");
        			}
        			cs = connection.prepareCall("{? = call fn_GetListID(?)}");
        			cs.registerOutParameter(1, Types.INTEGER);
        			cs.setString(2, listName);
        			cs.execute();
        			listIdMap.put(listName, cs.getInt(1));
        			System.out.println("List added sucessfully!");
        			System.out.println();
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	
            	for(String listItem : listItems) {
            		Integer listID = listIdMap.get(listName);
            		System.out.println("LIST ID!!!!!!!! " + listID);
            		Integer gameID = gameIdMap.get(listItem);
            		Integer consoleID = consoleIdMap.get(listItem);
            		if(consoleID == null) {
            			try {
            				cs = connection.prepareCall("{? = call AddGameToList(?,?)}");
            				cs.registerOutParameter(1, Types.INTEGER);
            				cs.setInt(2, listID);
            				cs.setInt(3, gameID);
            				cs.execute();
            				int value = cs.getInt(1);
            				if(value == 1) {
            					System.out.println("The listname given wasn't valid");
            				}
            				else if(value == 2) {
            					System.out.println("The game given must be in the games list");
            				}
            				else if(value == 3) {
            					System.out.println("Cannot add to other people's lists");
            				}
            				System.out.println("Game added sucessfully!");
            				System.out.println();
            			} catch (SQLException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            		else {
            			System.out.println("CONSOLE ID!!! " + consoleID);
            			try {
            				cs = connection.prepareCall("{? = call AddConsoleToList(?,?)}");
            				cs.registerOutParameter(1, Types.INTEGER);
            				cs.setInt(3, listID);
            				cs.setInt(2, consoleID);
            				cs.execute();
            				int value = cs.getInt(1);
            				if(value == 2) {
            					System.out.println("The listname given wasn't valid");
            				}
            				else if(value == 1) {
            					System.out.println("The console given must be in the console list");
            				}
            				System.out.println("Console added sucessfully!");
            				System.out.println();
            			} catch (SQLException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            	}
            	
            	
            }
          //Sheet 9 is the lists sheet
            //TODO: update number
            //col 0 is the name of the user and col 1 is the name of game col 2 is the review and col 3 is the rating;
            sheet = w.getSheet(9);
            System.out.println("working with ratings");
            for (int i = 0; i < sheet.getRows(); i++) {
            	String username = "";
            	String gameName = "";
            	String review = "";
            	float rating = 0;
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
                    CellType type = cell.getType();
                    String cellContents = cell.getContents();
	                System.out.print(cell.getContents() + " | ");
            		switch(j) {
	            		case 0:
	            			username = cellContents;
	            			break;
	            		case 1:
	            			gameName = cellContents;
	            			break;
	            		case 2:
	            			review = cellContents;
	            			break;
	            		case 3:
	            			rating = Float.parseFloat(cellContents);
	            			break;
            		}
            	}
            	System.out.println();
            	Integer gameID = gameIdMap.get(gameName);
            	try {
        			cs = connection.prepareCall("{call CreateRating(?, ?, ?, ?)}");
        			cs.setInt(1, gameID);
        			cs.setString(2, username);
        			cs.setString(3, review);
        			cs.setFloat(4, rating);
        			cs.executeUpdate();
        			System.out.println("Rating created successfully");
        		} catch (SQLException e) {
        			System.out.println("Creation failed");
        			e.printStackTrace();
        		}
            }
            
            
            
            
        } catch (BiffException e) {
            e.printStackTrace();
        }
        
    }
	
	public static void main(String[] args) throws IOException {
//		ReadExcel test = new ReadExcel();
//        setInputFile("c:/temp/lars.xls");
        read("C:\\Users\\sampsod1\\Documents\\GamerBaseJavaDataImport\\src\\GamerData.xls");

	}
	

}

