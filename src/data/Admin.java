package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Admin {
	private static ArrayList<User> users = new ArrayList<User>(Arrays.asList(new User("stock")));
	
	private static String folder = "data";
	private static String file = "listOfUser.ser";
		
	public static void save() {
		try {
	         FileOutputStream fileOut = new FileOutputStream(folder + File.separator + file);
			 ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(users);
	         out.close();
	         fileOut.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
	
	public static ArrayList<User> getUser() {
		return users;
	}
	
	public static User searchUser(String user) {
		//Search for user in the Admin
		for(int i = 0; i < Admin.getAllUser().size(); i++) {
			if(users.get(i).toString().equals(user)) {
				return users.get(i);
			}
		}
		return null;
	}
	
	public static ArrayList<String> getAllUser() {
		ArrayList<String> uNames = new ArrayList<String>();
		for (User u : users) {
			uNames.add(u.toString());
		}
		return uNames;
	}
	
	public static void resetDefault() {
		User u = Admin.getUser().get(0);
		if(u.toString().equals("stock")){
			//It should equal stock...
			Album a = new Album("Stock");
			for (int i = 1; i <= 6; i++) {
				a.addPhoto(new Photo("stock" + i + ".png", "Stock Picture " + i, true));
			}			
			u.addAlbum(a);
		}
	}
		
	@SuppressWarnings("unchecked")
	public static void load() throws ClassNotFoundException {
		try {
			 FileInputStream fileOut = new FileInputStream(folder + File.separator + file);
			 ObjectInputStream out = new ObjectInputStream(fileOut);
	         users = (ArrayList<User>) out.readObject();
	         out.close();
	         fileOut.close();
	      } catch (IOException i) {
//	         i.printStackTrace();
	      } 
	}

	public static void addUser(User u) {
		users.add(u);
	}

	public static boolean rmvUser(String s) {
		if(users == null) {
			return false;
		}
		for (int i = 0; i < users.size(); i++) {
			if (s.equals(users.get(i).toString())) {
				users.remove(i);
				return true;
			}
		}
		return false;
	}
}
