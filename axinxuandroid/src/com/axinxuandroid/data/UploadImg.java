package com.axinxuandroid.data;

public class UploadImg {

	public static final int STATUS_PREPARE=1;//等待上传
	public static final int STATUS_UPLOADING=2;//正在上传
	public static final int STATUS_UPLOAD_FINISH=3;//上传完毕
	public static final int STATUS_UPLOAD_FINISH_ERROR=4;//上传失败
	private String path;
	private int status;
	
	public UploadImg(){
		this(null,STATUS_PREPARE);
	}
    public UploadImg(String path){
      	this(path,STATUS_PREPARE);
    }
    public UploadImg(String path,int status){
    	this.path=path;
    	this.status=status;
    }
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public boolean equals(Object o) {
		if(o==null||!(o  instanceof UploadImg) )  return false;
		UploadImg temp=(UploadImg) o;
		if(temp.getPath()!=null&&this.getPath()!=null&&this.getPath().equals(temp.getPath())){
			return true;
		}
 		return false;
	}
	@Override
	public int hashCode() {
 		return 1;
	}
    
    
}
