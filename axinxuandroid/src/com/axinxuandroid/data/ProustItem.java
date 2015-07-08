package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.List;

 
public class ProustItem {
	public int key;
	public String value;
	private static final List<ProustItem> ps=new ArrayList<ProustItem>();
	public ProustItem(int key,String vla){
		this.key=key;
		this.value=vla;
	}
	public static List<ProustItem> getAllProustItems(){
		if(ps.size()<1)
			initAllProustItem();
		return ps;
	}

	private static void initAllProustItem(){
		ps.add(new ProustItem(1, "����Ϊ��������ũ��Ʒ�������ģ�"));
		ps.add(new ProustItem(2, "����ϣ��δ������ũҵ�����д��ͻ�ƣ�"));
		ps.add(new ProustItem(3, "����Ϊ�Լ���ũ�������ŵ���ʲô��"));
		ps.add(new ProustItem(4, "������ϧ��������������ʲô��"));
		ps.add(new ProustItem(5, "ũҵ�����У�����ϲ����һ�����ڻ���̣�"));
		ps.add(new ProustItem(6, "�������ũ����ũ��Ʒ����ʹ�ù������Ĵ�����ʲô��"));
		ps.add(new ProustItem(7, "��ũ��ƷƷ�ʶ��ԣ�����ص���ʲô��"));
		ps.add(new ProustItem(8, "ũ���У�������˻�����ʲô�� "));
		ps.add(new ProustItem(9, "ũҵ���������У���ʱ�ε�����о�������֣�"));
		ps.add(new ProustItem(10, "�������Ըı�ũҵ�������̵�һ���£��ǻ���ʲô��"));
		ps.add(new ProustItem(11, "���ڵ�ǰũҵ��״�������ź�����ʲô��"));
		ps.add(new ProustItem(12, "�������������˵һ��Ļ���ʲô��"));
	}
	
	public static ProustItem getByKey(int key){
		if(ps.size()<1)
			initAllProustItem();
		for(ProustItem item:ps){
			if(item.key==key)
				return item;
		}
		return null;
	}
	 
	 
}
