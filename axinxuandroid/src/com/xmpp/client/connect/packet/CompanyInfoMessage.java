package com.xmpp.client.connect.packet;

import org.jivesoftware.smack.packet.IQ;

public class CompanyInfoMessage   {
    private String id;
	private String name;//����
	private String createtime;//ʱ��
	private String count;//����
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
