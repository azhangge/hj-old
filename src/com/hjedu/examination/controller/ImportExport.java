package com.hjedu.examination.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IModuleExamCaseDAO;
import com.hjedu.examination.service.impl.ExamImportService;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ImportExport implements Serializable{

    ILogService logger = SpringHelper.getSpringBean("LogService");
    ExamImportService eis = SpringHelper.getSpringBean("ExamImportService");

    @PostConstruct
    public void init() {
    }

    public String up_action(FileUploadEvent event) {
        try {
            UploadedFile item = event.getFile();
            String n = item.getFileName();
            int l2 = n.lastIndexOf(".");
            String ext = "." + n.substring(l2 + 1).toLowerCase();
            long imgId = System.nanoTime();
            String sn = imgId + ext;
            InputStream fis = item.getInputstream();
            byte[] bb = new byte[1024];
            String tp = "upload/";
            String dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
            if (!dir.endsWith("\\")) {
                dir = dir + "\\";
            }

            String nfn = dir + sn;
            File ffff = new File(nfn);
            FileOutputStream fos = new FileOutputStream(ffff);
            BufferedInputStream is = new BufferedInputStream(fis);
            BufferedOutputStream os = new BufferedOutputStream(fos);// 将加密过的文件流转换为缓冲流
            int len = 0;
            while ((len = is.read(bb)) != -1) {
                os.write(bb, 0, len);
            }
            is.close();
            os.close();
            fis.close();
            fos.close();
            MyLogger.echo("upload executed.");
            this.eis.import1(nfn);

            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_INFO);
            fm.setSummary("导入题库完成！");
            FacesContext.getCurrentInstance().addMessage("", fm);

            File ff = new File(nfn);
            ff.delete();
            this.logger.log("导入了题库");
            AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
            asb.refreshAdmin();
//            Menu menu = JsfHelper.getBean("menu");
//            menu.loadStructure();
            this.refreshCache();
            this.refreshChangePanel();
            return null;
        } catch (Exception ee) {
            ee.printStackTrace();
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_FATAL);
            fm.setSummary("导入题库出错了！请仔细核对您的题库文件格式是否正确");
            FacesContext.getCurrentInstance().addMessage("", fm);
            this.logger.log("试图导入题图，但导入出错");
            return null;
        }
    }
    
    /**
     * 更新 移动 模块时显示的目录结构
     */
    private void refreshChangePanel() {
        ChangeModuleDir change = JsfHelper.getBean("changeModuleDir");
        if (change != null) {
            change.init();
        }
    }

    private void refreshCache() {
        ExamModule2Service moduleService = SpringHelper.getSpringBean("ExamModule2Service");
        moduleService.reBuildCache();
    }

    /**
     * 清除所有成绩数据、试题、模块
     * @return 
     */
    public String clearAllQuestions() {
        IExamCaseDAO caseDAO=SpringHelper.getSpringBean("ExamCaseDAO");
        caseDAO.deleteAllExamCase();//清空试题前清空所有成绩数据
        IContestCaseDAO ccaseDAO=SpringHelper.getSpringBean("ContestCaseDAO");
        ccaseDAO.deleteAllContestCase();
        IBuffetExamCaseDAO bcaseDAO=SpringHelper.getSpringBean("BuffetExamCaseDAO");
        bcaseDAO.deleteAllBuffetExamCase();
        IModuleExamCaseDAO mcaseDAO=SpringHelper.getSpringBean("ModuleExamCaseDAO");
        mcaseDAO.deleteAllModuleExamCase();
        
        this.logger.log("清空了所有题库");
        ExamModule2Service moduleDAO = SpringHelper.getSpringBean("ExamModule2Service");
        moduleDAO.deleteAllExamModule();
        FacesMessage fm = new FacesMessage();
        fm.setSeverity(FacesMessage.SEVERITY_INFO);
        fm.setSummary("清空题库完成！所有的试题模块和试题都被删除了！");
        FacesContext.getCurrentInstance().addMessage("", fm);
        return null;
    }
}
