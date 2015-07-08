package com.axinxuandroid.data;

public class UserFavorite {
	private long id; //id����
	private int favid;  
	private String label_name;	//	��ǩ����
	private int favorite_type;	//	���ͣ�ũ����Ʒ�ֵȵȣ�
	private int favorite_id;	//	 ���ղ���id
   	private int user_id;	  //	�û�id
    private int isdel;//�Ƿ���ɾ��
	private String lastoptime;//������ʱ��
   	public static class FavoriteType{
   	  	public static final int FavoriteType_OF_VILLEAGE=1;//ũ������
   	  	public static final int FavoriteType_OF_VARIETY=2;//Ʒ������
   	  	public static final int FavoriteType_OF_BATCH=3;//��������
   	  	public static final int FavoriteType_OF_USER=4;//�û�����
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
