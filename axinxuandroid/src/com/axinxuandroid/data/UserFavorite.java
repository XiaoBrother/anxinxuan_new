package com.axinxuandroid.data;

public class UserFavorite {
	private long id; //id自增
	private int favid;  
	private String label_name;	//	标签名称
	private int favorite_type;	//	类型（农场、品种等等）
	private int favorite_id;	//	 被收藏者id
   	private int user_id;	  //	用户id
    private int isdel;//是否已删除
	private String lastoptime;//最后操作时间
   	public static class FavoriteType{
   	  	public static final int FavoriteType_OF_VILLEAGE=1;//农场类型
   	  	public static final int FavoriteType_OF_VARIETY=2;//品种类型
   	  	public static final int FavoriteType_OF_BATCH=3;//批次类型
   	  	public static final int FavoriteType_OF_USER=4;//用户类型
   	}
   	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getFavid() {
		return favid;
	}
	public void setFavid(int favid) {
		this.favid = favid;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String labelName) {
		label_name = labelName;
	}
	public int getFavorite_type() {
		return favorite_type;
	}
	public void setFavorite_type(int favoriteType) {
		favorite_type = favoriteType;
	}
	public int getFavorite_id() {
		return favorite_id;
	}
	public void setFavorite_id(int favoriteId) {
		favorite_id = favoriteId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public String getLastoptime() {
		return lastoptime;
	}
	public void setLastoptime(String lastoptime) {
		this.lastoptime = lastoptime;
	}
	@Override
	public boolean equals(Object o) {
		if(o==null||!(o instanceof UserFavorite))return false;
		UserFavorite fv=(UserFavorite) o;
		if(fv.getFavid()==this.getFavid())
			return true;
		return false;
 	}
	@Override
	public int hashCode() {
 		return -1;
	}
   	
   	
   	
}
