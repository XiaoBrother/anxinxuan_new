package com.anxinxuandroid.constant;


public class HttpUrlConstant {
	//与web端链接的url头
	public static final String URL_HEAD="http://www.anxinxuan.com/";
	//检测版本
	public static final  String CHECK_VERSION = "update/version";
	//下载最新版本
	public static final  String DOWN_VERSION = "update/down";
	//获得记录信息
	public static final String URL_RECORDTIMELINW="phone/recordTimeline";
	//获得记录以及批次信息
	public static final String RECORD_INFO="phone/recordinfo";
	//获得用户创建的记录信息
	public static final String USER_RECORDS="phone/userRecords";
	//环节信息
	public static final String URL_GETTPYEDATAS="phone/queryVarityLabel";
	//上传图片url
	public static final  String URL_IMG = "phone/uploadImgs";
	//上传农场图片
	public static final  String UPLOAD_VILELAGE_PHOTO = "phone/uploadphoto";
	//存储记录url
	public static final  String URL_SAVERECORD = "phone/saverecord";
	//手机验证
	public static final  String SEND_PHONE_VALID_NUM = "phoneuser/sendpdaValidNum";
	//用户登录
	public static final  String USER_LOGIN = "phoneuser/userlogin";
	//用户信息
	public static final  String USER_INFO = "phoneuser/userinfo";
	//用户赞
	public static final  String ADD_ADVOCATE = "phoneuser/advocate";
	//重设密码
	public static final  String USER_RESET_PWD = "phoneuser/resetpwd";
	//获得批次信息
	public static final  String URL_GETBATCHS = "phone/farmbatch";
	//获得批次信息
	public static final  String URL_BATCHINFO = "phone/batchinfo";
 
	//上传音频url
	public static final  String URL_MEDIA = "phone/uploadMedia";
	//用户注册
	public static final  String USER_REGISTE = "phoneuser/registerUser";
	//用户OAuth登录
	public static final  String USER_OAUTH_LOGIN = "phoneuser/oauthlogin";
	//获得表格json
	public static final String TALBEL_JSON="phone/tabeljson";
	//获得refresh_token
	public static final String YUKU_GET_REFRESH_TOKEN="yuku/getRefreshToken";
	//释放refresh_token
	public static final String YUKU_RELEASE_REFRESH_TOKEN="yuku/relaseFefreshToken";
	//保存视频信息
	public static final String SAVE_VIDEO_INFO="phone/uploadVideo";
	//同步模板
	public static final String SYNCHRONISE_TEMPLATE="phone/template";
	//检查模板版本
	public static final String TEMPLATE_CHECKVERSION="phone/checktemplate";
	//获取更新模板
	public static final String LOAD_TEMPLATE="phone/gettemplate";
	//删除记录
	public static final String DELETE_RECORD="phone/deleterecord";
	//加载评论
	public static final String RECORD_COMMENT="phone/recordcomment";
	//加载用户评论
	public static final String USER_COMMENT="phone/usercomment";
	//添加评论
	public static final  String SAVE_RECORD_COMMENT = "phone/savecomment";
	//删除评论
	public static final  String DELETE_RECORD_COMMENT = "phone/deletecomment";
	//加载品种
	public static final String LOAD_VARIETY="phone/variety";
	//加载普鲁斯特问卷
	public static final  String LOAD_PROUST = "phoneuser/proust";
	//删除普鲁斯特问卷
	public static final String DELETE_RROUST="phoneuser/deleteproust";
	//更新普鲁斯特问卷
	public static final String UPDATE_RROUST="phoneuser/updateproust";
	//添加批次
	public static final  String ADD_BATCH = "phone/addbatch";
	//删除批次
	public static final String DELETE_BATCH="phone/deletebatch";
	//更新批次过程
	public static final String BATCH_STAGE="phone/batchstage";
	//批次记录数
	public static final String BATCH_RECORD_COUNT="phone/recordcount";
	//更新批次状态
	public static final String BATCH_STATUS="phone/batchstatus";
	//添加品种
	public static final  String ADD_VARIETY = "phone/addvariety";
	//获取更新日志
	public static final  String SYSTEM_UPDATE_LOG = "syslog/readlog";
 	//获得农场Banner
	public static final  String VILLEAGE_INFO = "phone/farminfo";
	//保存农场简介
	public static final  String SAVE_VILLEAGE_DESC= "phone/farmdesc";
	//获得用户收藏
	public static final  String USER_FAVORITE = "phoneuser/userfavorite";
	//获得农场图片
	public static final  String VILLEAGE_PHOTO = "phone/farmphoto";
	//上传错误信息
	public static final  String UPLOAD_SYSTEM_ERROR = "syslog/addphonelog";
	//添加用户收藏
	public static final  String ADD_USER_FAVORITE = "phoneuser/adduserfavorite";
	//删除用户收藏
	public static final  String REMOVE_USER_FAVORITE = "phoneuser/removefavorite";
	//用户农场
	public static final  String USER_VILLEAGE = "phoneuser/userfarm";
	//手机登录Web
	public static final  String PHONE_LOGIN_WEB = "http://www.anxinxuan.com:8080/login";
	//品种批次信息
	public static final String VAREITY_BATCH="phone/varietybatch";
	//验证防伪码
    public static final  String VALID_SECURITY_CODE = "phone/vscode";
    //获取更新品种
  	public static final String LOAD_CATEGORY="phone/category";
  //删除农场图片
  	public static final String DELETE_VILLEAGE_PHOTO="phone/deletephoto";
  	
	//防伪设置
	public static final String LOAD_ANTIFAKE_QUERYSET="phone/queryset";
	//储存设置
	public static final  String SAVE_ANTIFAKE_SAVESET = "phone/saveset";
	//储存防伪码生成
	public static final  String SAVE_ANTIFAKE_PUBLISHBATCH = "phone/saveScodebatch";
	//储存防伪码生成
	public static final  String Load_ANTIFAKE_BATCHLIST = "phone/scodeBatchList";
}
