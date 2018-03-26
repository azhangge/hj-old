package com.hjedu.examination.dao;

import com.hjedu.examination.entity.module.ModuleExamInfo;

public interface IModuleExamInfoDAO {

    public abstract void updateModuleExamInfo(ModuleExamInfo m);

    public abstract ModuleExamInfo findModuleExamInfo();


}
