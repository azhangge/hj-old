package com.hjedu.platform.dao;

import java.io.InputStream;

public interface IMp3DAO {

    public abstract void saveMp3(InputStream paramInputStream, String paramString);
    
    public void delMp3(String id) ;
}
