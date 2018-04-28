package data;

import java.io.Serializable;

public class Tag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String m_type;
	private String m_value;
	
	public Tag(String type, String value){
		this.m_type = type;
		this.m_value = value;
	}
	
	public String getString() {
		return m_type + "=" + m_value;
	}
	
	public void setType(String type) {
		this.m_type = type;
	}
	
	public void setValue(String value) {
		this.m_value = value;
	}
	
	public String getType() {
		return m_type;
	}
	
	public String getValue() {
		return m_value;
	}
}
