package com.hjedu.examination.dao;

import java.io.InputStream;
import java.util.List;

import com.hjedu.examination.entity.FileQuestion;

public interface IFileQuestionDAO {

    public abstract void addFileQuestion(FileQuestion m);

    public abstract void updateFileQuestion(FileQuestion m);

    public abstract void deleteFileQuestion(String id);

    public abstract void deleteFileQuestionByModule(String moduleId);

    public abstract FileQuestion findFileQuestion(String id);

    public abstract FileQuestion findFileQuestionByName(String id);
    
    public abstract FileQuestion findFileQuestionByHashCode(String id);

    public abstract List<FileQuestion> findAllFileQuestion();

    public abstract List<FileQuestion> findFileQuestionByModule(String moduleId);

    public long getQuestionNumByModule(String id);

    public List<FileQuestion> findQuestionByLike(String str,String businessId);

    public abstract void saveFile(InputStream paramInputStream, String itemId);

    public void delFile(String itemId);

    public boolean checkIfAttached(String itemId);

    public List<FileQuestion> getRandomQuestionOfNumInModule(long num, String mid);

    public FileQuestion findQuestionByIndex(int index, String mid);

}
