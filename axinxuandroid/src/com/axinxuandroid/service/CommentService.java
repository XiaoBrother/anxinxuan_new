package com.axinxuandroid.service;

 
 import java.util.ArrayList;
import java.util.List;

 

import com.axinxuandroid.data.Category;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Variety;

import com.axinxuandroid.db.CategoryDB;
import com.axinxuandroid.db.CommentDB;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.db.VarietyDB;
 
import com.axinxuandroid.sys.gloable.Gloable;

public class CommentService {
	private CommentDB commentDB;
 	public  CommentService(){
 		commentDB=new CommentDB(Gloable.getInstance().getCurContext().getApplicationContext());
  	}
 	public void clearData(){
 		commentDB.clearData();
	}
	 //保存
    public void saveOrUpdate(Comment comment) { 
    	if(comment!=null){
    		Comment dbcom=getByCommentid(comment.getComment_id());
    		if(dbcom==null){
    			commentDB.insert(comment);
    		}else{
    			if(dbcom.getIsdel()!=SystemDB.DATA_HAS_DELETE){
    				comment.setId(dbcom.getId());
        			commentDB.update(comment);
    			}
    		
    		}
    		
     	}
     }
	 
    
    public void deleteById(int id){
    	commentDB.deleteById(id);
    }
    public  Comment  getByCommentid(int commentid) {
    	return commentDB.getByCommentid(commentid);
    }
     
    public List<Comment> getCommentByRecordid(int recordid) { 
     	return commentDB.getCommentByRecordid(recordid);
    }
    
    public List<Comment> getUserSendComment(int userid,String lastoptime) { 
     	return commentDB.getUserSendComment(userid, lastoptime);
    }
    public List<Comment> getUserReceiveComment(int userid,String lastoptime) { 
     	return commentDB.getUserReceiveComment(userid, lastoptime);
    }
    public String getLatoptime(int recordid) {
    	return commentDB.getLatoptime(recordid);
    }
    public String getUserReceiveLatoptime(int userid) {
    	return commentDB.getUserReceiveLatoptime(userid);
    }
    public String getUserSendLatoptime(int userid) {
    	return commentDB.getUserSendLatoptime(userid);
    }
}
