package com.axinxuandroid.service;

import java.util.List;

import com.axinxuandroid.data.Advocate;
import com.axinxuandroid.db.AdvocateDB;
import com.axinxuandroid.sys.gloable.Gloable;

public class AdvocateService {
	private AdvocateDB advDB;

	public AdvocateService() {
		advDB = new AdvocateDB(Gloable.getInstance().getCurContext()
				.getApplicationContext());
	}

	public void clearData() {
		advDB.clearData();
	}

	// 保存或更新
	public void saveOrUpdateBatch(Advocate adv) {
		if (adv != null) {
			Advocate bc = this.getByUserWithRecord(adv.getUserid(),
					adv.getRecordid());
			if (bc == null) {
				advDB.insert(adv);
			} else {
				adv.setId(bc.getId());
				advDB.update(adv);

			}
		}

	}

	public void update(Advocate adv) {
		if (adv != null) {
			Advocate bc = this.getById(adv.getId());
			if (bc != null) {
				adv.setId(bc.getId());
				advDB.update(adv);
			}
		}
	}

	public Advocate getById(int id) {
		return advDB.selectbyId(id);
	}

	public Advocate getByUserWithRecord(int user_id, int recordid) {
		return advDB.selectbyUserIdRecordId(user_id, recordid);
	}

	public void deleteById(int id) {
		advDB.deleteById(id);
	}
	

	public List<Advocate> serachByUser(int userid) {
		return advDB.selectbyUserId(userid);
	}

}
