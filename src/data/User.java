package data;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Album> m_albums;
		
	private String m_userName;
	
	public User(String name){
		this.m_userName = name;
		this.m_albums = new ArrayList<Album>();
	}
	
	public String toString() {
		return m_userName;
	}
	
	public ArrayList<Album> getAlbum() {
		return m_albums;
	}
	
	public ArrayList<String> getAlbumName() {
		ArrayList<String> ret = new ArrayList<String>();
		
		for(Album a : m_albums) {
			ret.add(a.toString());
		}
		return ret;
	}
	
	public void addAlbum(Album e) {
		m_albums.add(e);
	}
	
	public int findIndex(String s) {
		if(m_albums == null) {
			return -1;
		}
		for (int i = 0; i < m_albums.size(); i++) {
			if (s.equals(m_albums.get(i).toString())) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean rmvUser(String s) {
		int plc = findIndex(s);
		if(plc != -1) {
			m_albums.remove(plc);
			return true;
		}
		return false;
	}
	
	public boolean editUser(String s, String newS) {
		int plc = findIndex(s);
		if(plc != -1) { 
			m_albums.get(plc).setString(newS);
			return true;
		}
		return false;
	}
}
