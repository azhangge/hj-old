package com.hjedu.platform.dao;


import java.util.List;
import java.util.Map;

import com.hjedu.platform.entity.NoticeUser;

public abstract interface INoticeUserDAO
{
  public abstract NoticeUser findNoticeUser(String id);

  public abstract List<NoticeUser> findAllNoticeUser();

  public abstract void deleteNoticeUser(String id);

  public abstract void addNoticeUser(NoticeUser noticeUser);

  public abstract void updateNoticeUser(NoticeUser noticeUser);

  public abstract List<NoticeUser> findByUserId(String userid,int firstindex,int maxresult,Map<String,String> orderby);

  public abstract List<NoticeUser> findReadNoticeListByUser(String userid, int firstindex, int maxresult, Map<String, String> orderby);
  
  public abstract List<NoticeUser> findUnreadNoticeListByUser(String userid, int firstindex, int maxresult, Map<String, String> orderby);
}