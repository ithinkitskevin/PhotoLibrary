package data;

import java.time.LocalDate;
import java.util.ArrayList;

public class Context {
	private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }
    
    private int currentIndex = 0;
    private ArrayList<Photo> sortedPhoto = new ArrayList<Photo>();
    private User u = null;
    private Album a = null;
    private Photo p = null;
    private LocalDate m_from;
	private LocalDate m_to;
	private ArrayList<Photo> m_array = new ArrayList<Photo>();;
	private boolean sselector = true;
	
	public void setComeFromSSelector(boolean set) {
		this.sselector = set;
	}
	
	public boolean getComeFromSSelector(){
		return sselector;
	}
	
	public ArrayList<Photo> getArray(){
		return m_array;
	}
	
	public void setArray(ArrayList<Photo> newPhoto){
		if(newPhoto == null) {
			return;
		}
		if(this.m_array.size() > 0) {
			this.m_array.clear();
		}
		this.m_array = new ArrayList<Photo>(newPhoto);
	}
	
	public LocalDate getFrom() {
		return m_from;
	}
	
	public LocalDate getTo() {
		return m_to;
	}
	
	public void setFrom(LocalDate from) {
		this.m_from = from;
	}
	
	public void setTo(LocalDate to) {
		this.m_to = to;
	}
    
    public Photo getPhoto() {
		return p;
	}
	
	public void setPhoto(Photo pho) {
		this.p = pho;
	}
    
    public Album getAlbum() {
		return a;
	}
	
	public void setAlbum(Album alb) {
		this.a = alb;
	}
    
    public User getUser() {
		return u;
	}
	
	public void setUser(User set) {
		this.u = set;
	}
    
    public int getCurrInd() {
    		return currentIndex;
    }
    
    public void setCurrInd(int currInd) {
		this.currentIndex = currInd;
    }
    
    public ArrayList<Photo> currPhotos(){
		return sortedPhoto;
}
    
    public ArrayList<Photo> getPhotos(){
    		return sortedPhoto;
    }
    
    public void setPhotos(ArrayList<Photo> arr){
    		if(arr == null) {
			return;
		}
		if(sortedPhoto.size() > 0) {
			sortedPhoto.clear();
		}
		sortedPhoto = new ArrayList<Photo>(arr);
    }
}
