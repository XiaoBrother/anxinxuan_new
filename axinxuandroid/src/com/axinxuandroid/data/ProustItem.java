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
		ps.add(new ProustItem(1, "你认为最完美的农产品是怎样的？"));
		ps.add(new ProustItem(2, "你最希望未来哪种农业技术有大的突破？"));
		ps.add(new ProustItem(3, "你认为自己或农场最大的优点是什么？"));
		ps.add(new ProustItem(4, "你最珍惜的消费者评价是什么？"));
		ps.add(new ProustItem(5, "农业生产中，你最喜欢哪一个季节或过程？"));
		ps.add(new ProustItem(6, "形容你的农场或农产品，你使用过的最多的词语是什么？"));
		ps.add(new ProustItem(7, "就农产品品质而言，你最看重的是什么？"));
		ps.add(new ProustItem(8, "农场中，你最爱的人或东西是什么？ "));
		ps.add(new ProustItem(9, "农业生产过程中，何时何地让你感觉到最快乐？"));
		ps.add(new ProustItem(10, "如果你可以改变农业生产过程的一件事，那会是什么？"));
		ps.add(new ProustItem(11, "对于当前农业现状，你最遗憾的是什么？"));
		ps.add(new ProustItem(12, "你最想对消费者说一句的话是什么？"));
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
