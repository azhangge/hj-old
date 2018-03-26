package com.hjedu.platform.dao;

import java.io.InputStream;

public interface IFlashDAO {

    public abstract void saveFlash(InputStream paramInputStream, String paramString);
    
    public void delFlash(String id) ;
}
