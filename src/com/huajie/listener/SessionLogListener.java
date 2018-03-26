package com.huajie.listener;

import java.util.Date;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionLogListener
  implements HttpSessionListener
{
  Date c = null;
  Date g = null;

  public void sessionCreated(HttpSessionEvent event)
  {
  }

  public void sessionDestroyed(HttpSessionEvent arg0)
  {
  }
}