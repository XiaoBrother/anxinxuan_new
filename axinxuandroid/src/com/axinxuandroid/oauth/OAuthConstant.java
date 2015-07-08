package com.axinxuandroid.oauth;

import com.anxinxuandroid.constant.HttpUrlConstant;

public class OAuthConstant {
	  public static int SINA_OAUTH_TYPE=1;
 	  public static int QQWEIBO_OAUTH_TYPE=2;
	  public static int TAOBAO_OAUTH_TYPE=3;
	  public static int WEIXIN_OAUTH_TYPE=5;
       public static class Sina{
    	// Ӧ�õ�key �뵽�ٷ�������ʽ��appkey�滻APP_KEY
        public static final String APP_KEY      = "390901783";
         // �滻Ϊ������REDIRECT_URL  
        public static final String REDIRECT_URL = HttpUrlConstant.URL_HEAD+"phoneuser/oauthRedirect";
         // ��֧��scope��֧�ִ�����scopeȨ�ޣ��ö��ŷָ�
        public static final String SCOPE = 
                "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write";

        public static final String CLIENT_ID         = "client_id";
        public static final String RESPONSE_TYPE     = "response_type";
        public static final String USER_REDIRECT_URL = "redirect_uri";
        public static final String DISPLAY           = "display";
        public static final String USER_SCOPE        = "scope";
        public static final String PACKAGE_NAME      = "packagename";
        public static final String KEY_HASH          = "key_hash";
        
        public static final String GET_USER_ID = "https://api.weibo.com/2/account/get_uid.json";
        public static final String GET_USER_INFO = "https://api.weibo.com/2/users/show.json";
        public static final String FOLLOW_ANXINXUAN  = "https://api.weibo.com/2/friendships/create.json";
    }
     //΢��  
     public static class WebChat{
    	 //puyinong@yeah.net liujian
       	// Ӧ�õ�key �뵽�ٷ�������ʽ��appkey�滻APP_KEY
           public static final String APP_ID      = "wxc0569f3f8ad4a084";
           public static final String APP_KEY      = "1f2d833c8f73cbab944c7aafa7fa6f9b";
           public static final String GET_ACCESS_TOKEN      = "https://api.weixin.qq.com/sns/oauth2/access_token";
           public static final String GET_USER_INFO      = "https://api.weixin.qq.com/sns/userinfo";
     }
     //�Ա�
     public static class TaoBao{
       	// Ӧ�õ�key �뵽�ٷ�������ʽ��appkey�滻APP_KEY
           public static final String APP_ID      = "21725210";
           public static final String APP_KEY      = "f246ef9efa5b914eb0670ca67584380e";
           // �滻Ϊ������REDIRECT_URL
           public static final String REDIRECT_URL = "com.axinxuandroid.activity://taobao";
     }
     //QQ΢��
     public static class QQWeiBo{
       	// Ӧ�õ�key �뵽�ٷ�������ʽ��appkey�滻APP_KEY
           public static final String APP_KEY     = "801478697";
           public static final String APP_SECRET     = "7ac1a9f6f1e9bad492ad2f4a0079cb28";
           public static final String GET_USER_INFO = "https://open.t.qq.com/api/user/info";
           
     }
     
}
