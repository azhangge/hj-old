package com.hjedu.platform.dao;

import java.io.InputStream;

public interface IImgDAO {

    public abstract void saveImg(InputStream paramInputStream, String paramString);
    
    public void delImg(String id) ;
}
