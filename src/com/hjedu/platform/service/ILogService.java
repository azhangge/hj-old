
package com.hjedu.platform.service;

import com.hjedu.customer.entity.AdminInfo;

public interface ILogService {

    public void log(String str);
    
    public void log(String str,AdminInfo admin);
}
