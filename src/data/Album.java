package data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class Album implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String m_albumName;
		
	private ArrayList<Photo> m_photos;
	
	public Album(String album) {
		this.m_albumName = album;
		this.m_photos = new ArrayList<Photo>();
	}	
	
	public String toString() {
		return m_albumName;
	}
	
	public String getSortDate(boolean isEarly) {
		if(m_photos.size() == 0) {
			return "No Photo";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		
		if(m_photos.size() == 1) {
			return df.format(m_photos.get(0).m_date);
		}
		
		ArrayList<Photo> test = new ArrayList<Photo>(m_photos);
		ArrayList<Date> listOfDates = new ArrayList<Date>();
		
		for(Photo p : test) {
			listOfDates.add(p.getDate());
		}
		
		if(isEarly) {
			Date minDate = Collections.min(listOfDates);
			return df.format(minDate);
		} else {
			Date maxDate = Collections.max(listOfDates);
			return df.format(maxDate);
		}
	}
	
	public ArrayList<Photo> getPhotos() {
		return m_photos;
	}
	
	public int findIndex(String s) {
		if(m_photos == null) {
			return -1;
		}
		for (int i = 0; i < m_photos.size(); i++) {
			if (s.equals(m_photos.get(i).toString())) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean rmvPhoto(String s) {
		int plc = findIndex(s);
		if(plc != -1) {
			m_photos.remove(plc);
			return true;
		}
		return false;
	}
	
	public void addPhoto(Photo newPhoto) {
		m_photos.add(newPhoto);
	}
	
	public int getSize() {
		if(m_photos.size() == 0) {
			return 0;
		}
		return m_photos.size();
	}

	public void setString(String s) {
		this.m_albumName = s;
	}

	public ArrayList <String> getPhotosName() {
		ArrayList<String> ret = new ArrayList<String>();
		
		for(Photo p : m_photos) {
			ret.add(p.toString());
		}
		
		return ret;
	}
}
