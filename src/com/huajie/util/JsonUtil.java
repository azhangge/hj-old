package com.huajie.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hjedu.course.dao.ILessonDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.Lesson;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.IBbsFileDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IDictionaryDAO;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IJudgeQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.DictionaryModel;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.ExamMultiChoice;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.JudgeQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.examination.entity.module2.ModuleExamination2;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.BbsFileModel;
import com.huajie.app.util.DateUtil;
import com.huajie.app.util.FileUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.app.util.UrlUtil;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.FFMPEGLocator;
import it.sauronsoftware.jave.MultimediaInfo;

public class JsonUtil {
	static IChoiceQuestionDAO choiceQuestionDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
	static IMultiChoiceQuestionDAO multiCQuestionDAO = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
	static IJudgeQuestionDAO judgeqQestionDAO = SpringHelper.getSpringBean("JudgeQuestionDAO");
	static ISystemConfigDAO systemConfigDAO = SpringHelper.getSpringBean("SystemConfigDAO");
	static IExamModule2DAO dicDAO = SpringHelper.getSpringBean("ExamModule2DAO");
	static ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
	static ILessonDAO lessonDAO = SpringHelper.getSpringBean("LessonDAO");
	static IDictionaryDAO dictionaryDAO = SpringHelper.getSpringBean("DictionaryDAO");
	static IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
	static IExamCaseCacheService cacheService = SpringHelper.getSpringBean("ExamCaseCacheService");
	
	/**
	 * 生成知识点json文件
	 * @param examModuleModel. 知识点id
	 */
    public static boolean createJsonFileByModule(HttpServletRequest request,ExamModuleModel examModuleModel) {
    	//获取各种题型的题目
    	Map<String, List<?>> map = getAllQuestionsByModuleId(examModuleModel.getId());
    	List<ObjectNode> cqsJson = new LinkedList<>();
    	for(Entry<String, List<?>> e : map.entrySet()){
    		addQuestionToJson(e.getValue(),cqsJson,examModuleModel);
    	}
    	List<ObjectNode> list = new LinkedList<>();
    	list.addAll(cqsJson);
    	return createJsonFileOfQuestions(request,list,examModuleModel);
	}
    
    
    private static Map<String, List<?>> getAllQuestionsByModuleId(String id) {
    	Map<String, List<?>> map = new HashMap<>();
    	//获取选择题
    	List<ChoiceQuestion> cqs = choiceQuestionDAO.findChoiceQuestionByModule(id);
    	List<MultiChoiceQuestion> mcqs = multiCQuestionDAO.findMultiChoiceQuestionByModule(id);
    	List<JudgeQuestion> jqs = judgeqQestionDAO.findJudgeQuestionByModule(id);
    	map.put("ChoiceQuestion", cqs);
    	map.put("MultiChoiceQuestion", mcqs);
    	map.put("JudgeQuestion", jqs);
    	return map;
	}


	private static void addQuestionToJson(List<?> cqs, List<ObjectNode> cqsJson, ExamModuleModel examModuleModel) {
    	ObjectMapper mapper = new ObjectMapper();
    	@SuppressWarnings("unchecked")
		List<GeneralQuestion> qs = (List<GeneralQuestion>)cqs;
    	for(GeneralQuestion cq : qs){
    		ObjectNode rootNode = mapper.createObjectNode();
    		rootNode.put("knowledge_id",examModuleModel.getId());
    		rootNode.put("course",examModuleModel.getName());
    		questionToJson(rootNode, cq);
    		cqsJson.add(rootNode);
    	}
	}
	
	
	/**
	 * 
	 * @param rootNode
	 * @param q
	 */
	private static void questionToJson(ObjectNode rootNode, GeneralQuestion q) {
		rootNode.put("question_id",q.getId());
		rootNode.put("question",q.getName());
		rootNode.put("analysis",q.getRightStr());
		HashMap<String, Object> map = getQuestionNameAnswer(q);
		rootNode.put("question",ToDBC(map.get("question").toString()));
		rootNode.put("type",map.get("type").toString());
		rootNode.put("answer",map.get("answer").toString());
	}
	
	public static Map<String,Object> questionToMap(GeneralQuestion q){
		Map<String,Object> rootNode = new HashMap<>();
		rootNode.put("question_id",q.getId());
		rootNode.put("question",q.getName());
		rootNode.put("analysis",q.getRightStr());
//		HashMap<String, Object> map = getQuestionNameAnswer(q);
		if(q.getModule()!=null){
			rootNode.put("knowledge_id",q.getModule().getId());
			rootNode.put("course",q.getModule().getName());
		}else{
			rootNode.put("knowledge_id","");
			rootNode.put("course","");
		}
		rootNode.put("question",ToDBC(q.getQuestion()));
		rootNode.put("type",q.getType());
		rootNode.put("answer",q.getAnswers());
		return rootNode;
	}
	
	/**
	 * 返回一个map，包含question：问题的内容和选项，用\n分隔开内容和选项
	 * answer：问题的答案
	 * type：题型1单选，2多选，3判断
	 * @param q
	 * @return
	 */
	public static HashMap<String, Object> getQuestionNameAnswer(GeneralQuestion q){
		String question = trimString(q.getName());
		Object answer = "";
		String type = "";
		String realoption = "";
//		ObjectNode choiceNode = mapper.createObjectNode();
		HashMap<String, Object> map = new HashMap<>();
		if(q.getClass().equals(JudgeQuestion.class)){
			JudgeQuestion jq = (JudgeQuestion)q;
			question = jq.getName();
			answer = jq.getAnswer()?"Y":"N";
			type = "3判断题";
		}
		if(q.getClass().equals(MultiChoiceQuestion.class)){
			MultiChoiceQuestion mq = (MultiChoiceQuestion)q;
//			for(ExamMultiChoice c : mq.getChoices()){
//				choiceNode.put(c.getLabel(),c.getName());
//			}
//			rootNode.put("choices",choiceNode);
			for(ExamMultiChoice c : mq.getChoices()){
				question+="\n"+c.getName();
				realoption+=c.getLabel();
			}
			answer = mq.getAnswer()==null?"":mq.getAnswer();
			type = "2多选题";
		}
		if(q.getClass().equals(ChoiceQuestion.class)){
			ChoiceQuestion mq = (ChoiceQuestion)q;
//			for(ExamChoice c : mq.getChoices()){
//				choiceNode.put(c.getLabel(),c.getName());
//			}
//			rootNode.put("choices",choiceNode);
			for(ExamChoice c : mq.getChoices()){
				question+="\n"+c.getName();
				realoption+=c.getLabel();
			}
			answer = mq.getAnswer()==null?"":mq.getAnswer();
			type = "1单选题";
		}
		map.put("question", question);
		map.put("answer", answer);
		map.put("type", type);
		map.put("realoption", realoption);
		return map;
	}
	
	/**
	 * 将字符串s中的空格，制表符，回车去掉
	 * @param s
	 * @return
	 */
	public static String trimString(String s){
		return s.replace("\n","").replace("\t", "").replace(" ","").replace("\n","").replace("	","").trim();
	}
	
	private static boolean createJsonFileOfQuestions(HttpServletRequest request,List<ObjectNode> list, ExamModuleModel examModuleModel) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
//		rootNode.put("material_id",examModuleModel.getId());
//		rootNode.put("name",examModuleModel.getName());
		ArrayNode questionList = rootNode.putArray("question_list");
		questionList.addAll(list);
		JsonFactory jsonFactory = new JsonFactory();
		File pathFile = new File(getSaveFilePath()+"json"+request.getServerPort());
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		File file = new File(getSaveFilePath()+"json"+request.getServerPort()+"/"+examModuleModel.getId()+".json");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			JsonGenerator jsonGenerator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
			mapper.writeTree(jsonGenerator, rootNode);
			FileInputStream fis = new FileInputStream(file.getPath());
			//更新模块MD5和版本号
			String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
			IOUtils.closeQuietly(fis);
			examModuleModel.setMD5(md5);
			examModuleModel.setVersion(examModuleModel.getVersion()+1);
			examModuleModel.setJsonDownLoadUrl(getRelativePath(request)+examModuleModel.getId()+".json");
			examModuleModel.setJsonFilePath(file.getPath());
			dicDAO.updateExamModuleModel(examModuleModel);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取保存文件路径c:/file/
	 * @return
	 */
	public static String getSaveFilePath(){
		String dir = "";
        boolean f = systemConfigDAO.getSystemConfig().getIfRelative();
        if (f) {
            String tp = systemConfigDAO.getSystemConfig().getFilePath();
            if (!tp.endsWith("/")) {
                tp = tp + "/";
            }
            dir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(tp);
        } else {
            dir = systemConfigDAO.getSystemConfig().getFilePath();
        }
        if (!(dir.endsWith("\\")||dir.endsWith("/"))) {
            dir = dir + "/";
        }
        
        File f_dir0 = new File(dir);
        if (!f_dir0.exists()) {
            f_dir0.mkdir();
        }
        System.out.println("存储路径：" + dir);
        return dir;
	}
	
	/**
	 * 获取下载json文件的url--http://27.17.20.242:8080/exam2/servlet/DownloadJsonFile?id=
	 * @return
	 */
	public static String getRelativePath(HttpServletRequest request){
		if(request==null){
			request = JsfHelper.getRequest();
		}
        String dir = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+request.getContextPath()+"/servlet/DownloadJsonFile?id=";
        return dir;
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);

               }
             }
        String returnString = new String(c);
             return returnString;
    }


	public static boolean createJsonZipByLessonTypeId(HttpServletRequest request, String id) {
		Map<String, File> zipFiles = new HashMap<>();
		
		LessonType lt = lessonTypeDAO.findLessonType(id);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		ArrayNode examList = rootNode.putArray("examList");
		ArrayNode materialList = rootNode.putArray("materialList");
		ArrayNode practiceList = rootNode.putArray("practiceList");
		for(Examination e : lt.getExams()){
			ObjectNode eNode = mapper.createObjectNode();
			eNode.put("exam_id", e.getId());
			eNode.put("exam_name", e.getName());
			eNode.put("exam_question_num", String.valueOf(getQuestionsNum(e)));
			eNode.put("exam_start_time",DateUtil.formateDateToString(e.getAvailableBegain()));
			eNode.put("exam_end_time", DateUtil.formateDateToString(e.getAvailableEnd()));
			eNode.put("exam_remain_num", String.valueOf(e.getMaxNum()));
			eNode.put("exam_total_score", getScoreOfExamination(e));
			eNode.put("exam_pass_score", e.getQualified()+"");
			if(e.getPaperType().equals("random")){
				eNode.put("exam_type",e.getRandomPaper().getIfSimulate());
			}else{
				eNode.put("exam_type","1");
			}
			eNode.put("exam_total_time",e.getTimeLen()*60+"");
			examList.add(eNode);
		}
		List<ModuleExamination2> pl = new LinkedList<>();
		for(Lesson m : lessonDAO.findAllShowedLessonByType(lt.getId())){
			ObjectNode material = mapper.createObjectNode();
			material.put("material_id", m.getId());
			material.put("material_name", m.getName());
			material.put("material_score",m.getScorePaid()+"");
			material.put("material_leastTime",m.getLeastTime()+"");
			material.put("material_classNum",m.getClassNum()+"");
			List<BbsFileModel> bfms = m.getFiles();
			if(bfms!=null&&bfms.size()>0){
				BbsFileModel bfm = bfms.get(0);
				material.put("material_type", getlTypeByBbsFileModel(bfm));
				material.put("material_download_url", getDownloadUrl(request)+"/servlet/DownloadFile?id="+bfm.getId());
				material.put("material_url",UrlUtil.getMp4VirtualUrlByRequest(request)+bfm.getId()+".mp4");
				material.put("material_size", bfm.getFileSize());
				material.put("vedio_totalTime", bfm.getTotal_time());
				material.put("screenshot",getDownloadUrl(request)+ bfm.getFileFullPath());
				String path = "";
				try {
					path = FileUtil.getFileRealPath(bfm);
					if(!StringUtil.isEmpty(path)){
						FileInputStream fis = new FileInputStream(path);
						String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
						IOUtils.closeQuietly(fis);
						material.put("material_md5", md5);
						if(!bfm.getFileExt().equals(".mp4")){
							File f = new File(path);
							zipFiles.put(bfm.getId()+bfm.getFileExt(), f);
						}
					}
				} catch (Exception e) {
					
				}
			}
			material.put("material_version", String.valueOf(m.getVersion()));
			material.put("knowledge_point_id", getKnowledgePointIds(m));
			materialList.add(material);
			List<ModuleExamination2> l = m.getModuleExaminations();
			pl.addAll(l);
		}
		for(ModuleExamination2 m : pl){
			ObjectNode practice = mapper.createObjectNode();
			practice.put("practice_id", m.getModule().getId());
			practice.put("practice_num",getTotalNumOfModule(m.getModule())+"");
			practice.put("practice_name",m.getModule().getName());
			String path = m.getModule().getJsonFilePath();
			if(StringUtil.isEmpty(path)){
				JsonUtil.createJsonFileByModule(request,m.getModule());
			}
			practice.put("practice_version", String.valueOf(m.getVersion()));
			practice.put("practice_url", m.getModule().getJsonDownLoadUrl());
			practice.put("practice_md5", m.getModule().getMD5());
			practiceList.add(practice);
			File f = new File(m.getModule().getJsonFilePath());
			zipFiles.put(f.getName(),f);
		}
		JsonFactory jsonFactory = new JsonFactory();
		String zipPath = getSaveFilePath()+"lesson/"+lt.getId()+"/";
		File f_dir = new File(zipPath);
		if (!f_dir.exists()) {
			f_dir.mkdirs();
		}
		File file = new File(zipPath+"config"+".json");
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			JsonGenerator jsonGenerator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);
			mapper.writeTree(jsonGenerator, rootNode);
			
			zipFiles.put("config.json",file);
			File zip = fileToZip2(zipFiles, zipPath, lt.getId());
			
			FileInputStream fis = new FileInputStream(zip);
			//更新模块MD5和版本号
			String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
			IOUtils.closeQuietly(fis);
			lt.setMD5(md5);
			lt.setVersion(lt.getVersion()+1);
			lt.setJsonDownLoadUrl(getRelativePath(request)+lt.getId()+".zip");
			lt.setJsonFilePath(zip.getPath());
			lessonTypeDAO.updateLessonType(lt);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取试题模块的总题数，目前只统计单选，多选，判断
	 * @param e
	 * @return
	 */
	public static long getTotalNumOfModule(ExamModuleModel e){
		return e.getChoiceNum()+e.getJudgeNum()+e.getMultiChoiceNum();
	}
	
	/**
	 * 获取考试的总题数
	 * @param e
	 * @return
	 */
	public static int getQuestionsNum(Examination e){
		int i = e.getChoiceTotal()+e.getMultiChoiceTotal()+e.getCaseTotal()+
				e.getEssayTotal()+e.getFileTotal()+e.getFillTotal()+e.getJudgeTotal();
		return i;
	}
	
	/**
	 * 获取考试的总分
	 * @param e
	 * @return
	 */
	public static String getScoreOfExamination(Examination e){
		String type = e.getPaperType();
		double score = 0;
		if(type.equals("random")){//简单随机试卷
			ExamPaperRandom epr = e.getRandomPaper();
			score = epr.getTotalScore();
		}else if(type.equals("random2")){//随机试卷
			score = e.getRandom2Paper().getTotalScore();
		}else{//人工试卷
			score = e.getManualPaper().getTotalScore();
		}
		return String.valueOf(score);
	}
	
	/**
	 * 通过文件扩展名获取文件类型
	 * @param bfm
	 * @return
	 */
	public static String getlTypeByBbsFileModel(BbsFileModel bfm) {
		if(bfm.getFileExt().equals(".epub")){
			return "1";
		}else if(bfm.getFileExt().contains(".ppt")){
			return "3";
		}else if(bfm.getFileExt().contains(".mp4")){
			return "2";
		}else{
			return "4";
		}
	}
	/**
	 * 获取url前缀例如：http://27.17.20.242:8080/exam2
	 * @param request
	 * @return
	 */
	public static String getDownloadUrl(HttpServletRequest request){
        String dir = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+request.getContextPath();
        return dir;
	}
	/**
	 * 根据课程获取知识点id，多个id用;隔开
	 * @param m
	 * @return
	 */
	private static String getKnowledgePointIds(Lesson m){
		String ids = "";
		List<ModuleExamination2> mes = m.getModuleExaminations();
		for(ModuleExamination2 me : mes){
			ids += me.getModule().getId()+";";
		}
		return ids;
	}
	/** 
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 
     * @param sourceFilePath :待压缩的文件路径 
     * @param zipFilePath :压缩后存放路径 
     * @param fileName :压缩后文件的名称 
     * @return 
     */
	public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){  
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");  
                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){  
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");  
                    }else{  
                        fos = new FileOutputStream(zipFile);  
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                        byte[] bufs = new byte[1024*10];  
                        for(int i=0;i<sourceFiles.length;i++){  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFiles[i]);  
                            bis = new BufferedInputStream(fis, 1024*10);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                        }  
                        flag = true;  
                    }  
                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    } 
	
	public static File fileToZip2(Map<String, File> zipFiles,String zipFilePath,String fileName){  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
        File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
        try {  
        	fos = new FileOutputStream(zipFile);  
        	zos = new ZipOutputStream(new BufferedOutputStream(fos));  
        	byte[] bufs = new byte[1024*10];  
        	for(Entry<String, File> zipf : zipFiles.entrySet()){  
        		//创建ZIP实体，并添加进压缩包  
        		ZipEntry zipEntry = new ZipEntry(zipf.getKey());  
        		zos.putNextEntry(zipEntry);  
        		//读取待压缩的文件并写进压缩包里  
        		fis = new FileInputStream(zipf.getValue());  
        		bis = new BufferedInputStream(fis, 1024*10);  
        		int read = 0;  
        		while((read=bis.read(bufs, 0, 1024*10)) != -1){  
        			zos.write(bufs,0,read);  
        		}  
        	}  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        } finally{  
            //关闭流  
            try {  
                if(null != bis) bis.close();  
                if(null != zos) zos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            }  
        }  
        return zipFile;  
    } 
	
	/**
	 * 格式化
	 * @param jsonStr
	 * @return
	 * @author   lizhgb
	 * @Date   2015-10-14 下午1:17:35
	 */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }
    
    /**
     * 添加space
     * @param sb
     * @param indent
     * @author   lizhgb
     * @Date   2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    
    public static DictionaryModel getCompanyByPart(DictionaryModel p){
    	String busId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	if(p==null){
    		return null;
    	}
    	if(p.getFatherId().equals(busId)||p.getId().equals(busId)){
    		return p;
    	}else{
    		return getCompanyByPart(dictionaryDAO.findDictionaryModel(p.getFatherId()));
    	}
    }
    
    public static void Ranking(String examid){
	    List<ExamCase> cases = examCaseDAO.findExamCaseByExamination(examid);
	    rankExamCase(cases);
	    int r = 1;
	    for (ExamCase cc : cases) {
	        cc.setRanking(r);
	        r++;
	    }
	    //更新缓存
	    final List<ExamCase> ts2 = cases;
	    new Thread(new Runnable() {
	        public void run() {
	            for (ExamCase ec : ts2) {
	                cacheService.updateExamCaseIfExists(ec);
	            }
	        }
	    }).start();
	    //批量更新数据库
	    examCaseDAO.updateBulkExamCase(cases);
    }
    
    @SuppressWarnings("unchecked")
	public static void rankExamCase(List<ExamCase> cases) {
        Collections.sort(cases, new Comparator() {
            @Override
            public int compare(Object a, Object b) {
                ExamCase aa = (ExamCase) a;
                ExamCase bb = (ExamCase) b;
                double gap = aa.getScore() - bb.getScore();
                if (gap >= 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        
    }
    
    /**
	 * 根据考试id获取根据分数高低排列好的考试实例list
	 * @param exam_id 考试id
	 * @return
	 */
	public static List<ExamCase> getRankedExamCaseList(String exam_id){
		List<ExamCase> es = examCaseDAO.findFormalExamCaseByExamination(exam_id);
		List<ExamCase> sortlist = new ArrayList<>();
		if(es!=null&&es.size()>0){
			Map<String, ExamCase> mape = new HashMap<>();
			//获取用户该考试最高成绩考试实例
			for(ExamCase e : es){
				BbsUser user = e.getUser();
				if(user!=null){
					String userId = user.getId();
					if(mape.get(userId)==null){
						mape.put(userId, e);
					}else{
						Double score = e.getScore();
						Double score2 = mape.get(userId).getScore();
						if(score>score2){
							mape.put(userId, e);
						}
					}
				}
			}
			for(Entry<String, ExamCase> e : mape.entrySet()){
				sortlist.add(e.getValue());
			}
			rankExamCase(sortlist);
		}
		return sortlist;
	}
	public static long getVedioTotalTime(BbsFileModel bf,final String path){  
		String bfpath = FileUtil.getFileRealPath(bf);
		// *.mp4，*.flv，*..3gp格式均可,其他自行测试  
		// <vedio_path>是你的视频文件路径  
		File source = new File(bfpath);  
		FFMPEGLocator locator = new FFMPEGLocator() {  
			@Override  
			protected String getFFMPEGExecutablePath() {  
				// <ffmpeg_path>是你的ffmpeg.exe路径  
				return path+"ffmpeg.exe";  
			}  
		};  
		Encoder encoder = new Encoder(locator);  
		long ls = 0;
		try {  
			MultimediaInfo m = encoder.getInfo(source);  
			ls = m.getDuration();  
			// 输出毫秒数  
//				System.out.println(ls);  
			// 输出0时0分0秒0毫秒的格式  
//				System.out.println("此视频时长为:" + ls / (60 * 60 * 1000) + "时" + (ls % (60 * 60 * 1000)) / 60000 + "分"  
//						+ ((ls % (60 * 60 * 1000)) % 60000) / 1000 + "秒" + (((ls % (60 * 60 * 1000)) % 60000) % 1000)  
//						+ "毫秒！");  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return ls/1000;
		
    }
	
	public static void test(String path){
		IBbsFileDAO bbsFileDAO = SpringHelper.getSpringBean("BbsFileDAO");
		List<BbsFileModel> bfms = bbsFileDAO.findFilesByEXT(".mp4");
		for(BbsFileModel bf : bfms){
			long ls = getVedioTotalTime(bf,path);
			bf.setTotal_time(ls);
			bbsFileDAO.updateClientFileInfo(bf);
		}
	}
}
