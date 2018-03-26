package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.NoticeInformation;

public abstract interface INoticeInformationDAO {
	public abstract List<NoticeInformation> getNoticeInformations(int firstindex,int maxresult);
}
