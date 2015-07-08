package com.axinxuandroid.data;

public class VideoInfo {
	public String videoid; //视频唯一ID
	public String title;//视频标题
	public String link;//视频播放链接
	public String thumbnail;//视频截图
	public int duration;//视频时长，单位：秒
	public String category;//视频分类
	public int state;//视频状态 normal: 正常 encoding: 转码中 fail: 转码失败 in_review: 审核中 blocked: 已屏蔽
	public String published;//发布时间
	public String description;//视频描述
}
