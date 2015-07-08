package com.xmpp.client.connect.packet;

import org.jivesoftware.smack.packet.IQ;

public class CompanyInfoMessage   {
    private String id;
	private String name;//名称
	private String createtime;//时间
	private String count;//人数
	private String address;
	 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
}
