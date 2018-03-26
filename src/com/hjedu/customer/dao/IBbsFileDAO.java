package com.hjedu.customer.dao;


import java.io.InputStream;
import java.util.List;

import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.entity.BbsFileModel;
import com.hjedu.platform.entity.FileSaveStatus;

public abstract interface IBbsFileDAO {

    public abstract String getRelativeFilePath();

    public abstract boolean checkNameIfExist(String name, String fatherId);
    
    public abstract boolean checkNameIfExistByUsr(String name, String fatherId,String uid);

    public abstract List<BbsFileModel> findAllClientFile();
 
    public abstract List<BbsFileModel> findAllClientFileByUsr(String uid, String dic);
    
    public abstract BbsFileModel findAllClientFileByLessionId(String lid);

    public abstract List<BbsFileModel> findAllClientFileByUsr(String urn);

    public abstract List<BbsFileModel> findAllSonClientFile(String dic);

    public List<BbsFileModel> findAllSonDirsByUsr(String dic, String uid);

    public abstract List<BbsFileModel> searchClientFile(String field,String keyWord);

    public abstract BbsFileModel findClientFile(String paramString);

    public abstract BbsFileModel findClientFile2(String paramString);
    
    public abstract void saveClientFileInfo(BbsFileModel cfm);

    public abstract void delClientFile(String paramString);

    public void delClientFileAndAllDescendants(String id);

    public abstract void loadAllDescendants(String fid, List<BbsFileModel> sons);

    public List<BbsFileModel> loadAllDescendants(String fid);

    public abstract void delClientFilebyUsr(String user_id);

    public abstract void updateClientFileInfo(BbsFileModel paramProject2Model);

    public void saveFile(InputStream is, String id, String userId);

    public void saveFile(InputStream is, String id, String userId, FileSaveStatus stat);
    
    public List<BbsFileModel> findAllDeptSharedClientFileByUsr(final String uid);

    public List<BbsFileModel> findAllSharedClientFileByUsr(final String uid);

    public List<BbsFileModel> genShareFamilyTree(String id, List<BbsFileModel> ffs);

    public List<BbsFileModel> findAllAccessableClientFileByDept(final String dept,final String dic);

	public abstract List<BbsFileModel> findAllFilesOfLesson(String fatherID);

	public abstract List<BbsFileModel> findFilesByEXT(String string);

	int delClientFilesByLessonTypeId(List<String> idlist);
 
}
