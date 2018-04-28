package data;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Photo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isStock;
	private File m_file;
	private String m_caption = "";
	
	private ArrayList <Tag> m_tags = new ArrayList<Tag>();
	
	Date m_date;
	
	public Photo(Photo copyP) {
		this.isStock = copyP.isStock;
		this.m_file = copyP.getFile();
		this.m_caption = copyP.getCaption();
//		this.tmpFile = copyP.getTmpFile();
		this.m_date = copyP.getDate();
	}
	
	public Photo(String url, String caption, boolean isStock) {
		this.isStock = isStock;
		this.m_file = new File("stock" + File.separator + url);
		this.m_caption = caption;
		this.m_date = new Date(this.m_file.lastModified());
	}
		
	public Photo(File newFile, String caption) {
		this.isStock = false;
		this.m_file = newFile;
		this.m_caption = caption;
//		this.tmpFile = new File(newFile.toString());
		this.m_date = new Date(this.m_file.lastModified());
	}
	
	public File getFile() {
		return m_file;
	}
	
//	public File getTmpFile() {
//		return tmpFile;
//	}
	
	public ArrayList<String> getTagString() {
		ArrayList<String> ret = new ArrayList<String>();
		
		if(m_tags.size() < 1) {
			return ret;
		}
		
		for(int i = 0; i < m_tags.size(); i++) {
			ret.add(m_tags.get(i).getType() + "=" +  m_tags.get(i).getValue());
		}
		
		return ret;
	}
	
	public boolean removeTag(String type, String value) {
		for(int i = 0; i < m_tags.size(); i++) {
			if(m_tags.get(i).getType().equals(type) && m_tags.get(i).getValue().equals(value)){
				m_tags.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public int getSize() {
		return m_tags.size();
	}
	
	public void setCaption(String caption) {
		this.m_caption = caption;
	}
	
	public String getCaption() {
		return m_caption;
	}
	
	public String toString() {
		return m_caption;
	}

	public String getURL() {
		return "file:" + this.m_file.getAbsolutePath();
	}
	
	public String getTime() {
		return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(this.m_date);
	}
	
	public Date getDate() {
		return this.m_date;
	}
	
	public void setTag(Tag newTag) {
		m_tags.add(newTag);
	}
	
	public ArrayList<Tag> getTag() {
		return m_tags;
	}
	
	public void setArrTags(ArrayList<Tag> tags) {
		this.m_tags.addAll(tags);
	}
}
