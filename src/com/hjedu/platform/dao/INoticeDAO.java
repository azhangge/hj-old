package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.NoticeModel;

public abstract interface INoticeDAO
{
  public abstract NoticeModel findNotice(String id);

  public abstract List<NoticeModel> findAllNotices();
  
  public abstract List<NoticeModel> findAllNoticesByBusinessId(String businessId);

  public abstract void deleteNotice(String id);

  public abstract void addNotice(NoticeModel model);

  public abstract void updateNotice(NoticeModel model);
  
  public void updateNoticeCountPlusOne(String id);

  List<NoticeModel> findNoticesByAdmin(String adminid);

  public List<NoticeModel> findNoticeByIds(List<String> idList);
}