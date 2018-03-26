package com.hjedu.examination.service;

import com.hjedu.examination.entity.module.ModuleExamCase;
import com.hjedu.examination.entity.module.ModuleExamCaseItemFill;
import com.hjedu.examination.entity.module2.ModuleExam2CaseItemAdapter;

public interface IModuleExamCaseService {

    public void buildExamCase(ModuleExamCase ec);

    public ModuleExamCase computeExamCase(ModuleExamCase ec);

    public double computeTotalScore(ModuleExamCase ec);
    
    public ModuleExam2CaseItemAdapter computeSingleAdapter(ModuleExam2CaseItemAdapter adapter);

    //public ExamRoom confirmExamRoom(String ip);

    //public Boolean compareIp(String o, String target);
    

    public void buildItemFillBlocks(ModuleExamCaseItemFill fill);

    public ModuleExamCase preSaveExamCase(ModuleExamCase ec);
    
    public ModuleExamCase resumeExamCase(ModuleExamCase ec);
    
    //public Boolean checkIfSupplementaryExamination(String examId, String userId) ;
}
