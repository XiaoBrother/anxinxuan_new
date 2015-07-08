package com.axinxuandroid.data;

import java.io.Serializable;

public class PhotoItem implements Serializable {
	private static final long serialVersionUID = 8682674788506891598L;
	private String  photoID;
 	private String path;
 	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	 
	public String getPhotoID() {
		return photoID;
	}

	public void setPhotoID(String photoID) {
		this.photoID = photoID;
	}

	@Override
	public boolean equals(Object o) {
 		if(o==null) return false;
 		PhotoItem item=(PhotoItem) o;
 		if(item.getPhotoID()!=null&&item.getPhotoID().equals(photoID))
 			return true;
 		return false;
	}

	@Override
	public int hashCode() {
 		return 1;
	}

	@Override
	public String toString() {
 		return photoID+":"+path;
	}

	 
    
 
	 
}
